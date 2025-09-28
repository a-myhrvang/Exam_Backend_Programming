package org.example.CandyCo.customerAddress;


import org.example.CandyCo.customer.Customer;
import org.example.CandyCo.customer.CustomerRepository;
import org.example.CandyCo.error.CustomerAddressNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerAddressController.class)
public class CustomerAddressControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerAddressService customerAddressService;

    @MockBean
    private CustomerRepository customerRepository;

    @Test
    void testGetAllCustomerAddresses() throws Exception {
        CustomerAddress address = new CustomerAddress(1L, "Street 1", "City 1", "State 1", "12345", null);
        when(customerAddressService.getAllCustomerAddresses()).thenReturn(List.of(address));

        mockMvc.perform(get("/customer-addresses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].street").value("Street 1"))
                .andExpect(jsonPath("$[0].city").value("City 1"));
    }

    @Test
    void testGetCustomerAddressById_Success() throws Exception {
        CustomerAddress address = new CustomerAddress(1L, "123 Street", "City", "State", "12345", null);

        when(customerAddressService.getCustomerAddressById(1L)).thenReturn(address);

        mockMvc.perform(get("/customer-addresses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.street").value("123 Street"))
                .andExpect(jsonPath("$.city").value("City"));
    }

    @Test
    void testGetCustomerAddressById_NotFound() throws Exception {
        when(customerAddressService.getCustomerAddressById(1L)).thenThrow(new CustomerAddressNotFoundException(1L));

        mockMvc.perform(get("/customer-addresses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.reason").value("CustomerAddress not found for ID: 1"));
    }

    @Test
    void testCreateCustomerAddress() throws Exception {
        Customer customer = new Customer(1L, "John Doe", "12345678", "john@example.com", null, null);
        CustomerAddress address = new CustomerAddress(null, "Street 1", "City 1", "State 1", "12345", customer);
        when(customerAddressService.saveCustomerAddress(any())).thenReturn(address);

        mockMvc.perform(post("/customer-addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"street\": \"Street 1\", \"city\": \"City 1\", \"state\": \"State 1\", \"zipCode\": \"12345\", \"customer\": {\"id\": 1}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street").value("Street 1"))
                .andExpect(jsonPath("$.customer.id").value(1));
    }

    @Test
    void testSaveCustomerAddress_Success() throws Exception {
        CustomerAddress newAddress = new CustomerAddress(null, "123 New Street", "New City", "New State", "54321", null);
        CustomerAddress savedAddress = new CustomerAddress(1L, "123 New Street", "New City", "New State", "54321", null);

        when(customerAddressService.saveCustomerAddress(any(CustomerAddress.class))).thenReturn(savedAddress);

        mockMvc.perform(post("/customer-addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAddress)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.street").value("123 New Street"))
                .andExpect(jsonPath("$.city").value("New City"))
                .andExpect(jsonPath("$.state").value("New State"))
                .andExpect(jsonPath("$.zipCode").value("54321"));
    }

    @Test
    void testDeleteCustomerAddress() throws Exception {
        doNothing().when(customerAddressService).deleteCustomerAddress(1L);

        mockMvc.perform(delete("/customer-addresses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteMultipleCustomerAddresses_Success() throws Exception {
        doNothing().when(customerAddressService).deleteMultipleCustomerAddresses(anyList());

        mockMvc.perform(delete("/customer-addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[1, 2, 3]"))
                .andExpect(status().isOk());

        verify(customerAddressService, times(1)).deleteMultipleCustomerAddresses(anyList());
    }

    @Test
    void testDeleteMultipleCustomerAddresses_EmptyList() throws Exception {
        doNothing().when(customerAddressService).deleteMultipleCustomerAddresses(anyList());

        mockMvc.perform(delete("/customer-addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isOk());

        verify(customerAddressService, times(1)).deleteMultipleCustomerAddresses(anyList());
    }


    @Test
    void testUpdateCustomerAddress() throws Exception {
        CustomerAddress updatedAddress = new CustomerAddress(1L, "Updated Street", "Updated City", "Updated State", "67890", null);
        when(customerAddressService.updateCustomerAddress(eq(1L), any())).thenReturn(updatedAddress);

        mockMvc.perform(put("/customer-addresses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"street\": \"Updated Street\", \"city\": \"Updated City\", \"state\": \"Updated State\", \"zipCode\": \"67890\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street").value("Updated Street"))
                .andExpect(jsonPath("$.city").value("Updated City"));
    }

    @Test
    void testUpdateCustomerAddress_Success() throws Exception {
        CustomerAddress updatedAddress = new CustomerAddress(1L, "Updated Street", "Updated City", "Updated State", "54321", null);

        when(customerAddressService.updateCustomerAddress(eq(1L), any(CustomerAddress.class))).thenReturn(updatedAddress);

        mockMvc.perform(put("/customer-addresses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAddress)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street").value("Updated Street"))
                .andExpect(jsonPath("$.city").value("Updated City"))
                .andExpect(jsonPath("$.state").value("Updated State"))
                .andExpect(jsonPath("$.zipCode").value("54321"));
    }

    @Test
    void testUpdateCustomerAddress_NotFound() throws Exception {
        CustomerAddress updatedAddress = new CustomerAddress(null, "Updated Street", "Updated City", "Updated State", "54321", null);

        when(customerAddressService.updateCustomerAddress(eq(1L), any(CustomerAddress.class)))
                .thenThrow(new CustomerAddressNotFoundException(1L));

        mockMvc.perform(put("/customer-addresses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAddress)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.reason").value("CustomerAddress not found for ID: 1"));
    }

}
