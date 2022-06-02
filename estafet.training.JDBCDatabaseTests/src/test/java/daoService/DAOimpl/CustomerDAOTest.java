package daoService.DAOimpl;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import entity.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDAOTest {
    static CustomerDAO customerDAO;

    static {
        try {
            customerDAO = new CustomerDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    CustomerDAOTest() throws SQLException, IOException {
    }

//    @BeforeAll
//    public static void setUp() {
//        customerDAO.deleteAll();
//        for (int i = 0; i < 3; i++) {
//            customerDAO.save(buildCustomer());
//        }
//    }

    @Test
    public void notEmptyTableBeforeTestExecution() {
        int recordsInDB = customerDAO.getAllRecordsCount();
        System.out.println(recordsInDB);
        Assertions.assertTrue(recordsInDB > 0, "The table is empty");
    }

    @Test
    public void failTheTestIfTableIsEmpty() {
        customerDAO.deleteAll();
        int recordsInDB = customerDAO.getAllRecordsCount();
        Assertions.assertThrows(NullPointerException.class, (Executable) customerDAO.getAllRecords());
    }


    @Test
    public void noCustomersWithoutAddress() {
        customerDAO.getAllRecords()
                .stream()
                .forEach(customer -> assertFalse(customer.getAddress().isEmpty(), "Customer with id "
                        + customer.getCustomer_id() + " is without address."));
    }

    @Test
    public void testNoEqualsPhoneNumbers() {
        List<Customer> allRecordsDB = customerDAO.getAllRecords();
        List<String> phones = new ArrayList<>();
        for (Customer customer : allRecordsDB) {
            phones.add(customer.getPhone());
        }
        List<String> listStrings = phones
                .stream()
                .filter(p -> Collections.frequency(phones, p) > 1)
                .collect(Collectors.toList());
        Assertions.assertTrue(listStrings.isEmpty(), "There are customers with the same phone numbers");
    }

    @Test
    public void testEqualsEmailAddress() {
        List<Customer> allRecordsDB = customerDAO.getAllRecords();
        List<String> mails = new ArrayList<>();
        for (Customer customer : allRecordsDB) {
            mails.add(customer.getEmail());
        }
        List<String> listStrings = mails
                .stream()
                .filter(p -> Collections.frequency(mails, p) > 1)
                .collect(Collectors.toList());
        Assertions.assertTrue(listStrings.isEmpty(), "There are customers with the same email address");
    }

    @Test
    public void testSaveSuccessfully() {
        Customer customerForSave = buildCustomer();
        customerDAO.save(customerForSave);

        Customer customerFromDB = customerDAO.getById(customerForSave.getCustomer_id());
        assertTrue(customerForSave.getCustomer_id().equals(customerFromDB.getCustomer_id()));
        assertEquals(customerForSave.getName(), customerFromDB.getName());
        assertEquals(customerForSave.getEmail(), customerFromDB.getEmail());
        assertEquals(customerForSave.getPhone().trim(), customerFromDB.getPhone().trim());
        assertEquals(customerForSave.getAge(), customerFromDB.getAge());
        assertEquals(customerForSave.getAddress(), customerFromDB.getAddress());
        assertEquals(customerForSave.getPostal_code(), customerFromDB.getPostal_code());
        assertEquals(customerForSave.getCountry(), customerFromDB.getCountry());
        assertEquals(customerForSave.isConsent_status(), customerFromDB.isConsent_status());
        assertEquals(customerForSave.is_profile_active(), customerFromDB.is_profile_active());
        assertEquals(customerForSave.getDate_profile_created(), customerFromDB.getDate_profile_created());
        assertEquals(customerForSave.getDate_profile_deactivated(), customerFromDB.getDate_profile_deactivated());
        assertEquals(customerForSave.getReason_for_deactivation(), customerFromDB.getReason_for_deactivation());
        assertEquals(customerForSave.getNotes(), customerFromDB.getNotes());
    }

    @Test
    public void cannotCreateAndSaveCustomerWithoutMandatoryFields() {
        try {
            customerDAO.save(createCustomerWithoutMandatoryFields());
            Assertions.assertTrue(false, "Records with not filled mandatory field are created.");
        } catch (Exception e) {
            Assertions.assertTrue(e.getClass().equals(NullPointerException.class));
        }
    }

    private Customer createCustomerWithoutMandatoryFields() throws NullPointerException {
        Faker faker = new Faker();
        Customer customer = Customer.builder()
                .customer_id(null)
                .name(null)
                .email(null)
                .phone(null)
                .age(faker.random().nextInt(18, 100))
                .address(null)
                .city(faker.address().city())
                .postal_code(faker.number().randomDigit())
                .country(faker.address().country())
                .consent_status(false)
                .is_profile_active(false)
                .date_profile_created(null)
                .date_profile_deactivated(Date.valueOf(LocalDate.now()))
                .reason_for_deactivation("error")
                .notes("Lorem Ipsum is simply dummy text of " +
                        "the printing and typesetting industry. Lorem Ipsum has" +
                        " been the industry's standard dummy text ever since the " +
                        "1500s, when an unknown printer took a galley of type and " +
                        "scrambled it to make a type specimen book. It has survived " +
                        "not only five centuries, but also ")
                .build();
        return customer;
    }

    private static Customer buildCustomer() {
        Faker faker = new Faker();
        FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-GB"), new RandomService());

        Customer customer = Customer.builder()
                .customer_id(faker.number().numberBetween(5L, 10L))
                .name(faker.name().firstName())
                .email(fakeValuesService.bothify("????##@gmail.com"))
                .phone(faker.numerify("############"))
                .age(faker.random().nextInt(18, 100))
                .address(faker.address().fullAddress())
                .city(faker.address().city())
                .postal_code(2498)
                .country(faker.address().country())
                .consent_status(faker.random().nextBoolean())
                .is_profile_active(faker.random().nextBoolean())
                .date_profile_created(Date.valueOf(LocalDate.now()))
                .date_profile_deactivated(Date.valueOf(LocalDate.now()))
                .reason_for_deactivation("error")
                .notes("Lorem Ipsum is simply dummy text of " +
                        "the printing and typesetting industry. Lorem Ipsum has" +
                        " been the industry's standard dummy text ever since the " +
                        "1500s, when an unknown printer took a galley of type and " +
                        "scrambled it to make a type specimen book. It has survived " +
                        "not only five centuries, but also ")
                .build();
        return customer;
    }
}