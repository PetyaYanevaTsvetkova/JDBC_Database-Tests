package daoService.DAOimpl;

import com.github.javafaker.Faker;
import entity.Customer;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrdersDAOTest {
    static OrdersDAO ordersDAO;
    static CustomerDAO customerDAO;
    static ProductDAO productDAO;

    static {
        try {
            ordersDAO = new OrdersDAO();
            customerDAO = new CustomerDAO();
            productDAO = new ProductDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    OrdersDAOTest() throws SQLException, IOException {
    }

//    @BeforeAll
//    public static void setUp() {
//        ordersDAO.deleteAll();
//        for (int i = 0; i < 3; i++) {
//            ordersDAO.save(buildOrder());
//        }
//    }

    @Test
    public void notEmptyTableBeforeTestExecution() {
        int recordsInDB = ordersDAO.getAllRecordsCount();
        System.out.println(recordsInDB);
        Assertions.assertTrue(recordsInDB > 0, "The table is empty");
    }

    @Test
    public void failTheTestIfTableIsEmpty() {
        ordersDAO.deleteAll();
        int recordsInDB = ordersDAO.getAllRecordsCount();
        Assertions.assertThrows(NullPointerException.class, (Executable) ordersDAO.getAllRecords());
    }

    @Test
    public void ordersWithAllMandatoryFields() {
        List<Customer> randomCustomers = customerDAO.getRandomIds(2);
        List<Long> randomCustomerIds = new ArrayList<>();
        for (Customer randomCustomer : randomCustomers) {
            randomCustomerIds.add(randomCustomer.getCustomer_id());
        }

        for (Long randomCustomerId : randomCustomerIds) {
            List<Orders> ordersByCustomerId = ordersDAO.getOrdersByCustomerId(randomCustomerId);

            if (ordersByCustomerId == null) {
                System.out.println(String.format
                        ("The customer with id %d don't have any orders.", randomCustomerId));
            } else {
                for (Orders order : ordersByCustomerId) {
                    Assertions.assertNotNull(order.getOrder_id());
                    Assertions.assertNotNull(order.getCustomer_id());
                    Assertions.assertNotNull(order.is_order_completed());
                    Assertions.assertNotNull(order.is_order_payed());
                    Assertions.assertNotNull(order.getDate_of_order());
                }
            }
        }
    }

    @Test
    public void randomOrdersHavingCustomer() {
        for (Orders randomOrder : ordersDAO.getRandomIds(2)) {
            Assertions.assertNotNull(randomOrder.getCustomer_id());
        }
    }

    @Test
    public void randomOrdersWithExistingProductsFromProductTable() {
        List<Orders> randomOrders = ordersDAO.getRandomIds(2);
        for (Orders randomOrder : randomOrders) {
            Long order_id = randomOrder.getOrder_id();
            Product product = productDAO.getProductByOrderId(order_id);
            Assertions.assertNotNull(product, "There is Order without product");
        }
    }

    @Test
    public void createAndSaveOrderSuccessfully() {
        Orders orderForSave = buildOrder();
        ordersDAO.save(orderForSave);

        Orders orderFromDB = ordersDAO.getById(orderForSave.getOrder_id());
        assertEquals(orderForSave.getOrder_id(), orderFromDB.getOrder_id());
        assertEquals(orderForSave.getCustomer_id(), orderFromDB.getCustomer_id());
        assertEquals(orderForSave.is_order_completed(), orderFromDB.is_order_completed());
        assertEquals(orderForSave.is_order_payed(), orderFromDB.is_order_payed());
        assertEquals(orderForSave.getDate_of_order(), orderFromDB.getDate_of_order());
        assertEquals(orderForSave.getDate_order_completed(), orderFromDB.getDate_order_completed());
    }


    @Test
    public void cannotCreateAndSaveOrderWithoutMandatoryFields() {
        try {
            System.out.println(createAndSaveOrderWithoutMandatoryFields());
            ordersDAO.save(createAndSaveOrderWithoutMandatoryFields());
            Assertions.assertTrue(false, "Records with not filled mandatory field are created.");
        } catch (Exception e) {
            Assertions.assertTrue(e.getClass().equals(NullPointerException.class));
        }
    }

    private Orders createAndSaveOrderWithoutMandatoryFields() {
        Orders order = Orders.builder()
                .order_id(null)
                .customer_id(null)
                .is_order_completed(false)
                .is_order_payed(false)
                .date_of_order(null)
                .date_order_completed(null)
                .build();
        return order;
    }

    private static Orders buildOrder() {
        Faker faker = new Faker();
        Orders order = Orders.builder()
                .order_id(faker.number().numberBetween(6L, 10L))
                .customer_id(faker.number().numberBetween(1L, 5L))
                .is_order_completed(faker.random().nextBoolean())
                .is_order_payed(faker.random().nextBoolean())
                .date_of_order(Date.valueOf(LocalDate.now()))
                .date_order_completed(Date.valueOf(LocalDate.now()))
                .build();
        return order;
    }
}