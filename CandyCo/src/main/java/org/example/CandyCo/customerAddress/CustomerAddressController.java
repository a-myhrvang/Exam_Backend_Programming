package org.example.CandyCo.customerAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer-addresses")
public class CustomerAddressController {
    @Autowired
    private CustomerAddressService customerAddressService;

    @GetMapping
    public List<CustomerAddress> getAllCustomerAddresses() {
        return customerAddressService.getAllCustomerAddresses();
    }

    @GetMapping("/{id}")
    public CustomerAddress getCustomerAddressById(@PathVariable Long id) {
        return customerAddressService.getCustomerAddressById(id);
    }

    @PostMapping
    public CustomerAddress createCustomerAddress(@RequestBody CustomerAddress customerAddress) {
        return customerAddressService.saveCustomerAddress(customerAddress);
    }

    @PutMapping("/{id}")
    public CustomerAddress updateCustomerAddress(@PathVariable Long id, @RequestBody CustomerAddress updatedAddress) {
        return customerAddressService.updateCustomerAddress(id, updatedAddress);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomerAddress(@PathVariable Long id) {
        customerAddressService.deleteCustomerAddress(id);
    }

    @DeleteMapping
    public void deleteMultipleCustomerAddresses(@RequestBody List<Long> ids) {
        customerAddressService.deleteMultipleCustomerAddresses(ids);
    }
}