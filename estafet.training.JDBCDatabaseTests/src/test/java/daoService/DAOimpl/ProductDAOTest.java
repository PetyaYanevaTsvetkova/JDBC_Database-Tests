package daoService.DAOimpl;

import com.github.javafaker.Faker;
import entity.Orders;
import entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductDAOTest {
    static OrdersDAO ordersDAO;
    static ProductDAO productDAO;

    static {
        try {
            ordersDAO = new OrdersDAO();
            productDAO = new ProductDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ProductDAOTest() throws SQLException, IOException {
    }

    @BeforeAll
    public static void setUp() {
        productDAO.deleteAll();
        for (int i = 0; i < 3; i++) {
            productDAO.save(productBuilder());
        }
    }

    @Test
    public void randomOrdersWithProductsAllMandatoryFieldsFilled() {
        List<Orders> randomOrders = ordersDAO.getRandomIds(2);

        for (Orders randomOrder : randomOrders) {
            Long order_id = randomOrder.getOrder_id();
            Product productByOrderId = productDAO.getProductByOrderId(order_id);
            Assertions.assertNotNull(productByOrderId.getProduct_id());
            Assertions.assertNotNull(productByOrderId.getProduct_name());
            Assertions.assertNotNull(productByOrderId.getAvailable_quantity());
            Assertions.assertNotNull(productByOrderId.getProduct_type());
            Assertions.assertNotNull(productByOrderId.getPrice_without_VAT());
            Assertions.assertNotNull(productByOrderId.getPrice_with_VAT());
            Assertions.assertNotNull(productByOrderId.is_product_in_stock());
            Assertions.assertNotNull(productByOrderId.getWarehouse());
        }
    }

    @Test
    public void createAndSaveProductSuccessfullyAndNotPresentInAnyOrderAsJustCreated() {
        Product productForSave = productBuilder();
        productDAO.save(productForSave);

        Product productFromDB = productDAO.getById(productForSave.getProduct_id());
        assertEquals(productForSave.getProduct_id(), productFromDB.getProduct_id());
        assertEquals(productForSave.getProduct_name(), productFromDB.getProduct_name());
        assertEquals(productForSave.getAvailable_quantity(), productFromDB.getAvailable_quantity());
        assertEquals(productForSave.getProduct_type(), productFromDB.getProduct_type());
        assertEquals(productForSave.getPrice_without_VAT(), productFromDB.getPrice_without_VAT());
        assertEquals(productForSave.getPrice_with_VAT(), productFromDB.getPrice_with_VAT());
        assertEquals(productForSave.is_product_in_stock(), productFromDB.is_product_in_stock());
        assertEquals(productForSave.getWarehouse(), productFromDB.getWarehouse());

        for (Orders order : ordersDAO.getAllRecords()) {
            Long order_id = order.getOrder_id();
            Product productByOrderId = productDAO.getProductByOrderId(order_id);
            Assertions.assertNotEquals(productByOrderId.getProduct_id(), productFromDB.getProduct_id(), "There is Order with product, just created");
        }
    }

    @Test
    public void cannotCreateAndSaveOrderWithoutMandatoryFields() {
        try {
            productDAO.save(createAndSaveWithoutMandatoryFields());
            Assertions.assertTrue(false, "Records with not filled mandatory field are created.");
        } catch (Exception e) {
            Assertions.assertTrue(e.getClass().equals(NullPointerException.class));
        }
    }

    @Test
    public void notEmptyTableBeforeTestExecution() {
        int recordsInDB = productDAO.getAllRecordsCount();
        System.out.println(recordsInDB);
        Assertions.assertTrue(recordsInDB > 0, "The table is empty");
    }

    @Test
    public void failTheTestIfTableIsEmpty() {
        ordersDAO.deleteAll();
        int recordsInDB = ordersDAO.getAllRecordsCount();
        Assertions.assertThrows(NullPointerException.class, (Executable) ordersDAO.getAllRecords());
    }

    private Product createAndSaveWithoutMandatoryFields() {
        Product product = Product.builder()
                .product_id(null)
                .product_name(null)
                .available_quantity(null)
                .product_type(null)
                .price_without_VAT(null)
                .price_with_VAT(null)
                .is_product_in_stock(false)
                .warehouse(null)
                .build();
        return product;
    }

    private static Product productBuilder() {
        Faker faker = new Faker();
        Product product = Product.builder()
                .product_id(faker.number().numberBetween(21L, 3000L))
                .product_name(faker.food().ingredient())
                .available_quantity(faker.number().randomDigit())
                .product_type("food")
                .price_without_VAT(1.2)
                .price_with_VAT(1.44)
                .is_product_in_stock(faker.random().nextBoolean())
                .warehouse("Ivailovgrad")
                .build();
        return product;
    }
}