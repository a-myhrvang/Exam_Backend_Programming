package org.example.CandyCo.error;


import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RestExceptionHandlerTest {

    private final RestExceptionHandler exceptionHandler = new RestExceptionHandler();

    @Test
    void handleCustomerNotFoundException() {

        Long customerId = 1L;
        CustomerNotFoundException exception = new CustomerNotFoundException(customerId);

        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleCustomerNotFound(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Customer not found for ID: " + customerId, response.getBody().getReason());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertTrue(response.getBody().getTimestamp().isBefore(ZonedDateTime.now()));
    }

    @Test
    void handleProductNotFoundException() {

        Long productId = 42L;
        ProductNotFoundException exception = new ProductNotFoundException(productId);

        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleProductNotFound(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Product not found for ID: " + productId, response.getBody().getReason());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertTrue(response.getBody().getTimestamp().isBefore(ZonedDateTime.now()));
    }

    @Test
    void handleOrderNotFoundException() {

        Long orderId = 7L;
        OrderNotFoundException exception = new OrderNotFoundException(orderId);

        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleOrderNotFound(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Order not found for ID: " + orderId, response.getBody().getReason());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertTrue(response.getBody().getTimestamp().isBefore(ZonedDateTime.now()));
    }

    @Test
    void handleCustomerAddressNotFoundException() {

        Long addressId = 25L;
        CustomerAddressNotFoundException exception = new CustomerAddressNotFoundException(addressId);

        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleCustomerAddressNotFound(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("CustomerAddress not found for ID: " + addressId, response.getBody().getReason());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertTrue(response.getBody().getTimestamp().isBefore(ZonedDateTime.now()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ExceptionResponse response = ExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .reason(ex.getMessage())
                .timestamp(ZonedDateTime.now(ZoneId.of("Europe/Oslo")))
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        ExceptionResponse response = ExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .reason(errorMessage)
                .timestamp(ZonedDateTime.now(ZoneId.of("Europe/Oslo")))
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
