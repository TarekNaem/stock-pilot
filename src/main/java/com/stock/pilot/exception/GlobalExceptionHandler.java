package com.stock.pilot.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<EntityErorrResponse> handleResourceNotFound(
            ResourceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<EntityErorrResponse> handleEntityNotFound(
            EntityNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DuplicateSkuException.class)
    public ResponseEntity<EntityErorrResponse> handleDuplicateSku(
            DuplicateSkuException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(EntityAlreadyExist.class)
    public ResponseEntity<EntityErorrResponse> handleAlreadyExists(
            EntityAlreadyExist ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<EntityErorrResponse> handleInsufficientStock(
            InsufficientStockException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler({BusinessException.class, EntityBadRequest.class})
    public ResponseEntity<EntityErorrResponse> handleBadRequest(
            RuntimeException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<EntityErorrResponse> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> validationErrors = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                validationErrors.put(error.getField(), error.getDefaultMessage())
        );

        EntityErorrResponse response = new EntityErorrResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(validationErrors.toString());
        response.setTimeStamp(System.currentTimeMillis());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<EntityErorrResponse> handleConstraintViolation(
            ConstraintViolationException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<EntityErorrResponse> handleBadCredentials(
            BadCredentialsException ex) {
        return build(HttpStatus.UNAUTHORIZED, "Invalid username or password.");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<EntityErorrResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {
        return build(HttpStatus.CONFLICT, "The requested operation violates a database constraint.");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<EntityErorrResponse> handleAccessDenied(AccessDeniedException ex) {
        return build(HttpStatus.FORBIDDEN, "You do not have permission to perform this operation.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<EntityErorrResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<EntityErorrResponse> handleUnexpectedException(
            Exception ex,
            HttpServletRequest request) {
        // Keep internal exception details out of the API response.
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred."
        );
    }

    private ResponseEntity<EntityErorrResponse> build(
            HttpStatus status,
            String message) {

        EntityErorrResponse response = new EntityErorrResponse();
        response.setStatus(status.value());
        response.setMessage(message);
        response.setTimeStamp(System.currentTimeMillis());

        return ResponseEntity.status(status).body(response);
    }
}
