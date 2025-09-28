package org.example.CandyCo.customer;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.example.CandyCo.error.CustomerNotFoundException;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    void getAllCustomers_shouldReturnListOfCustomers() throws Exception {
        Customer customer = new Customer(1L, "John Doe", "123456789", "john.doe@example.com", null, null);

        when(customerService.getAllCustomers()).thenReturn(List.of(customer));

        mockMvc.perform(get("/customers/api")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"));

        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    void getCustomerById_shouldReturnCustomer_whenCustomerExists() throws Exception {
        Customer customer = new Customer(1L, "John Doe", "123456789", "john.doe@example.com", null, null);

        when(customerService.getCustomerById(1L)).thenReturn(customer);

        mockMvc.perform(get("/customers/api/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(customerService, times(1)).getCustomerById(1L);
    }

    @Test
    void getCustomerById_shouldReturnNotFound_whenCustomerDoesNotExist() throws Exception {
        when(customerService.getCustomerById(1L)).thenThrow(new CustomerNotFoundException(1L));

        mockMvc.perform(get("/customers/api/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.reason").value("Customer not found for ID: 1"));

        verify(customerService, times(1)).getCustomerById(1L);
    }

    @Test
    void saveCustomer_shouldCreateCustomer() throws Exception {
        Customer customer = new Customer(null, "John Doe", "123456789", "john.doe@example.com", null, null);

        when(customerService.saveCustomer(any(Customer.class))).thenReturn(new Customer(1L, "John Doe", "123456789", "john.doe@example.com", null, null));

        mockMvc.perform(post("/customers/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(customerService, times(1)).saveCustomer(any(Customer.class));
    }

    @Test
    void deleteCustomer_shouldRemoveCustomer() throws Exception {
        doNothing().when(customerService).deleteCustomer(1L);

        mockMvc.perform(delete("/customers/api/1"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).deleteCustomer(1L);
    }

    @Test
    void deleteMultipleCustomers_shouldRemoveCustomers() throws Exception {
        doNothing().when(customerService).deleteMultipleCustomers(List.of(1L, 2L));

        mockMvc.perform(delete("/customers/api/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[1, 2]"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).deleteMultipleCustomers(List.of(1L, 2L));
    }
}
