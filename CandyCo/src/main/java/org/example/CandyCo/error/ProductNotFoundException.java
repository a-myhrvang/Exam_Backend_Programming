package org.example.CandyCo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {
    private final Long productId;

    public ProductNotFoundException(Long productId) {
        super("Product not found for ID: " + productId);
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}