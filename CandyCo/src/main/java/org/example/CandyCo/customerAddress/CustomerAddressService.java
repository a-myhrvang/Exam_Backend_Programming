package org.example.CandyCo.customerAddress;

import org.example.CandyCo.error.CustomerAddressNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerAddressService {
    @Autowired
    private CustomerAddressRepository customerAddressRepository;

    public List<CustomerAddress> getAllCustomerAddresses() {
        return customerAddressRepository.findAll();
    }

    public CustomerAddress getCustomerAddressById(Long id) {
        return customerAddressRepository.findById(id).orElseThrow(() ->
                new CustomerAddressNotFoundException(id));
    }

    public CustomerAddress saveCustomerAddress(CustomerAddress customerAddress) {
        if (customerAddress.getStreet() == null || customerAddress.getStreet().isEmpty() ||
                customerAddress.getCity() == null || customerAddress.getCity().isEmpty() ||
                customerAddress.getState() == null || customerAddress.getState().isEmpty() ||
                customerAddress.getZipCode() == null || customerAddress.getZipCode().isEmpty()) {
            throw new IllegalArgumentException("Invalid Customer Address: All fields are required");
        }

        return customerAddressRepository.save(customerAddress);
    }

    public CustomerAddress updateCustomerAddress(Long id, CustomerAddress updatedAddress) {
        if (updatedAddress.getStreet() == null || updatedAddress.getCity() == null || updatedAddress.getState() == null || updatedAddress.getZipCode() == null) {
            throw new IllegalArgumentException("All fields are required for updating the address");
        }

        if (!updatedAddress.getZipCode().matches("\\d{5}")) {
            throw new IllegalArgumentException("Invalid zip code format");
        }

        CustomerAddress existingAddress = customerAddressRepository.findById(id)
                .orElseThrow(() -> new CustomerAddressNotFoundException(id));

        existingAddress.setStreet(updatedAddress.getStreet());
        existingAddress.setCity(updatedAddress.getCity());
        existingAddress.setState(updatedAddress.getState());
        existingAddress.setZipCode(updatedAddress.getZipCode());

        return customerAddressRepository.save(existingAddress);
    }

    public void deleteCustomerAddress(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        if (!customerAddressRepository.existsById(id)) {
            throw new CustomerAddressNotFoundException(id);
        }

        customerAddressRepository.deleteById(id);
    }

    public void deleteMultipleCustomerAddresses(List<Long> ids) {
        ids.forEach(id -> {
            if (!customerAddressRepository.existsById(id)) {
                throw new CustomerAddressNotFoundException(id);
            }
            customerAddressRepository.deleteById(id);
        });
    }
}
