package daoService.DAOimpl;

import daoService.DAO;
import dbconnection.DatabaseConnection;
import entity.Address;
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
public class AddressDAO implements DAO<Address> {
    private final Connection connection;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;

    public AddressDAO() throws SQLException, IOException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Saves the record to the database.
     * @param address - Object of type Address.
     */
    @Override
    public void save(Address address) {
        try {
            this.preparedStatement = this.connection.prepareStatement("INSERT INTO address \n" +
                    "(address_id, customer_id, address, city, province, state_UK, postal_code, country) \n" +
                    "VALUES \n" +
                    "(?, ?, ?, ?, ?, ?, ?, ?)");
            this.preparedStatement.setLong(1, address.getAddress_id());
            this.preparedStatement.setLong(2, address.getCustomer_id());
            this.preparedStatement.setString(3, address.getAddress());
            this.preparedStatement.setString(4, address.getCity());
            this.preparedStatement.setString(5, address.getProvince());
            this.preparedStatement.setString(6, address.getState_uk());
            this.preparedStatement.setInt(7, address.getPostal_code());
            this.preparedStatement.setString(8, address.getCountry());

            this.preparedStatement.executeUpdate();
            System.out.println("Address saved successfully.");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Deletes the record from the database.
     * @param address_id - id of the address record
     */
    @Override
    public void deleteById(Long address_id) {
        try {
            this.preparedStatement = this.connection.prepareStatement("DELETE FROM address WHERE address_id = ?");
            this.preparedStatement.setLong(1, address_id);
            int i = this.preparedStatement.executeUpdate();
            System.out.printf("The records with id %d is deleted.%n", address_id);
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
            this.preparedStatement = this.connection.prepareStatement("DELETE FROM address");
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
    public List<Address> getAllRecords() {
        List<Address> allAddresses = new ArrayList<>();
        ResultSetMapper resultSetMapper = new ResultSetMapper();
        try {
            this.preparedStatement = this.connection.prepareStatement("SELECT * FROM address");
            ResultSet resultSet = this.preparedStatement.executeQuery();
            allAddresses = resultSetMapper.mapResultSetToObject(resultSet, Address.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allAddresses;
    }

    /**
     * Takes a single record from the table by id.
     * @param id - id of the address record
     * @return - record from the Database with the id, given in the method parameter
     */
    @Override
    public Address getById(Long id) {
        Address searchedAddress = null;
        try {
            this.preparedStatement = this.connection.prepareStatement
                    ("SELECT * FROM address WHERE address_id = ?");
            this.preparedStatement.setLong(1, id);
            this.resultSet = preparedStatement.executeQuery();

            while (this.resultSet.next()) {
                searchedAddress = Address.builder()
                        .address_id(resultSet.getLong("address_id"))
                        .customer_id(resultSet.getLong("customer_id"))
                        .address(resultSet.getString("address"))
                        .city(resultSet.getString("city"))
                        .province(resultSet.getString("province"))
                        .state_uk(resultSet.getString("state_uk"))
                        .postal_code(resultSet.getInt("postal_code"))
                        .country(resultSet.getString("country"))
                        .build();
            }
            System.out.printf(String.format("Address with id %d is:%n", id));
            System.out.println(searchedAddress);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return searchedAddress;
    }

    /**
     * Takes a list of records by list of ids.
     * @param ids - List of ids of the address records.
     * @return - ArrayList containing list of records with the ids, given in the method parameter.
     */
    @Override
    public List<Address> getByIds(List<Long> ids) {
        ResultSetMapper resultSetMapper = new ResultSetMapper();
        try {
            this.preparedStatement = this.connection.prepareStatement
                    (String.format("SELECT * FROM address WHERE address_id IN (%s)",
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
        List<Address> searchedAddresses = resultSetMapper.mapResultSetToObject(this.resultSet, Address.class);
        System.out.println("Searched addresses are:");
        searchedAddresses
                .stream()
                .forEach(System.out::println);
        return searchedAddresses;
    }

    /**
     * Takes the count of all records in the table.
     * @return - the count of all records in the table.
     */
    @Override
    public int getAllRecordsCount() {
        int recordsCount = 0;
        try {
            this.preparedStatement = this.connection.prepareStatement("SELECT COUNT(address_id) FROM address");
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
    public Address getRandomId() {
        Address randomAddress = null;
        try {
            this.preparedStatement = this.connection.prepareStatement
                    ("SELECT * FROM address ORDER BY RANDOM() LIMIT 1");
            this.resultSet = preparedStatement.executeQuery();

            while (this.resultSet.next()) {
                randomAddress = Address.builder()
                        .address_id(resultSet.getLong("address_id"))
                        .customer_id(resultSet.getLong("customer_id"))
                        .address(resultSet.getString("address"))
                        .city(resultSet.getString("city"))
                        .province(resultSet.getString("province"))
                        .state_uk(resultSet.getString("state_uk"))
                        .postal_code(resultSet.getInt("postal_code"))
                        .country(resultSet.getString("country"))
                        .build();
                System.out.println("Random address is:");
                System.out.println(randomAddress);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return randomAddress;
    }

    /**
     * Takes a random list of records.
     * @param randomCount - the count of random records.
     * @return - returns list of random records from the Database.
     */
    @Override
    public List<Address> getRandomIds(int randomCount) {
        ResultSetMapper resultSetMapper = new ResultSetMapper();
        List<Address> randomAddresses = null;
        try {
            this.preparedStatement = this.connection.prepareStatement
                    ("SELECT * FROM (SELECT DISTINCT * FROM address) as AL ORDER BY RANDOM() LIMIT ?");
            this.preparedStatement.setInt(1, randomCount);
            this.resultSet = preparedStatement.executeQuery();
            randomAddresses = resultSetMapper.mapResultSetToObject(this.resultSet, Address.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.printf(String.format("Random %d addresses are:%n", randomCount));
        randomAddresses
                .stream()
                .forEach(System.out::println);
        return randomAddresses;
    }

    /**
     * Takes a records by given Customer id.
     * @param customer_id - id of the customer record
     * @return - returns object of type Address
     */
    public Address getAddressByCustomerId(Long customer_id) {
        Address address = new Address();
        try {
            this.preparedStatement = this.connection.prepareStatement
                    ("SELECT * FROM address WHERE address_id = ?");
            this.preparedStatement.setLong(1, customer_id);
            this.resultSet = preparedStatement.executeQuery();

            while (this.resultSet.next()) {
                address = Address.builder()
                        .address_id(resultSet.getLong("address_id"))
                        .customer_id(resultSet.getLong("customer_id"))
                        .address(resultSet.getString("address"))
                        .city(resultSet.getString("city"))
                        .province(resultSet.getString("province"))
                        .state_uk(resultSet.getString("state_uk"))
                        .postal_code(resultSet.getInt("postal_code"))
                        .country(resultSet.getString("country"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return address;
    }
}

