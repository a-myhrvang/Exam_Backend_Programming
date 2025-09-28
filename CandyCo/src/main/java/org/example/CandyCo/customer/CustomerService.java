package org.example.CandyCo.customer;

import org.example.CandyCo.error.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    public Customer saveCustomer(Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use: " + customer.getEmail());
        }
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        Customer existingCustomer = customerRepository.findById(id).orElseThrow(() ->
                new CustomerNotFoundException(id));

        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());

        return customerRepository.save(existingCustomer);
    }

    public void deleteCustomer(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException(id);
        }
        customerRepository.deleteById(id);
    }

    public void deleteMultipleCustomers(List<Long> ids) {

        Objects.requireNonNull(ids, "The list of IDs cannot be null");

        if (ids.isEmpty()) {
            throw new IllegalArgumentException("The list of IDs cannot be empty");
        }

        for (Long id : ids) {
            if (id == null) {
                throw new IllegalArgumentException("One of the IDs in the list is null");
            }

            if (!customerRepository.existsById(id)) {
                throw new CustomerNotFoundException(id);
            }

            customerRepository.deleteById(id);
        }
    }
}

