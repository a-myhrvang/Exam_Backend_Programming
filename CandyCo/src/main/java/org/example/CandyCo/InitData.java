package org.example.CandyCo;

import com.github.javafaker.Faker;
import org.example.CandyCo.customer.Customer;
import org.example.CandyCo.customer.CustomerRepository;
import org.example.CandyCo.customerAddress.CustomerAddress;
import org.example.CandyCo.customerAddress.CustomerAddressRepository;
import org.example.CandyCo.order.Order;
import org.example.CandyCo.order.OrderRepository;
import org.example.CandyCo.product.Product;
import org.example.CandyCo.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Service
public class InitData {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerAddressRepository customerAddressRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    private final Faker faker = new Faker(new Locale("en"));
    private final Random random = new Random();

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        // Create Products
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product product = new Product(
                    null,
                    faker.commerce().productName(),
                    faker.lorem().sentence(),
                    Double.parseDouble(faker.commerce().price().replace(",", ".")),
                    "AVAILABLE",
                    random.nextInt(100) + 1
            );
            products.add(productRepository.save(product));
        }

        // Create Customers with Addresses and Orders
        for (int i = 0; i < 5; i++) {
            Customer customer = new Customer();
            String fullName = faker.name().fullName();
            customer.setName(fullName.substring(0, Math.min(12, fullName.length())));
            String phoneNumber = faker.phoneNumber().phoneNumber();
            customer.setPhoneNumber(phoneNumber.substring(0, Math.min(20, phoneNumber.length())));
            String email = faker.internet().emailAddress();
            customer.setEmail(email.substring(0, Math.min(20, email.length())));
            customer = customerRepository.save(customer);

            // Generate Addresses for the Customer
            List<CustomerAddress> addresses = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                CustomerAddress address = new CustomerAddress();
                address.setStreet(faker.address().streetAddress());
                address.setCity(faker.address().city());
                address.setState(faker.address().state());
                address.setZipCode(faker.address().zipCode());
                address.setCustomer(customer);
                addresses.add(customerAddressRepository.save(address));
            }
            customer.setAddresses(addresses);
            customerRepository.save(customer);

            // Create Orders for the Customer
            for (int k = 0; k < 2; k++) {
                Order order = new Order();
                order.setCustomer(customer);
                order.setShippingAddress(addresses.get(random.nextInt(addresses.size())));
                order.setProducts(products.subList(0, random.nextInt(products.size())));
                order.setShippingCharge(5.0);
                order.setTotalPrice(order.getProducts().stream().mapToDouble(Product::getPrice).sum() + 5.0);
                order.setShipped(random.nextBoolean());
                orderRepository.save(order);
            }
        }
    }
}
