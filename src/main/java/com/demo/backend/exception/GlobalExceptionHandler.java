package com.demo.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ... keep your existing handlers (BusinessException, Validation, etc.)

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
        String message = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        // detect duplicate key keywords (MySQL) as conflict
        HttpStatus status = HttpStatus.CONFLICT;
        return buildResponse(status, "Data Integrity Violation", message, request.getRequestURI(), null);
    }

    // fallback for SQL Exception if needed
    @ExceptionHandler(java.sql.SQLException.class)
    public ResponseEntity<ErrorResponse> handleSQLException(java.sql.SQLException ex, HttpServletRequest request) {
        String message = ex.getMessage();
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Database Error", message, request.getRequestURI(), null);
    }

    // general builder (reuse your previous buildResponse method)
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String error, String message, String path, Map<String, String> validationErrors) {
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(error)
                .message(message)
                .path(path)
                .validationErrors(validationErrors)
                .build();
        return new ResponseEntity<>(response, status);
    }
}
