package org.example.CandyCo.integration;


import org.example.CandyCo.customer.Customer;
import org.example.CandyCo.customer.CustomerRepository;
import org.example.CandyCo.product.ProductRepository;
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
public class CustomerIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    void setup() {
        productRepository.deleteAll();
    }

    @Test
    void testSaveAndRetrieveCustomer() {
        Customer customer = new Customer();
        customer.setName("Test User");
        customer.setPhoneNumber("1234567890");
        customer.setEmail("test@example.com");

        customerRepository.save(customer);

        Customer retrievedCustomer = customerRepository.findById(customer.getId()).orElse(null);

        assertNotNull(retrievedCustomer);
        assertEquals("Test User", retrievedCustomer.getName());
    }

    @Test
    void testUpdateCustomer() {
        Customer customer = new Customer();
        customer.setName("Old Name");
        customer.setPhoneNumber("1234567890");
        customer.setEmail("old@example.com");

        Customer savedCustomer = customerRepository.save(customer);

        // Oppdater informasjon
        savedCustomer.setName("New Name");
        savedCustomer.setEmail("new@example.com");
        customerRepository.save(savedCustomer);

        Customer updatedCustomer = customerRepository.findById(savedCustomer.getId()).orElse(null);

        assertNotNull(updatedCustomer);
        assertEquals("New Name", updatedCustomer.getName());
        assertEquals("new@example.com", updatedCustomer.getEmail());
    }

    @Test
    void testDeleteCustomer() {
        Customer customer = new Customer();
        customer.setName("To Be Deleted");
        customer.setPhoneNumber("1234567890");
        customer.setEmail("delete@example.com");

        Customer savedCustomer = customerRepository.save(customer);
        customerRepository.deleteById(savedCustomer.getId());

        assertFalse(customerRepository.existsById(savedCustomer.getId()));
    }

    @Test
    void testFindAllCustomers() {
        Customer customer1 = new Customer();
        customer1.setName("Customer One");
        customer1.setPhoneNumber("1234567890");
        customer1.setEmail("one@example.com");

        Customer customer2 = new Customer();
        customer2.setName("Customer Two");
        customer2.setPhoneNumber("0987654321");
        customer2.setEmail("two@example.com");

        customerRepository.save(customer1);
        customerRepository.save(customer2);

        Iterable<Customer> customers = customerRepository.findAll();
        assertNotNull(customers);
        assertTrue(((List<Customer>) customers).size() >= 2); // Eller test spesifikt på antall om databasen er ren
    }

    @Test
    void testFindNonExistentCustomer() {
        Customer customer = customerRepository.findById(999L).orElse(null);
        assertNull(customer);
    }
}
