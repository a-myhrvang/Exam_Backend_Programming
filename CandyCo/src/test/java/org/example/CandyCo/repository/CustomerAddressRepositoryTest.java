package org.example.CandyCo.repository;

import org.example.CandyCo.customer.Customer;
import org.example.CandyCo.customerAddress.CustomerAddress;
import org.example.CandyCo.customerAddress.CustomerAddressRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerAddressRepositoryTest {

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
    private CustomerAddressRepository customerAddressRepository;

    @Test
    void testSaveAndFindCustomerAddress() {

        Customer customer = new Customer();
        customer.setName("Jane Doe");

        CustomerAddress address = new CustomerAddress();
        address.setStreet("123 Main St");
        address.setCity("Metropolis");
        address.setState("NY");
        address.setZipCode("10001");
        address.setCustomer(customer);

        CustomerAddress savedAddress = customerAddressRepository.save(address);

        CustomerAddress foundAddress = customerAddressRepository.findById(savedAddress.getId()).orElse(null);

        assertNotNull(foundAddress, "Adresse skal finnes");
        assertEquals("123 Main St", foundAddress.getStreet(), "Gatenavn skal stemme");
    }
}
