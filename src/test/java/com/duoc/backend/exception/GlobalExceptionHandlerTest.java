package com.duoc.backend.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Map;

public class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    public void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    public void testHandleRuntimeException() {
        // Arrange
        RuntimeException ex = new RuntimeException("Unexpected error occurred");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleRuntimeException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertResponse(response.getBody(), HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
    }

    @Test
    public void testHandleUserNotFoundException() {
        // Arrange
        UserNotFoundException ex = new UserNotFoundException("User not found");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleUserNotFoundException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertResponse(response.getBody(), HttpStatus.NOT_FOUND, "User not found");
    }

    @Test
    public void testHandleInvalidCredentialsException() {
        // Arrange
        InvalidCredentialsException ex = new InvalidCredentialsException("Invalid credentials");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleInvalidCredentialsException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertResponse(response.getBody(), HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }

    private void assertResponse(Map<String, Object> response, HttpStatus status, String message) {
        assertNotNull(response);
        assertEquals(status.value(), response.get("status"));
        assertEquals(status.getReasonPhrase(), response.get("error"));
        assertEquals(message, response.get("message"));
        assertNotNull(response.get("timestamp")); // Ensure timestamp is present
    }
}
