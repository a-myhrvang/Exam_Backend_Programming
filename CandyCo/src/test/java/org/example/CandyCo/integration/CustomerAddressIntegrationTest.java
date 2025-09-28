package org.example.CandyCo.integration;

import org.example.CandyCo.customer.Customer;
import org.example.CandyCo.customer.CustomerRepository;
import org.example.CandyCo.customerAddress.CustomerAddress;
import org.example.CandyCo.customerAddress.CustomerAddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class CustomerAddressIntegrationTest {

    @BeforeEach
    void setup() {
        customerAddressRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private CustomerAddressRepository customerAddressRepository;

    @Autowired
    private CustomerRepository customerRepository;


    @Test
    void testSaveAndRetrieveCustomerAddress() {
        String uniqueEmail = "test" + System.currentTimeMillis() + "@example.com";
        Customer customer = new Customer(null, "John Doe", "1234567890", uniqueEmail, null, null);
        Customer savedCustomer = customerRepository.save(customer);

        CustomerAddress address = new CustomerAddress(null, "123 Street", "City", "State", "12345", savedCustomer);
        CustomerAddress savedAddress = customerAddressRepository.save(address);

        CustomerAddress retrievedAddress = customerAddressRepository.findById(savedAddress.getId()).orElse(null);

        assertNotNull(retrievedAddress);
        assertEquals("123 Street", retrievedAddress.getStreet());
        assertNotNull(retrievedAddress.getCustomer());
        assertEquals("John Doe", retrievedAddress.getCustomer().getName());
    }

    @Test
    void testDeleteCustomerAddress() {
        Customer customer = new Customer(null, "Jane Smith", "9876543210", "jane.smith@example.com", null, null);
        Customer savedCustomer = customerRepository.save(customer);

        CustomerAddress address = new CustomerAddress(null, "456 Avenue", "Metropolis", "State", "54321", savedCustomer);
        CustomerAddress savedAddress = customerAddressRepository.save(address);

        customerAddressRepository.deleteById(savedAddress.getId());
        assertFalse(customerAddressRepository.existsById(savedAddress.getId()));
    }

    @Test
    void testUpdateCustomerAddress() {
        Customer customer = new Customer(null, "Jane Doe", "1234567890", "jane.doe@example.com", null, null);
        Customer savedCustomer = customerRepository.save(customer);

        CustomerAddress address = new CustomerAddress(null, "789 Boulevard", "Old City", "Old State", "99999", savedCustomer);
        CustomerAddress savedAddress = customerAddressRepository.save(address);

        savedAddress.setStreet("New Street");
        customerAddressRepository.save(savedAddress);

        CustomerAddress updatedAddress = customerAddressRepository.findById(savedAddress.getId()).orElse(null);

        assertNotNull(updatedAddress);
        assertEquals("New Street", updatedAddress.getStreet());
    }

    @Test
    void testFindAllCustomerAddresses() {
        Customer customer = new Customer(null, "John Smith", "1234567890", "john.smith@example.com", null, null);
        Customer savedCustomer = customerRepository.save(customer);

        CustomerAddress address1 = new CustomerAddress(null, "Street 1", "City 1", "State 1", "11111", savedCustomer);
        CustomerAddress address2 = new CustomerAddress(null, "Street 2", "City 2", "State 2", "22222", savedCustomer);

        customerAddressRepository.save(address1);
        customerAddressRepository.save(address2);

        Iterable<CustomerAddress> addresses = customerAddressRepository.findAll();

        assertNotNull(addresses);
        assertEquals(2, ((List<CustomerAddress>) addresses).size());
    }

    @Test
    void testDeleteMultipleCustomerAddresses() {
        Customer customer = new Customer(null, "Jane Doe", "1234567890", "jane.doe@example.com", null, null);
        Customer savedCustomer = customerRepository.save(customer);

        CustomerAddress address1 = new CustomerAddress(null, "123 Street", "City", "State", "12345", savedCustomer);
        CustomerAddress address2 = new CustomerAddress(null, "456 Avenue", "Metropolis", "State", "54321", savedCustomer);

        customerAddressRepository.save(address1);
        customerAddressRepository.save(address2);

        customerAddressRepository.deleteAll();

        assertEquals(0, customerAddressRepository.count());
    }

    @Test
    void testFindNonExistentCustomerAddress() {
        CustomerAddress address = customerAddressRepository.findById(999L).orElse(null);
        assertNull(address);
    }

    @Test
    void testCustomerAddressBelongsToCorrectCustomer() {
        Customer customer1 = new Customer(null, "Customer One", "1234567890", "one@example.com", null, null);
        Customer customer2 = new Customer(null, "Customer Two", "9876543210", "two@example.com", null, null);

        Customer savedCustomer1 = customerRepository.save(customer1);
        Customer savedCustomer2 = customerRepository.save(customer2);

        CustomerAddress address1 = new CustomerAddress(null, "Street 1", "City 1", "State 1", "11111", savedCustomer1);
        CustomerAddress address2 = new CustomerAddress(null, "Street 2", "City 2", "State 2", "22222", savedCustomer2);

        customerAddressRepository.save(address1);
        customerAddressRepository.save(address2);

        CustomerAddress retrievedAddress1 = customerAddressRepository.findById(address1.getId()).orElse(null);
        CustomerAddress retrievedAddress2 = customerAddressRepository.findById(address2.getId()).orElse(null);

        assertNotNull(retrievedAddress1);
        assertNotNull(retrievedAddress2);
        assertEquals("Customer One", retrievedAddress1.getCustomer().getName());
        assertEquals("Customer Two", retrievedAddress2.getCustomer().getName());
    }
}
