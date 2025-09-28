package org.example.CandyCo.customerAddress;


import org.example.CandyCo.customer.Customer;
import org.example.CandyCo.error.CustomerAddressNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerAddressServiceTest {

    @Mock
    private CustomerAddressRepository customerAddressRepository;

    @InjectMocks
    private CustomerAddressService customerAddressService;

    public CustomerAddressServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCustomerAddressById_AddressExists() {
        CustomerAddress address = new CustomerAddress(1L, "123 Street", "City", "State", "12345", null);
        when(customerAddressRepository.findById(1L)).thenReturn(Optional.of(address));

        CustomerAddress result = customerAddressService.getCustomerAddressById(1L);

        assertNotNull(result);
        assertEquals("123 Street", result.getStreet());
    }

    @Test
    void testGetCustomerAddressById_AddressNotFound() {
        when(customerAddressRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomerAddressNotFoundException.class, () -> {
            customerAddressService.getCustomerAddressById(1L);
        });

        assertEquals("Customer address not found for ID: " + 1L, exception.getMessage());
    }

    @Test
    void testSaveCustomerAddress() {
        CustomerAddress address = new CustomerAddress(null, "123 Street", "City", "State", "12345", null);
        when(customerAddressRepository.save(any(CustomerAddress.class))).thenReturn(address);

        CustomerAddress result = customerAddressService.saveCustomerAddress(address);

        assertNotNull(result);
        verify(customerAddressRepository, times(1)).save(address);
    }

    @Test
    void testDeleteCustomerAddress_AddressExists() {
        when(customerAddressRepository.existsById(1L)).thenReturn(true);

        customerAddressService.deleteCustomerAddress(1L);

        verify(customerAddressRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCustomerAddress_AddressNotFound() {
        when(customerAddressRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(CustomerAddressNotFoundException.class, () -> {
            customerAddressService.deleteCustomerAddress(1L);
        });

        assertEquals("Customer address not found for ID: " + 1L, exception.getMessage());
    }

    @Test
    void testUpdateCustomerAddress() {
        CustomerAddress existingAddress = new CustomerAddress(1L, "123 Street", "City", "State", "12345", null);
        CustomerAddress updatedAddress = new CustomerAddress(1L, "456 Avenue", "New City", "New State", "67890", null);

        when(customerAddressRepository.findById(1L)).thenReturn(Optional.of(existingAddress));
        when(customerAddressRepository.save(any(CustomerAddress.class))).thenReturn(updatedAddress);

        CustomerAddress result = customerAddressService.saveCustomerAddress(updatedAddress);

        assertNotNull(result);
        assertEquals("456 Avenue", result.getStreet());
        assertEquals("New City", result.getCity());
        verify(customerAddressRepository, times(1)).save(updatedAddress);
    }

    @Test
    void testGetAllCustomerAddresses() {
        CustomerAddress address1 = new CustomerAddress(1L, "123 Street", "City", "State", "12345", null);
        CustomerAddress address2 = new CustomerAddress(2L, "456 Avenue", "New City", "New State", "67890", null);

        when(customerAddressRepository.findAll()).thenReturn(List.of(address1, address2));

        List<CustomerAddress> addresses = customerAddressService.getAllCustomerAddresses();

        assertNotNull(addresses);
        assertEquals(2, addresses.size());
        assertEquals("123 Street", addresses.get(0).getStreet());
        assertEquals("456 Avenue", addresses.get(1).getStreet());
    }

    @Test
    void testSaveCustomerAddress_MissingFields() {
        CustomerAddress address = new CustomerAddress(null, null, null, null, null, null);

        when(customerAddressRepository.save(any(CustomerAddress.class))).thenThrow(new IllegalArgumentException("Invalid address data"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            customerAddressService.saveCustomerAddress(address);
        });

        assertEquals("Invalid Customer Address: All fields are required", exception.getMessage());
    }

    @Test
    void testCustomerAddressBelongsToCustomer() {
        Customer customer = new Customer(1L, "John Doe", "1234567890", "john.doe@example.com", null, null);
        CustomerAddress address = new CustomerAddress(1L, "123 Street", "City", "State", "12345", customer);

        when(customerAddressRepository.findById(1L)).thenReturn(Optional.of(address));

        CustomerAddress result = customerAddressService.getCustomerAddressById(1L);

        assertNotNull(result);
        assertNotNull(result.getCustomer());
        assertEquals("John Doe", result.getCustomer().getName());
    }

    @Test
    void testDeleteMultipleCustomerAddresses() {
        when(customerAddressRepository.existsById(1L)).thenReturn(true);
        when(customerAddressRepository.existsById(2L)).thenReturn(true);

        customerAddressService.deleteCustomerAddress(1L);
        customerAddressService.deleteCustomerAddress(2L);

        verify(customerAddressRepository, times(1)).deleteById(1L);
        verify(customerAddressRepository, times(1)).deleteById(2L);
    }

    @Test
    void testDeleteMultipleCustomerAddresses_EmptyList() {
        customerAddressService.deleteMultipleCustomerAddresses(List.of());
        verify(customerAddressRepository, times(0)).deleteById(anyLong());
    }

    @Test
    void testSaveCustomerAddress_InvalidData() {
        CustomerAddress invalidAddress = new CustomerAddress(null, null, null, null, null, null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            customerAddressService.saveCustomerAddress(invalidAddress);
        });

        assertEquals("Invalid Customer Address: All fields are required", exception.getMessage());
    }

    @Test
    void testDeleteCustomerAddress_NullId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            customerAddressService.deleteCustomerAddress(null);
        });

        assertEquals("ID cannot be null", exception.getMessage());
    }


    @Test
    void testSaveCustomerAddress_PartialInvalidData() {
        CustomerAddress address = new CustomerAddress(null, "123 Street", null, null, null, null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            customerAddressService.saveCustomerAddress(address);
        });

        assertEquals("Invalid Customer Address: All fields are required", exception.getMessage());
    }

    @Test
    void testGetAllCustomerAddresses_MultipleAddresses() {
        CustomerAddress address1 = new CustomerAddress(1L, "123 Street", "City1", "State1", "12345", null);
        CustomerAddress address2 = new CustomerAddress(2L, "456 Avenue", "City2", "State2", "67890", null);

        when(customerAddressRepository.findAll()).thenReturn(List.of(address1, address2));

        List<CustomerAddress> addresses = customerAddressService.getAllCustomerAddresses();

        assertNotNull(addresses);
        assertEquals(2, addresses.size());
        assertEquals("123 Street", addresses.get(0).getStreet());
        assertEquals("456 Avenue", addresses.get(1).getStreet());
    }

    @Test
    void testUpdateCustomerAddress_AddressNotFound() {
        CustomerAddress updatedAddress = new CustomerAddress(null, "New Street", "New City", "New State", "12345", null);

        when(customerAddressRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomerAddressNotFoundException.class, () -> {
            customerAddressService.updateCustomerAddress(1L, updatedAddress);
        });

        assertEquals("Customer address not found for ID: " + 1L, exception.getMessage());
    }

    @Test
    void testDeleteMultipleCustomerAddresses_Success() {
        when(customerAddressRepository.existsById(1L)).thenReturn(true);
        when(customerAddressRepository.existsById(2L)).thenReturn(true);

        customerAddressService.deleteMultipleCustomerAddresses(List.of(1L, 2L));

        verify(customerAddressRepository, times(1)).deleteById(1L);
        verify(customerAddressRepository, times(1)).deleteById(2L);
    }

    @Test
    void testSaveCustomerAddress_DatabaseError() {
        CustomerAddress address = new CustomerAddress(null, "123 Street", "City", "State", "12345", null);

        when(customerAddressRepository.save(any(CustomerAddress.class))).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            customerAddressService.saveCustomerAddress(address);
        });

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void testUpdateCustomerAddress_InvalidZipCode() {
        CustomerAddress address = new CustomerAddress(1L, "123 Main St", "City", "State", "ABC", null);
        when(customerAddressRepository.findById(1L)).thenReturn(Optional.of(address));

        CustomerAddress updatedAddress = new CustomerAddress(1L, "456 Elm St", "NewCity", "NewState", "12A34", null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            customerAddressService.updateCustomerAddress(1L, updatedAddress);
        });

        assertEquals("Invalid zip code format", exception.getMessage());
    }

    @Test
    void testUpdateCustomerAddress_MissingFields() {
        CustomerAddress updatedAddress = new CustomerAddress(1L, null, "City", "State", "12345", null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            customerAddressService.updateCustomerAddress(1L, updatedAddress);
        });

        assertEquals("All fields are required for updating the address", exception.getMessage());
    }

    @Test
    void testUpdateCustomerAddress_NotFound() {
        CustomerAddress updatedAddress = new CustomerAddress(1L, "Street", "City", "State", "12345", null);

        when(customerAddressRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomerAddressNotFoundException.class, () -> {
            customerAddressService.updateCustomerAddress(1L, updatedAddress);
        });

        assertEquals("Customer address not found for ID: 1", exception.getMessage());
    }

    @Test
    void testDeleteMultipleCustomerAddresses_InvalidId() {
        when(customerAddressRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(CustomerAddressNotFoundException.class, () -> {
            customerAddressService.deleteMultipleCustomerAddresses(List.of(1L));
        });

        assertEquals("Customer address not found for ID: 1", exception.getMessage());
    }
}
