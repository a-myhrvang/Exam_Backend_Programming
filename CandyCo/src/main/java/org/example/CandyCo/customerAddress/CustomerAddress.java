package org.example.CandyCo.customerAddress;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.example.CandyCo.customer.Customer;


@Entity
@Getter @Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;
    private String city;
    private String state;
    private String zipCode;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnoreProperties("addresses")
    private Customer customer;
}

