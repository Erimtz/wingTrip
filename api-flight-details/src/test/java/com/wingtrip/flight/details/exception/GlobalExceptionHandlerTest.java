package com.wingtrip.flight.details.exception;

import com.wingtrip.flight.details.controller.errorhandling.GlobalExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private HttpServletRequest mockRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        mockRequest = new MockHttpServletRequest("GET", "/api/v1/flight-details");
    }

    // ==================== FlightDetailsNotFoundException Tests ====================

    @Test
    void testHandleFlightDetailsNotFoundException() {
        FlightDetailsNotFoundException exception = new FlightDetailsNotFoundException("Flight details not found");

        ResponseEntity<Map<String, Object>> response = exceptionHandler
                .handleFlightDetailsNotFoundException(mockRequest, exception);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().get("status"));
        assertEquals("Flight details not found", response.getBody().get("error"));
        assertTrue(response.getBody().containsKey("timestamp"));
        assertTrue(response.getBody().containsKey("path"));
    }

    @Test
    void testHandleFlightDetailsNotFoundException_WithCustomMessage() {
        String customMessage = "Flight details not found with ID: 507f1f77bcf86cd799439011";
        FlightDetailsNotFoundException exception = new FlightDetailsNotFoundException(customMessage);

        ResponseEntity<Map<String, Object>> response = exceptionHandler
                .handleFlightDetailsNotFoundException(mockRequest, exception);

        assertNotNull(response.getBody());
        assertEquals(customMessage, response.getBody().get("error"));
    }

    // ==================== FlightDetailsNotFoundByIdException Tests ====================

    @Test
    void testHandleFlightDetailsNotFoundByIdException() {
        FlightDetailsNotFoundByIdException exception = 
                new FlightDetailsNotFoundByIdException("Flight details not found by ID");

        ResponseEntity<Map<String, Object>> response = exceptionHandler
                .handleFlightDetailsNotFoundByIdException(mockRequest, exception);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().get("status"));
        assertEquals("Flight details not found by ID", response.getBody().get("error"));
    }

    // ==================== FlightDetailsNotFoundByFlightIdException Tests ====================

    @Test
    void testHandleFlightDetailsNotFoundByFlightIdException() {
        FlightDetailsNotFoundByFlightIdException exception = 
                new FlightDetailsNotFoundByFlightIdException("Flight details not found by flight ID");

        ResponseEntity<Map<String, Object>> response = exceptionHandler
                .handleFlightDetailsNotFoundByFlightIdException(mockRequest, exception);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().get("status"));
        assertEquals("Flight details not found by flight ID", response.getBody().get("error"));
    }

    // ==================== FlightDetailsAlreadyExistsException Tests ====================

    @Test
    void testHandleFlightDetailsAlreadyExistsException() {
        FlightDetailsAlreadyExistsException exception = 
                new FlightDetailsAlreadyExistsException("Flight details already exist for this flight");

        ResponseEntity<Map<String, Object>> response = exceptionHandler
                .handleFlightDetailsAlreadyExistsException(mockRequest, exception);

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(409, response.getBody().get("status"));
        assertEquals("Flight details already exist for this flight", response.getBody().get("error"));
    }

    // ==================== FlightDetailsNotCreateException Tests ====================

    @Test
    void testHandleFlightDetailsNotCreateException() {
        FlightDetailsNotCreateException exception = 
                new FlightDetailsNotCreateException("Failed to create flight details");

        ResponseEntity<Map<String, Object>> response = exceptionHandler
                .handleFlightDetailsException(mockRequest, exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Failed to create flight details", response.getBody().get("error"));
    }

    // ==================== FlightDetailsNotUpdateException Tests ====================

    @Test
    void testHandleFlightDetailsNotUpdateException() {
        FlightDetailsNotUpdateException exception = 
                new FlightDetailsNotUpdateException("Failed to update flight details");

        ResponseEntity<Map<String, Object>> response = exceptionHandler
                .handleFlightDetailsException(mockRequest, exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Failed to update flight details", response.getBody().get("error"));
    }

    // ==================== FlightDetailsNotDeleteException Tests ====================

    @Test
    void testHandleFlightDetailsNotDeleteException() {
        FlightDetailsNotDeleteException exception = 
                new FlightDetailsNotDeleteException("Failed to delete flight details");

        ResponseEntity<Map<String, Object>> response = exceptionHandler
                .handleFlightDetailsException(mockRequest, exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Failed to delete flight details", response.getBody().get("error"));
    }

    // ==================== Generic Exception Tests ====================

    @Test
    void testHandleGenericException() {
        RuntimeException exception = new RuntimeException("Unexpected error occurred");

        ResponseEntity<Map<String, Object>> response = exceptionHandler
                .handleGenericException(mockRequest, exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().get("status"));
        assertEquals("Unexpected error occurred", response.getBody().get("error"));
    }

    @Test
    void testHandleGenericException_DatabaseError() {
        RuntimeException exception = new RuntimeException("Database connection failed");

        ResponseEntity<Map<String, Object>> response = exceptionHandler
                .handleGenericException(mockRequest, exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().get("status"));
        assertEquals("Database connection failed", response.getBody().get("error"));
    }

    // ==================== Response Structure Tests ====================

    @Test
    void testExceptionresponse_ContainsAllRequiredFields() {
        FlightDetailsNotFoundException exception = new FlightDetailsNotFoundException("Test error");

        ResponseEntity<Map<String, Object>> response = exceptionHandler
                .handleFlightDetailsNotFoundException(mockRequest, exception);

        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("timestamp"));
        assertTrue(body.containsKey("status"));
        assertTrue(body.containsKey("error"));
        assertTrue(body.containsKey("path"));

        assertNotNull(body.get("timestamp"));
        assertNotNull(body.get("status"));
        assertNotNull(body.get("error"));
        assertNotNull(body.get("path"));
    }

    @Test
    void testExceptionResponse_TimestampIsLong() {
        FlightDetailsNotCreateException exception = 
                new FlightDetailsNotCreateException("Create failed");

        ResponseEntity<Map<String, Object>> response = exceptionHandler
                .handleFlightDetailsException(mockRequest, exception);

        assertNotNull(response.getBody());
        Object timestamp = response.getBody().get("timestamp");
        assertInstanceOf(Long.class, timestamp);
        assertTrue((Long) timestamp > 0);
    }

    @Test
    void testExceptionResponse_PathIsCorrect() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", 
                "/api/v1/flight-details");
        FlightDetailsAlreadyExistsException exception = 
                new FlightDetailsAlreadyExistsException("Already exists");

        ResponseEntity<Map<String, Object>> response = exceptionHandler
                .handleFlightDetailsAlreadyExistsException(request, exception);

        assertNotNull(response.getBody());
        String path = (String) response.getBody().get("path");
        assertNotNull(path);
        assertTrue(path.contains("/api/v1/flight-details"));
    }

    // ==================== HTTP Status Code Tests ====================

    @Test
    void testCorrectHttpStatusCodes() {
        // 404 - Not Found
        ResponseEntity<?> notFoundResponse = exceptionHandler
                .handleFlightDetailsNotFoundException(mockRequest, 
                        new FlightDetailsNotFoundException("Not found"));
        assertEquals(HttpStatus.NOT_FOUND, notFoundResponse.getStatusCode());

        // 409 - Conflict
        ResponseEntity<?> conflictResponse = exceptionHandler
                .handleFlightDetailsAlreadyExistsException(mockRequest, 
                        new FlightDetailsAlreadyExistsException("Conflict"));
        assertEquals(HttpStatus.CONFLICT, conflictResponse.getStatusCode());

        // 400 - Bad Request
        ResponseEntity<?> badRequestResponse = exceptionHandler
                .handleFlightDetailsException(mockRequest, 
                        new FlightDetailsNotCreateException("Bad request"));
        assertEquals(HttpStatus.BAD_REQUEST, badRequestResponse.getStatusCode());

        // 500 - Internal Server Error
        ResponseEntity<?> internalErrorResponse = exceptionHandler
                .handleGenericException(mockRequest, 
                        new RuntimeException("Internal error"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, internalErrorResponse.getStatusCode());
    }

    // ==================== Multiple Exception Types in Single Handler Tests ====================

    @Test
    void testHandleFlightDetailsException_WithNotCreateException() {
        FlightDetailsNotCreateException exception = 
                new FlightDetailsNotCreateException("Creation failed");

        ResponseEntity<Map<String, Object>> response = exceptionHandler
                .handleFlightDetailsException(mockRequest, exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().get("status"));
    }

    @Test
    void testHandleFlightDetailsException_WithNotUpdateException() {
        FlightDetailsNotUpdateException exception = 
                new FlightDetailsNotUpdateException("Update failed");

        ResponseEntity<Map<String, Object>> response = exceptionHandler
                .handleFlightDetailsException(mockRequest, exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().get("status"));
    }

    @Test
    void testHandleFlightDetailsException_WithNotDeleteException() {
        FlightDetailsNotDeleteException exception = 
                new FlightDetailsNotDeleteException("Delete failed");

        ResponseEntity<Map<String, Object>> response = exceptionHandler
                .handleFlightDetailsException(mockRequest, exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().get("status"));
    }

    // ==================== Edge Cases ====================

    @Test
    void testExceptionHandler_WithNullMessage() {
        FlightDetailsNotFoundException exception = new FlightDetailsNotFoundException(null);

        ResponseEntity<Map<String, Object>> response = exceptionHandler
                .handleFlightDetailsNotFoundException(mockRequest, exception);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testExceptionHandler_WithEmptyMessage() {
        FlightDetailsNotFoundException exception = new FlightDetailsNotFoundException("");

        ResponseEntity<Map<String, Object>> response = exceptionHandler
                .handleFlightDetailsNotFoundException(mockRequest, exception);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("", response.getBody().get("error"));
    }

    @Test
    void testExceptionHandler_WithLongMessage() {
        String longMessage = "Flight details not found. " + "X".repeat(500);
        FlightDetailsNotFoundException exception = new FlightDetailsNotFoundException(longMessage);

        ResponseEntity<Map<String, Object>> response = exceptionHandler
                .handleFlightDetailsNotFoundException(mockRequest, exception);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(longMessage, response.getBody().get("error"));
    }
}

