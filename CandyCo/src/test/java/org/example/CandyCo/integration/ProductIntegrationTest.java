package org.example.CandyCo.integration;

import org.example.CandyCo.product.Product;
import org.example.CandyCo.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class ProductIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
    }

    @Test
    void testSaveAndRetrieveProduct() {
        Product product = new Product();
        product.setName("Chocolate");
        product.setPrice(5.99);
        Product savedProduct = productRepository.save(product);

        Product retrievedProduct = productRepository.findById(savedProduct.getId()).orElse(null);
        assertNotNull(retrievedProduct);
        assertEquals("Chocolate", retrievedProduct.getName());
        assertEquals(5.99, retrievedProduct.getPrice());
    }

    @Test
    void testFindAllProducts() {
        Product product1 = new Product();
        product1.setName("Chocolate");
        product1.setPrice(5.99);

        Product product2 = new Product();
        product2.setName("Candy");
        product2.setPrice(2.99);

        productRepository.save(product1);
        productRepository.save(product2);

        List<Product> products = productRepository.findAll();

        assertNotNull(products);
        assertEquals(2, products.size());
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Chocolate")));
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Candy")));
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product();
        product.setName("Chocolate");
        product.setPrice(5.99);
        Product savedProduct = productRepository.save(product);

        savedProduct.setPrice(6.99);
        productRepository.save(savedProduct);

        Product updatedProduct = productRepository.findById(savedProduct.getId()).orElse(null);
        assertNotNull(updatedProduct);
        assertEquals(6.99, updatedProduct.getPrice());
    }

    @Test
    void testDeleteProduct() {
        Product product = new Product();
        product.setName("Chocolate");
        product.setPrice(5.99);
        Product savedProduct = productRepository.save(product);

        productRepository.deleteById(savedProduct.getId());
        assertFalse(productRepository.existsById(savedProduct.getId()));
    }

    @Test
    void testFindNonExistentProduct() {
        Product product = productRepository.findById(999L).orElse(null);
        assertNull(product);
    }
}
