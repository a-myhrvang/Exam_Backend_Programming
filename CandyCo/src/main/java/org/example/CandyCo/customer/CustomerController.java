package org.example.CandyCo.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    // REST API-endepunkter
    @GetMapping("/api")
    @ResponseBody
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public Customer getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping("/api")
    @ResponseBody
    public Customer saveCustomer(@RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }

    @DeleteMapping("/api/batch")
    @ResponseBody
    public void deleteMultipleCustomers(@RequestBody List<Long> ids) {
        customerService.deleteMultipleCustomers(ids);
    }

    // Thymeleaf-visning
    @GetMapping
    public String viewCustomers(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        return "customers";
    }
}
