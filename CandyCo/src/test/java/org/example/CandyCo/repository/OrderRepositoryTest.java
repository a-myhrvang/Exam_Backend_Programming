package org.example.CandyCo.repository;

import org.example.CandyCo.customer.Customer;
import org.example.CandyCo.customer.CustomerRepository;
import org.example.CandyCo.customerAddress.CustomerAddress;
import org.example.CandyCo.customerAddress.CustomerAddressRepository;
import org.example.CandyCo.order.Order;
import org.example.CandyCo.order.OrderRepository;
import org.example.CandyCo.product.Product;
import org.example.CandyCo.product.ProductRepository;
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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryTest {

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
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerAddressRepository customerAddressRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSaveAndFindOrder() {

        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setPhoneNumber("1234567890");
        customer.setEmail("john.doe@example.com");
        customer = customerRepository.save(customer);

        CustomerAddress address = new CustomerAddress();
        address.setStreet("123 Main St");
        address.setCity("Metropolis");
        address.setState("NY");
        address.setZipCode("10001");
        address.setCustomer(customer);
        address = customerAddressRepository.save(address);

        Product product = new Product();
        product.setName("Candy");
        product.setDescription("Delicious sweet treat");
        product.setPrice(5.99);
        product.setStatus("AVAILABLE");
        product.setQuantityOnHand(100);
        product = productRepository.save(product);

        Order order = new Order();
        order.setCustomer(customer);
        order.setShippingAddress(address);
        order.setProducts(Collections.singletonList(product));
        order.setTotalPrice(5.99);
        order.setShippingCharge(2.50);
        order.setShipped(true);

        Order savedOrder = orderRepository.save(order);
        Order foundOrder = orderRepository.findById(savedOrder.getId()).orElse(null);

        assertNotNull(foundOrder, "Order should not be null");
        assertEquals(5.99, foundOrder.getTotalPrice(), 0.01, "Total price should match");
    }

    @Test
    void testFindAllOrders() {

        Order order1 = new Order();
        order1.setTotalPrice(20.0);
        orderRepository.save(order1);

        Order order2 = new Order();
        order2.setTotalPrice(40.0);
        orderRepository.save(order2);

        List<Order> orders = orderRepository.findAll();
        assertEquals(2, orders.size(), "There should be 2 orders in the database");
    }
}
