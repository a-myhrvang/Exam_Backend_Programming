package org.example.CandyCo.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = CustomerNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleCustomerNotFound(CustomerNotFoundException ex) {
        ExceptionResponse response = ExceptionResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .reason("Customer not found for ID: " + ex.getCustomerId())
                .timestamp(ZonedDateTime.now(ZoneId.of("Europe/Oslo")))
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ProductNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleProductNotFound(ProductNotFoundException ex) {
        ExceptionResponse response = ExceptionResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .reason("Product not found for ID: " + ex.getProductId())
                .timestamp(ZonedDateTime.now(ZoneId.of("Europe/Oslo")))
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = OrderNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleOrderNotFound(OrderNotFoundException ex) {
        ExceptionResponse response = ExceptionResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .reason("Order not found for ID: " + ex.getOrderId())
                .timestamp(ZonedDateTime.now(ZoneId.of("Europe/Oslo")))
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = CustomerAddressNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleCustomerAddressNotFound(CustomerAddressNotFoundException ex) {
        ExceptionResponse response = ExceptionResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .reason("CustomerAddress not found for ID: " + ex.getCustomerAddressId())
                .timestamp(ZonedDateTime.now(ZoneId.of("Europe/Oslo")))
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
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
