package org.example.CandyCo.product;

import org.example.CandyCo.error.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        if (!List.of("AVAILABLE", "OUT_OF_STOCK", "DISCONTINUED").contains(updatedProduct.getStatus())) {
            throw new IllegalArgumentException("Invalid product status");
        }

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStatus(updatedProduct.getStatus());
        existingProduct.setQuantityOnHand(updatedProduct.getQuantityOnHand());

        return productRepository.save(existingProduct);
    }
}
