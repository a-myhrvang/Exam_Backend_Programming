package org.example.CandyCo.customer;

import org.example.CandyCo.error.CustomerNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    public CustomerServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCustomerById_CustomerExists() {
        Customer customer = new Customer(1L, "Jane Doe", "123456789", "jane@example.com", null, null);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerById(1L);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
    }

    @Test
    void testGetCustomerById_CustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomerNotFoundException.class, () -> {
            customerService.getCustomerById(1L);
        });

        assertEquals("Customer not found for ID: " + 1L, exception.getMessage());
    }

    @Test
    void testDeleteCustomer_CustomerNotFound() {
        when(customerRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(CustomerNotFoundException.class, () -> {
            customerService.deleteCustomer(1L);
        });

        assertEquals("Customer not found for ID: " + 1L, exception.getMessage());
    }

    @Test
    void testSaveCustomer_NullCustomer() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            customerService.saveCustomer(null);
        });

        assertEquals("Customer cannot be null", exception.getMessage());
    }


    @Test
    void testUpdateCustomer_CustomerExists() {
        Customer existingCustomer = new Customer(1L, "John Doe", "1234567890", "john@example.com", null, null);
        Customer updatedCustomer = new Customer(null, "Jane Doe", "0987654321", "jane@example.com", null, null);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        Customer result = customerService.updateCustomer(1L, updatedCustomer);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        assertEquals("0987654321", result.getPhoneNumber());
        verify(customerRepository, times(1)).save(existingCustomer);
    }

    @Test
    void testUpdateCustomer_CustomerNotFound() {
        Customer updatedCustomer = new Customer(null, "Jane Doe", "0987654321", "jane@example.com", null, null);

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomerNotFoundException.class, () -> {
            customerService.updateCustomer(1L, updatedCustomer);
        });

        assertEquals("Customer not found for ID: " + 1L, exception.getMessage());
    }

    @Test
    void testDeleteCustomer_NullId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            customerService.deleteCustomer(null);
        });

        assertEquals("ID cannot be null", exception.getMessage());
    }

    @Test
    void testDeleteMultipleCustomers_Success() {
        when(customerRepository.existsById(1L)).thenReturn(true);
        when(customerRepository.existsById(2L)).thenReturn(true);

        customerService.deleteMultipleCustomers(List.of(1L, 2L));

        verify(customerRepository, times(1)).deleteById(1L);
        verify(customerRepository, times(1)).deleteById(2L);
    }

    @Test
    void testDeleteMultipleCustomers_EmptyList() {
        List<Long> ids = List.of();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            customerService.deleteMultipleCustomers(ids);
        });

        assertEquals("The list of IDs cannot be empty", exception.getMessage());
    }


    @Test
    void testDeleteMultipleCustomers_NonExistentId() {
        List<Long> ids = List.of(1L, 2L);
        when(customerRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(CustomerNotFoundException.class, () -> {
            customerService.deleteMultipleCustomers(ids);
        });

        assertEquals("Customer not found for ID: " + 1L, exception.getMessage());
    }

    @Test
    void testSaveCustomer_DuplicateEmail() {
        Customer existingCustomer = new Customer(1L, "John Doe", "1234567890", "john.doe@example.com", null, null);
        when(customerRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(existingCustomer));

        Customer newCustomer = new Customer(null, "Jane Doe", "0987654321", "john.doe@example.com", null, null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            customerService.saveCustomer(newCustomer);
        });

        assertEquals("Email already in use: john.doe@example.com", exception.getMessage());
    }
}
