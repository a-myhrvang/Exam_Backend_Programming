package org.example.CandyCo.integration;

import org.example.CandyCo.customer.Customer;
import org.example.CandyCo.customer.CustomerRepository;
import org.example.CandyCo.order.Order;
import org.example.CandyCo.order.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class OrderIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setup() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void testSaveAndRetrieveOrder() {
        Order order = new Order();
        order.setShippingCharge(10.0);
        Order savedOrder = orderRepository.save(order);

        Order retrievedOrder = orderRepository.findById(savedOrder.getId()).orElse(null);
        assertNotNull(retrievedOrder);
        assertEquals(10.0, retrievedOrder.getShippingCharge());
    }

    @Test
    void testFindAllOrders() {
        Order order1 = new Order();
        order1.setShippingCharge(5.0);

        Order order2 = new Order();
        order2.setShippingCharge(15.0);

        orderRepository.save(order1);
        orderRepository.save(order2);

        Iterable<Order> orders = orderRepository.findAll();

        assertNotNull(orders);
        assertEquals(2, ((List<Order>) orders).size());
    }

    @Test
    void testUpdateOrder() {
        Order order = new Order();
        order.setShippingCharge(5.0);
        Order savedOrder = orderRepository.save(order);

        savedOrder.setShippingCharge(20.0);
        orderRepository.save(savedOrder);

        Order updatedOrder = orderRepository.findById(savedOrder.getId()).orElse(null);
        assertNotNull(updatedOrder);
        assertEquals(20.0, updatedOrder.getShippingCharge());
    }

    @Test
    void testDeleteOrder() {
        Order order = new Order();
        order.setShippingCharge(10.0);
        Order savedOrder = orderRepository.save(order);

        orderRepository.deleteById(savedOrder.getId());
        assertFalse(orderRepository.existsById(savedOrder.getId()));
    }

    @Test
    void testFindNonExistentOrder() {
        Order order = orderRepository.findById(999L).orElse(null);
        assertNull(order);
    }

    @Test
    void testOrderWithCustomer() {
        Customer customer = new Customer(null, "John Doe", "1234567890", "john.doe@example.com", null, null);
        Customer savedCustomer = customerRepository.save(customer);

        Order order = new Order();
        order.setShippingCharge(10.0);
        order.setCustomer(savedCustomer);
        Order savedOrder = orderRepository.save(order);

        Order retrievedOrder = orderRepository.findById(savedOrder.getId()).orElse(null);
        assertNotNull(retrievedOrder);
        assertNotNull(retrievedOrder.getCustomer());
        assertEquals("John Doe", retrievedOrder.getCustomer().getName());
    }
}
