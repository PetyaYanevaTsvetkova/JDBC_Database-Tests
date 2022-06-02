package daoService.DAOimpl;

import com.github.javafaker.Faker;
import entity.Address;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class AddressDAOTest {
    static AddressDAO addressDAO;

    static {
        try {
            addressDAO = new AddressDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    AddressDAOTest() throws SQLException, IOException {
    }

//    @BeforeAll
//    public static void setUp() {
//        addressDAO.deleteAll();
//        for (int i = 0; i < 3; i++) {
//            addressDAO.save(buildAddress());
//        }
//    }

    @Test
    public void notEmptyTableBeforeTestExecution() {
        int recordsInDB = addressDAO.getAllRecordsCount();
        Assertions.assertTrue(recordsInDB > 0, "The table is empty");
    }

    @Test
    public void failTheTestIfTableIsEmpty() {
        addressDAO.deleteAll();
        int recordsInDB = addressDAO.getAllRecordsCount();
        Assertions.assertThrows(NullPointerException.class, (Executable) addressDAO.getAllRecords());
    }

    @Test
    public void randomCustomersWithAddressWithAllMandatoryFields() {
        Address address = addressDAO.getAddressByCustomerId(getRandomCustomerId());

        Assertions.assertNotNull(address.getAddress_id());
        Assertions.assertNotNull(address.getCustomer_id());
        Assertions.assertNotNull(address.getCity());
        Assertions.assertNotNull(address.getCountry());
    }


    @Test
    public void cannotCreateAndSaveCustomerWithoutMandatoryFields() {
        try {
            addressDAO.save(createAddressWithoutMandatoryFields());
            Assertions.assertTrue(false, "Records with not filled mandatory field are created.");
        } catch (Exception e) {
            Assertions.assertTrue(e.getClass().equals(NullPointerException.class));
        }
    }

    private Address createAddressWithoutMandatoryFields() {
        Faker faker = new Faker();
        Address address = Address.builder()
                .address_id(null)
                .customer_id(null)
                .address(faker.address().fullAddress())
                .city(null)
                .province("UK")
                .state_uk(null)
                .postal_code(5498)
                .country(null)
                .build();
        return address;
    }

    private static Address buildAddress() {
        Faker faker = new Faker();
        Address address = Address.builder()
                .address_id(faker.number().numberBetween(1L, 5L))
                .customer_id(faker.number().numberBetween(1L, 5L))
                .address(faker.address().fullAddress())
                .city(faker.address().city())
                .province("UK")
                .state_uk(null)
                .postal_code(5498)
                .country(faker.address().country())
                .build();
        return address;
    }

    private Long getRandomCustomerId() {
        Random random = new Random();
        List<Long> customerIds = new ArrayList<>();
        for (Address address : addressDAO.getAllRecords()) {
            Long customer_id = address.getCustomer_id();
            customerIds.add(customer_id);
        }
        return customerIds.get(random.nextInt(customerIds.size()));
    }
}