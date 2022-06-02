package daoService.DAOimpl;

import daoService.DAO;
import dbconnection.DatabaseConnection;
import entity.Orders;
import entity.Product;
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
public class ProductDAO implements DAO<Product> {
    private final Connection connection;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;

    public ProductDAO() throws SQLException, IOException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Saves the record to the database.
     *
     * @param product - Object of type Product.
     */
    @Override
    public void save(Product product) {
        try {
            this.preparedStatement = this.connection.prepareStatement("INSERT INTO product \n" +
                    "(product_id," +
                    "product_name," +
                    "available_quantity, " +
                    "product_type," +
                    "price_without_VAT, " +
                    "price_with_VAT, " +
                    "is_product_in_stock, " +
                    "warehouse) \n" +
                    "VALUES \n" +
                    "(?, ?, ?, ?, ?, ?, ?, ?)");
            this.preparedStatement.setLong(1, product.getProduct_id());
            this.preparedStatement.setString(2, product.getProduct_name());
            this.preparedStatement.setInt(3, product.getAvailable_quantity());
            this.preparedStatement.setString(4, product.getProduct_type());
            this.preparedStatement.setDouble(5, product.getPrice_without_VAT());
            this.preparedStatement.setDouble(6, product.getPrice_with_VAT());
            this.preparedStatement.setBoolean(7, product.is_product_in_stock());
            this.preparedStatement.setString(8, product.getWarehouse());

            this.preparedStatement.executeUpdate();
            System.out.println("Product saved successfully.");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Deletes the record from the database.
     *
     * @param id - id of the product record
     */
    @Override
    public void deleteById(Long id) {
        try {
            this.preparedStatement = this.connection.prepareStatement("DELETE FROM product WHERE order_id = ?");
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
            this.preparedStatement = this.connection.prepareStatement("DELETE FROM product");
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
    public List<Product> getAllRecords() {
        List<Product> allProducts = new ArrayList<>();
        ResultSetMapper resultSetMapper = new ResultSetMapper();
        try {
            this.preparedStatement = this.connection.prepareStatement("SELECT * FROM product");
            ResultSet resultSet = this.preparedStatement.executeQuery();
            allProducts = resultSetMapper.mapResultSetToObject(resultSet, Product.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allProducts;
    }

    /**
     * Takes a single record from the table by id.
     * @param id - id of the product record
     * @return - record from the Database with the id, given in the method parameter
     */
    @Override
    public Product getById(Long id) {
        Product searchedProduct = null;
        try {
            this.preparedStatement = this.connection.prepareStatement
                    ("SELECT * FROM product WHERE product_id = ?");
            this.preparedStatement.setLong(1, id);
            this.resultSet = preparedStatement.executeQuery();

            while (this.resultSet.next()) {
                searchedProduct = Product.builder()
                        .product_id(resultSet.getLong("product_id"))
                        .product_name(resultSet.getString("product_name"))
                        .available_quantity(resultSet.getInt("available_quantity"))
                        .product_type(resultSet.getString("product_type"))
                        .price_without_VAT(resultSet.getDouble("price_without_VAT"))
                        .price_with_VAT(resultSet.getDouble("price_with_VAT"))
                        .is_product_in_stock(resultSet.getBoolean("is_product_in_stock"))
                        .warehouse(resultSet.getString("warehouse"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.printf(String.format("Product with id %d is:%n", id));
        System.out.println(searchedProduct);
        return searchedProduct;
    }

    /**
     * Takes a list of records by list of ids.
     *
     * @param ids - List of ids of the product records.
     * @return - ArrayList containing list of records with the ids, given in the method parameter.
     */
    @Override
    public List<Product> getByIds(List<Long> ids) {
        ResultSetMapper resultSetMapper = new ResultSetMapper();
        try {
            this.preparedStatement = this.connection.prepareStatement
                    (String.format("SELECT * FROM product WHERE order_id IN (%s)",
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
        List<Product> searchedProducts = resultSetMapper.mapResultSetToObject(this.resultSet, Product.class);
        System.out.println("Searched products are:");
        searchedProducts
                .stream()
                .forEach(System.out::println);
        return searchedProducts;
    }

    /**
     * Takes the count of all records in the table.
     *
     * @return - the count of all records in the table.
     */
    @Override
    public int getAllRecordsCount() {
        int recordsCount = 0;
        try {
            this.preparedStatement = this.connection.prepareStatement("SELECT COUNT(product_id) FROM product");
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
    public Product getRandomId() {
        Product randomProduct = null;
        try {
            this.preparedStatement = this.connection.prepareStatement
                    ("SELECT * FROM product ORDER BY RANDOM() LIMIT 1");
            this.resultSet = preparedStatement.executeQuery();

            while (this.resultSet.next()) {
                randomProduct = Product.builder()
                        .product_id(resultSet.getLong("product_id"))
                        .product_name(resultSet.getString("product_name"))
                        .available_quantity(resultSet.getInt("available_quantity"))
                        .product_type(resultSet.getString("product_type"))
                        .price_without_VAT(resultSet.getDouble("price_without_VAT"))
                        .price_with_VAT(resultSet.getDouble("price_with_VAT"))
                        .is_product_in_stock(resultSet.getBoolean("is_product_in_stock"))
                        .warehouse(resultSet.getString("warehouse"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Random product is:");
        System.out.println(randomProduct);
        return randomProduct;
    }

    /**
     * Takes a random list of records.
     * @param randomCount - the count of random records.
     * @return - returns list of random records from the Database.
     */
    @Override
    public List<Product> getRandomIds(int randomCount) {
        ResultSetMapper resultSetMapper = new ResultSetMapper();
        List<Product> randomProducts = null;
        try {
            this.preparedStatement = this.connection.prepareStatement
                    ("SELECT * FROM (SELECT DISTINCT * FROM product) as AL ORDER BY RANDOM() LIMIT ?");
            this.preparedStatement.setInt(1, randomCount);
            this.resultSet = preparedStatement.executeQuery();
            randomProducts = resultSetMapper.mapResultSetToObject(this.resultSet, Product.class);
        } catch (SQLException e) {
            e.getMessage();
        }
        System.out.printf(String.format("Random %d orders are:%n", randomCount));
        randomProducts
                .stream()
                .forEach(System.out::println);
        return randomProducts;
    }

    /**
     * Takes a records by given Order id.
     * @param orderId - id of the order record
     * @return - returns object of type Product
     */
    public Product getProductByOrderId(Long orderId) {
        Product productByOrderId = new Product();

        try {
            this.preparedStatement = this.connection.prepareStatement
                    ("SELECT product.* FROM orders_products\n" +
                            "INNER JOIN\n" +
                            "product \n" +
                            "ON \n" +
                            "orders_products.product_id = product.product_id\n" +
                            "WHERE orders_products.order_id = ?");
            this.preparedStatement.setLong(1, orderId);
            this.resultSet = preparedStatement.executeQuery();
            while (this.resultSet.next()) {
                productByOrderId = Product.builder()
                        .product_id(resultSet.getLong("product_id"))
                        .product_name(resultSet.getString("product_name"))
                        .available_quantity(resultSet.getInt("available_quantity"))
                        .product_type(resultSet.getString("product_type"))
                        .price_without_VAT(resultSet.getDouble("price_without_VAT"))
                        .price_with_VAT(resultSet.getDouble("price_with_VAT"))
                        .is_product_in_stock(resultSet.getBoolean("is_product_in_stock"))
                        .warehouse(resultSet.getString("warehouse"))
                        .build();
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return productByOrderId;
    }
}
