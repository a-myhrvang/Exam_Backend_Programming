package org.example.CandyCo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class CustomerNotFoundException extends RuntimeException {
    private final Long customerId;

    public CustomerNotFoundException(Long customerId) {
        super("Customer not found for ID: " + customerId);
        this.customerId = customerId;
    }

    public Long getCustomerId() {
        return customerId;
    }
}