package daoService.DAOimpl;

import daoService.DAO;
import dbconnection.DatabaseConnection;
import entity.Customer;
import util.ResultSetMapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The class provides methods for CRUD operations with the Database.
 */
public class CustomerDAO implements DAO<Customer> {
    private final Connection connection;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;

    public CustomerDAO() throws SQLException, IOException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Saves the record to the database.
     * @param customer - Object of type Customer.
     */
    @Override
    public void save(Customer customer) {
        try {
            this.preparedStatement = this.connection.prepareStatement("INSERT INTO customer \n" +
                    "(customer_id," +
                    "name, " +
                    "email, " +
                    "phone, " +
                    "age, " +
                    "address, " +
                    "city, " +
                    "postal_code, " +
                    "country, " +
                    "consent_status, " +
                    "is_profile_active, " +
                    "date_profile_created, " +
                    "date_profile_deactivated, " +
                    "reason_for_deactivation, " +
                    "notes) \n" +
                    "VALUES \n" +
                    "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            this.preparedStatement.setLong(1, customer.getCustomer_id());
            this.preparedStatement.setString(2, customer.getName());
            this.preparedStatement.setString(3, customer.getEmail());
            this.preparedStatement.setString(4, customer.getPhone());
            this.preparedStatement.setInt(5, customer.getAge());
            this.preparedStatement.setString(6, customer.getAddress());
            this.preparedStatement.setString(7, customer.getCity());
            this.preparedStatement.setInt(8, customer.getPostal_code());
            this.preparedStatement.setString(9, customer.getCountry());
            this.preparedStatement.setBoolean(10, customer.isConsent_status());
            this.preparedStatement.setBoolean(11, customer.is_profile_active());
            this.preparedStatement.setDate(12, customer.getDate_profile_created());
            this.preparedStatement.setDate(13, customer.getDate_profile_deactivated());
            this.preparedStatement.setString(14, customer.getReason_for_deactivation());
            this.preparedStatement.setString(15, customer.getNotes());

            this.preparedStatement.executeUpdate();
            System.out.println("Customer saved successfully.");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Deletes the record from the database.
     * @param id - id of the customer record
     */
    @Override
    public void deleteById(Long id) {
        try {
            this.preparedStatement = this.connection.prepareStatement("DELETE FROM customer WHERE customer_id = ?");
            this.preparedStatement.setLong(1, id);
            int i = this.preparedStatement.executeUpdate();
            System.out.printf("The records with id %d is deleted.%n", id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes all records in the table.
     */
    @Override
    public void deleteAll() {
        try {
            this.preparedStatement = this.connection.prepareStatement("DELETE FROM customer");
            int i = this.preparedStatement.executeUpdate();
            System.out.printf("All %d records are deleted.%n", i);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return all records from the table.
     */
    @Override
    public List<Customer> getAllRecords() {
       List<Customer> allCustomers = new ArrayList<>();
        ResultSetMapper resultSetMapper = new ResultSetMapper();
        try {
            this.preparedStatement = this.connection.prepareStatement("SELECT * FROM customer");
            ResultSet resultSet = this.preparedStatement.executeQuery();
            allCustomers = resultSetMapper.mapResultSetToObject(resultSet, Customer.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCustomers;
    }

    /**
     * Takes a single record from the table by id.
     * @param id - id of the customer record
     * @return - record from the Database with the id, given in the method parameter
     */
    @Override
    public Customer getById(Long id) {
        Customer searchedCustomer = null;
        try {
            this.preparedStatement = this.connection.prepareStatement
                    ("SELECT * FROM customer WHERE customer_id = ?");
            this.preparedStatement.setLong(1, id);
            this.resultSet = preparedStatement.executeQuery();

            while (this.resultSet.next()) {
                searchedCustomer = Customer.builder()
                        .customer_id(resultSet.getLong("customer_id"))
                        .name(resultSet.getString("name"))
                        .email(resultSet.getString("email"))
                        .phone(resultSet.getString("phone"))
                        .age(resultSet.getInt("age"))
                        .address(resultSet.getString("address"))
                        .city(resultSet.getString("city"))
                        .postal_code(resultSet.getInt("postal_code"))
                        .country(resultSet.getString("country"))
                        .consent_status(resultSet.getBoolean("consent_status"))
                        .is_profile_active(resultSet.getBoolean("is_profile_active"))
                        .date_profile_created(resultSet.getDate("date_profile_created"))
                        .date_profile_deactivated(resultSet.getDate("date_profile_deactivated"))
                        .reason_for_deactivation(resultSet.getString("reason_for_deactivation"))
                        .notes(resultSet.getString("notes"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.printf(String.format("Customer with id %d is:%n", id));
        System.out.println(searchedCustomer);
        return searchedCustomer;
    }

    /**
     * Takes a list of records by list of ids.
     * @param ids - List of ids of the customer records.
     * @return - ArrayList containing list of records with the ids, given in the method parameter.
     */
    @Override
    public List<Customer> getByIds(List<Long> ids) {
        ResultSetMapper resultSetMapper = new ResultSetMapper();
        try {
            this.preparedStatement = this.connection.prepareStatement
                    (String.format("SELECT * FROM customer WHERE customer_id IN (%s)",
                            ids
                                    .stream()
                                    .map(id -> "?")
                                    .collect(Collectors.joining(", "))));

            for (int id = 0; id < ids.size(); id++) {
                this.preparedStatement.setLong(id + 1, ids.get(id));
            }
            this.resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Customer> searchedCustomers = resultSetMapper.mapResultSetToObject(this.resultSet, Customer.class);
        System.out.println("Searched customers are:");
        searchedCustomers
                .stream()
                .forEach(System.out::println);
        return searchedCustomers;
    }

    /**
     * Takes the count of all records in the table.
     * @return - the count of all records in the table.
     */
    @Override
    public int getAllRecordsCount() {
        int recordsCount = 0;
        try {
            this.preparedStatement = this.connection.prepareStatement("SELECT COUNT(customer_id) FROM customer");
            ResultSet resultSet = this.preparedStatement.executeQuery();
            if (resultSet.next()) {
                recordsCount = resultSet.getInt(1);
                System.out.printf("All records count is: %d%n", recordsCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recordsCount;
    }

    /**
     * Takes a random record.
     * @return - returns random record from the Database.
     */
    @Override
    public Customer getRandomId() {
        Customer randomCustomer = null;
        try {
            this.preparedStatement = this.connection.prepareStatement
                    ("SELECT * FROM customer ORDER BY RANDOM() LIMIT 1");
            this.resultSet = preparedStatement.executeQuery();

            while (this.resultSet.next()) {
                randomCustomer = Customer.builder()
                        .customer_id(resultSet.getLong("customer_id"))
                        .name(resultSet.getString("name"))
                        .email(resultSet.getString("email"))
                        .phone(resultSet.getString("phone"))
                        .age(resultSet.getInt("age"))
                        .address(resultSet.getString("address"))
                        .city(resultSet.getString("city"))
                        .postal_code(resultSet.getInt("postal_code"))
                        .country(resultSet.getString("country"))
                        .consent_status(resultSet.getBoolean("consent_status"))
                        .is_profile_active(resultSet.getBoolean("is_profile_active"))
                        .date_profile_created(resultSet.getDate("date_profile_created"))
                        .date_profile_deactivated(resultSet.getDate("date_profile_deactivated"))
                        .reason_for_deactivation(resultSet.getString("reason_for_deactivation"))
                        .notes(resultSet.getString("notes"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Random customer is:");
        System.out.println(randomCustomer);
        return randomCustomer;
    }

    /**
     * Takes a random list of records.
     * @param randomCount - the count of random records.
     * @return - returns list of random records from the Database.
     */
    @Override
    public List<Customer> getRandomIds(int randomCount) {
        ResultSetMapper resultSetMapper = new ResultSetMapper();
        List<Customer> randomCustomers = null;
        try {
            this.preparedStatement = this.connection.prepareStatement
                    ("SELECT * FROM (SELECT DISTINCT * FROM customer) as AL ORDER BY RANDOM() LIMIT ?");
            this.preparedStatement.setInt(1, randomCount);
            this.resultSet = preparedStatement.executeQuery();
            randomCustomers = resultSetMapper.mapResultSetToObject(this.resultSet, Customer.class);
        } catch (SQLException e) {
            e.getMessage();
        }
        System.out.printf(String.format("Random %d customers are:%n", randomCount));
        randomCustomers
                .stream()
                .forEach(System.out::println);
        return randomCustomers;
    }
}
