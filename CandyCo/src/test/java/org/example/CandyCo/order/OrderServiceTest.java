package org.example.CandyCo.order;

import org.example.CandyCo.product.Product;
import org.example.CandyCo.customer.Customer;
import org.example.CandyCo.customer.CustomerRepository;
import org.example.CandyCo.customerAddress.CustomerAddress;
import org.example.CandyCo.customerAddress.CustomerAddressRepository;
import org.example.CandyCo.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerAddressRepository customerAddressRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private Customer customer;
    private CustomerAddress address;
    private Product product;

    public OrderServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setup() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");

        address = new CustomerAddress();
        address.setId(1L);
        address.setStreet("123 Main St");
        address.setCity("Metropolis");
        address.setState("NY");
        address.setZipCode("12345");
        address.setCustomer(customer);

        product = new Product();
        product.setId(1L);
        product.setName("Chocolate");
        product.setPrice(5.99);
        product.setQuantityOnHand(100);
    }

    @Test
    void testSaveOrderWithRelations() {
        Order order = new Order();
        order.setCustomer(customer);
        order.setShippingAddress(address);
        order.setProducts(List.of(product));
        order.setTotalPrice(10.99);
        order.setShippingCharge(5.0);

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order savedOrder = orderService.saveOrder(order);

        assertNotNull(savedOrder);
        assertEquals(1, savedOrder.getProducts().size());
        assertEquals(10.99, savedOrder.getTotalPrice());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testDeleteOrder_OrderExists() {
        when(orderRepository.existsById(1L)).thenReturn(true);

        orderService.deleteOrder(1L);

        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteOrder_OrderNotFound() {
        when(orderRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> orderService.deleteOrder(1L));

        assertEquals("Order not found for ID: " + 1L, exception.getMessage());
    }

    @Test
    void testGetAllOrders() {
        Order order1 = new Order();
        order1.setId(1L);
        order1.setTotalPrice(20.0);

        Order order2 = new Order();
        order2.setId(2L);
        order2.setTotalPrice(40.0);

        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));

        List<Order> orders = orderService.getAllOrders();

        assertNotNull(orders);
        assertEquals(2, orders.size());
        assertEquals(20.0, orders.get(0).getTotalPrice());
    }

    @Test
    void testUpdateOrder_OrderExists() {
        Order existingOrder = new Order();
        existingOrder.setId(1L);
        existingOrder.setProducts(List.of(new Product()));
        existingOrder.setTotalPrice(100.0);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));


        Order updatedOrder = new Order();
        updatedOrder.setId(1L);
        updatedOrder.setProducts(List.of(new Product()));
        updatedOrder.setTotalPrice(150.0);

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            savedOrder.setId(1L);
            return savedOrder;
        });

        Order result = orderService.saveOrder(updatedOrder);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(150.0, result.getTotalPrice());
        verify(orderRepository, times(1)).save(updatedOrder);
    }

    @Test
    void testSaveOrder_NullOrder() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.saveOrder(null);
        });

        assertEquals("Order cannot be null", exception.getMessage());
    }

    @Test
    void testGetOrderById_NullId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.getOrderById(null);
        });

        assertEquals("ID cannot be null", exception.getMessage());
    }

    @Test
    void testGetAllOrders_EmptyDatabase() {
        when(orderRepository.findAll()).thenReturn(List.of());

        List<Order> orders = orderService.getAllOrders();

        assertNotNull(orders);
        assertTrue(orders.isEmpty());
    }

    @Test
    void testSaveOrder_EmptyProductList() {
        Order order = new Order();
        order.setProducts(new ArrayList<>());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.saveOrder(order);
        });

        assertEquals("Order must have at least one product", exception.getMessage());
    }
}
