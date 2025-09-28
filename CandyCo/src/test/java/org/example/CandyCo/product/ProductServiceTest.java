package org.example.CandyCo.product;

import org.example.CandyCo.error.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    public ProductServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProductById_ProductExists() {
        Product product = new Product();
        product.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetProductById_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.getProductById(1L);
        });

        assertEquals("Product not found for ID: " + 1L, exception.getMessage());
    }

    @Test
    void testSaveProduct() {
        Product product = new Product();
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.saveProduct(product);

        assertNotNull(result);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testGetAllProducts() {
        Product product1 = new Product();
        product1.setId(1L);
        Product product2 = new Product();
        product2.setId(2L);

        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        List<Product> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals(1L, products.get(0).getId());
        assertEquals(2L, products.get(1).getId());
    }

    @Test
    void testDeleteProduct_ProductExists() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProduct_ProductNotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.deleteProduct(1L);
        });

        assertEquals("Product not found for ID: " + 1L, exception.getMessage());
    }

    @Test
    void testUpdateProduct_InvalidStatus() {
        Product product = new Product(1L, "Product A", "Description", 10.0, "AVAILABLE", 50);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product updatedProduct = new Product(1L, "Product A", "Description", 10.0, "INVALID_STATUS", 50);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(1L, updatedProduct);
        });

        assertEquals("Invalid product status", exception.getMessage());
    }

    @Test
    void testUpdateProduct_ProductExists() {
        Product existingProduct = new Product(1L, "Product A", "Description", 10.0, "AVAILABLE", 50);
        Product updatedProduct = new Product(1L, "Updated Product A", "Updated Description", 20.0, "OUT_OF_STOCK", 30);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        Product result = productService.updateProduct(1L, updatedProduct);

        assertNotNull(result);
        assertEquals("Updated Product A", result.getName());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(20.0, result.getPrice());
        assertEquals("OUT_OF_STOCK", result.getStatus());
        assertEquals(30, result.getQuantityOnHand());
        verify(productRepository, times(1)).save(updatedProduct);
    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        Product updatedProduct = new Product(1L, "Product A", "Description", 10.0, "AVAILABLE", 50);

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.updateProduct(1L, updatedProduct);
        });

        assertEquals("Product not found for ID: 1", exception.getMessage());
    }

    @Test
    void testUpdateProduct_NullId() {
        Product updatedProduct = new Product(null, "Product A", "Description", 10.0, "AVAILABLE", 50);

        Exception exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.updateProduct(null, updatedProduct);
        });

        assertEquals("Product not found for ID: null", exception.getMessage());
    }


    @Test
    void testUpdateProduct_UnchangedFields() {
        Product existingProduct = new Product(1L, "Product A", "Description", 10.0, "AVAILABLE", 50);
        Product updatedProduct = new Product(1L, "Updated Product A", "Updated Description", 20.0, "OUT_OF_STOCK", 30);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        Product result = productService.updateProduct(1L, updatedProduct);

        assertNotNull(result);
        assertEquals(existingProduct.getId(), result.getId()); // ID skal være uendret
        verify(productRepository, times(1)).save(updatedProduct);
    }
}
