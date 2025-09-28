package org.example.CandyCo.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.CandyCo.customer.Customer;
import org.example.CandyCo.customerAddress.CustomerAddress;
import org.example.CandyCo.product.Product;

import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    private double shippingCharge;
    private double totalPrice;
    private boolean shipped;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnoreProperties({"orders", "addresses"})
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "shipping_address_id")
    @JsonIgnoreProperties("customer")
    private CustomerAddress shippingAddress;
}