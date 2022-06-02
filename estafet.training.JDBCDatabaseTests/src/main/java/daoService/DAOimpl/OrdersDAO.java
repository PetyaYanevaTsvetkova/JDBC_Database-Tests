package daoService.DAOimpl;

import daoService.DAO;
import dbconnection.DatabaseConnection;
import entity.Customer;
import entity.Orders;
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
public class OrdersDAO implements DAO<Orders> {
    private final Connection connection;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;
    private CustomerDAO customerDAO;

    public OrdersDAO() throws SQLException, IOException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Saves the record to the database.
     *
     * @param order - Object of type Order.
     */
    @Override
    public void save(Orders order) {
        try {
            this.preparedStatement = this.connection.prepareStatement("INSERT INTO orders \n" +
                    "(order_id," +
                    "customer_id, " +
                    "is_order_completed, " +
                    "is_order_payed, " +
                    "date_of_order, " +
                    "date_order_completed) \n" +
                    "VALUES \n" +
                    "(?, ?, ?, ?, ?, ?)");
            this.preparedStatement.setLong(1, order.getOrder_id());
            this.preparedStatement.setLong(2, order.getCustomer_id());
            this.preparedStatement.setBoolean(3, order.is_order_completed());
            this.preparedStatement.setBoolean(4, order.is_order_payed());
            this.preparedStatement.setDate(5, order.getDate_of_order());
            this.preparedStatement.setDate(6, order.getDate_order_completed());

            this.preparedStatement.executeUpdate();
            System.out.println("Order saved successfully.");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Deletes the record from the database.
     *
     * @param id - id of the order record
     */
    @Override
    public void deleteById(Long id) {
        try {
            this.preparedStatement = this.connection.prepareStatement("DELETE FROM orders WHERE order_id = ?");
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
            this.preparedStatement = this.connection.prepareStatement("DELETE FROM orders");
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
    public List<Orders> getAllRecords() {
        List<Orders> allOrders = new ArrayList<>();
        ResultSetMapper resultSetMapper = new ResultSetMapper();
        try {
            this.preparedStatement = this.connection.prepareStatement("SELECT * FROM orders");
            ResultSet resultSet = this.preparedStatement.executeQuery();
            allOrders = resultSetMapper.mapResultSetToObject(resultSet, Orders.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allOrders;
    }

    @Override
    public Orders getById(Long id) {
        Orders searchedOrder = null;
        try {
            this.preparedStatement = this.connection.prepareStatement
                    ("SELECT * FROM orders WHERE order_id = ?");
            this.preparedStatement.setLong(1, id);
            this.resultSet = preparedStatement.executeQuery();

            while (this.resultSet.next()) {
                searchedOrder = Orders.builder()
                        .order_id(resultSet.getLong("order_id"))
                        .customer_id(resultSet.getLong("customer_id"))
                        .is_order_completed(resultSet.getBoolean("is_order_completed"))
                        .is_order_payed(resultSet.getBoolean("is_order_payed"))
                        .date_of_order(resultSet.getDate("date_of_order"))
                        .date_order_completed(resultSet.getDate("date_of_order"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.printf(String.format("Order with id %d is:%n", id));
        System.out.println(searchedOrder);
        return searchedOrder;
    }

    @Override
    public List<Orders> getByIds(List<Long> ids) {
        ResultSetMapper resultSetMapper = new ResultSetMapper();
        try {
            this.preparedStatement = this.connection.prepareStatement
                    (String.format("SELECT * FROM orders WHERE order_id IN (%s)",
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
        List<Orders> searchedOrders = resultSetMapper.mapResultSetToObject(this.resultSet, Orders.class);
        System.out.println("Searched orders are:");
        searchedOrders
                .stream()
                .forEach(System.out::println);
        return searchedOrders;
    }

    @Override
    public int getAllRecordsCount() {
        int recordsCount = 0;
        try {
            this.preparedStatement = this.connection.prepareStatement("SELECT COUNT(order_id) FROM orders");
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

    @Override
    public Orders getRandomId() {
        Orders randomOrder = null;
        try {
            this.preparedStatement = this.connection.prepareStatement
                    ("SELECT * FROM order ORDER BY RANDOM() LIMIT 1");
            this.resultSet = preparedStatement.executeQuery();

            while (this.resultSet.next()) {
                randomOrder = Orders.builder()
                        .order_id(resultSet.getLong("order_id"))
                        .customer_id(resultSet.getLong("customer_id"))
                        .is_order_completed(resultSet.getBoolean("is_order_completed"))
                        .is_order_payed(resultSet.getBoolean("is_order_payed"))
                        .date_of_order(resultSet.getDate("date_of_order"))
                        .date_order_completed(resultSet.getDate("date_of_order"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Random order is:");
        System.out.println(randomOrder);
        return randomOrder;
    }

    @Override
    public List<Orders> getRandomIds(int randomCount) {
        ResultSetMapper resultSetMapper = new ResultSetMapper();
        List<Orders> randomOrders = null;
        try {
            this.preparedStatement = this.connection.prepareStatement
                    ("SELECT * FROM (SELECT DISTINCT * FROM orders) as AL ORDER BY RANDOM() LIMIT ?");
            this.preparedStatement.setInt(1, randomCount);
            this.resultSet = preparedStatement.executeQuery();
            randomOrders = resultSetMapper.mapResultSetToObject(this.resultSet, Orders.class);
        } catch (SQLException e) {
            e.getMessage();
        }
        System.out.printf(String.format("Random %d orders are:%n", randomCount));
        randomOrders
                .stream()
                .forEach(System.out::println);
        return randomOrders;
    }

    public List<Orders> getOrdersByCustomerId(Long customerId) {
        List<Orders> ordersByCustomerId = new ArrayList<>();

        List<Orders> orders = new ArrayList<>(getAllRecords());

        for (Orders order : orders) {
            Long customer_id = order.getCustomer_id();
            if (customer_id.longValue() == customerId.longValue()) {
                ordersByCustomerId.add(order);
            }
        }
        return ordersByCustomerId;
    }
}
