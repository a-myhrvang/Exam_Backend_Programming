package org.example.CandyCo.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.CandyCo.product.Product;
import org.example.CandyCo.product.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ProductService productService;

    @MockBean
    private OrderRepository orderRepository;

    @Test
    void testGetAllOrdersAsJson() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setTotalPrice(100.0);
        order.setShipped(false);

        Mockito.when(orderService.getAllOrders()).thenReturn(Collections.singletonList(order));

        mockMvc.perform(get("/orders/api")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(order.getId()))
                .andExpect(jsonPath("$[0].totalPrice").value(order.getTotalPrice()))
                .andExpect(jsonPath("$[0].shipped").value(order.isShipped()));
    }

    @Test
    void testGetOrderById() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setTotalPrice(100.0);

        Mockito.when(orderService.getOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/orders/api/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId()))
                .andExpect(jsonPath("$.totalPrice").value(order.getTotalPrice()));
    }

    @Test
    void testCreateOrder() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setTotalPrice(100.0);

        Mockito.when(orderService.saveOrder(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/orders/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId()))
                .andExpect(jsonPath("$.totalPrice").value(order.getTotalPrice()));
    }

    @Test
    void testDeleteOrder() throws Exception {
        Mockito.doNothing().when(orderService).deleteOrder(1L);

        mockMvc.perform(delete("/orders/api/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(orderService, Mockito.times(1)).deleteOrder(1L);
    }

    @Test
    void testUpdateProductInOrder() throws Exception {
        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(50.0);

        Mockito.when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/orders/api/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedProduct.getId()))
                .andExpect(jsonPath("$.name").value(updatedProduct.getName()))
                .andExpect(jsonPath("$.price").value(updatedProduct.getPrice()));
    }
}
