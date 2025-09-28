package org.example.CandyCo.repository;

import org.example.CandyCo.customer.Customer;
import org.example.CandyCo.customer.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void registerDatabaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testSaveAndFindCustomer() {
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setPhoneNumber("1234567890");
        customer.setEmail("john.doe@example.com");

        Customer savedCustomer = customerRepository.save(customer);
        Customer foundCustomer = customerRepository.findById(savedCustomer.getId()).orElse(null);

        assertNotNull(foundCustomer, "Customer should be found");
        assertEquals("John Doe", foundCustomer.getName(), "Customer name should match");
    }

    @Test
    void testFindAllCustomers() {
        Customer customer1 = new Customer();
        customer1.setName("Alice");
        customer1.setPhoneNumber("1111111111");
        customer1.setEmail("alice@example.com");
        customerRepository.save(customer1);

        Customer customer2 = new Customer();
        customer2.setName("Bob");
        customer2.setPhoneNumber("2222222222");
        customer2.setEmail("bob@example.com");
        customerRepository.save(customer2);

        List<Customer> customers = customerRepository.findAll();
        assertEquals(2, customers.size(), "There should be 2 customers in the database");
    }
}
