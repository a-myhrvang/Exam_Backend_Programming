package org.example.CandyCo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class CustomerAddressNotFoundException extends RuntimeException {
    private final Long addressId;

    public CustomerAddressNotFoundException(Long addressId) {
        super("Customer address not found for ID: " + addressId);
        this.addressId = addressId;
    }

    public Long getCustomerAddressId() {
        return addressId;
    }
}