package org.example.CandyCo.repository;

import org.example.CandyCo.product.Product;
import org.example.CandyCo.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void registerDatabaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSaveAndFindProduct() {

        Product product = new Product();
        product.setName("Chocolate");
        product.setDescription("Delicious dark chocolate");
        product.setPrice(9.99);
        product.setStatus("AVAILABLE");
        product.setQuantityOnHand(100);

        Product savedProduct = productRepository.save(product);

        Product foundProduct = productRepository.findById(savedProduct.getId()).orElse(null);
        assertNotNull(foundProduct, "Product should not be null");
        assertEquals("Chocolate", foundProduct.getName(), "Product name should match");
    }

    @Test
    void testFindAllProducts() {

        Product product1 = new Product();
        product1.setName("Candy");
        product1.setDescription("Sweet candy");
        product1.setPrice(2.50);
        product1.setStatus("AVAILABLE");
        product1.setQuantityOnHand(50);
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setName("Gum");
        product2.setDescription("Chewy gum");
        product2.setPrice(1.99);
        product2.setStatus("AVAILABLE");
        product2.setQuantityOnHand(30);
        productRepository.save(product2);

        List<Product> products = productRepository.findAll();
        assertEquals(2, products.size(), "There should be 2 products in the database");
    }
}
