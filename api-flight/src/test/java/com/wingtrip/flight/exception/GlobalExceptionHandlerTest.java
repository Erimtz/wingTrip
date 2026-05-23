package com.wingtrip.flight.exception;

import com.wingtrip.flight.controller.errorhandling.GlobalExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Objects;

import static com.wingtrip.flight.exception.MessageCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        lenient().when(request.getRequestURI()).thenReturn("/api/v1/flights");
        lenient().when(request.getContextPath()).thenReturn("");
        lenient().when(request.getServletPath()).thenReturn("/api/v1/flights");
    }

    @Test
    void testHandleFlightNotFound() {
        when(request.getRequestURI()).thenReturn("/api/v1/flights/LA123");
        FlightNotFoundException exception = new FlightNotFoundException("Flight not found");

        ResponseEntity<Map<String, Object>> response =
                globalExceptionHandler.handleFlightNotFoundException(request, exception);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().get("status"));
        assertEquals("Flight not found", response.getBody().get("error"));
        assertTrue(response.getBody().containsKey("timestamp"));
        assertTrue(response.getBody().containsKey("path"));
    }

    @Test
    void testHandleFlightNotFound_NullMessage() {
        FlightNotFoundException exception = new FlightNotFoundException((String) null);

        ResponseEntity<Map<String, Object>> response =
                globalExceptionHandler.handleFlightNotFoundException(request, exception);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(Objects.requireNonNull(response.getBody()).get("error"));
    }

    @Test
    void testHandleFlightNotCreated() {
        when(request.getRequestURI()).thenReturn("/api/v1/flights");
        FlightNotCreatedException exception = new FlightNotCreatedException("Flight cannot be created");

        ResponseEntity<Map<String, Object>> response =
                globalExceptionHandler.handleFlightException(request, exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Flight cannot be created",
                Objects.requireNonNull(response.getBody()).get("error"));
    }

    @Test
    void testHandleFlightNotUpdated() {
        when(request.getRequestURI()).thenReturn("/api/v1/flights/LA123");
        FlightNotUpdatedException exception = new FlightNotUpdatedException("Flight cannot be updated");

        ResponseEntity<Map<String, Object>> response =
                globalExceptionHandler.handleFlightException(request, exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Flight cannot be updated",
                Objects.requireNonNull(response.getBody()).get("error"));
    }

    @Test
    void testHandleFlightNotDeleted() {
        when(request.getRequestURI()).thenReturn("/api/v1/flights/LA123");
        FlightNotDeletedException exception = new FlightNotDeletedException("Flight cannot be deleted");

        ResponseEntity<Map<String, Object>> response =
                globalExceptionHandler.handleFlightException(request, exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Flight cannot be deleted",
                Objects.requireNonNull(response.getBody()).get("error"));
    }

    @Test
    void testHandleFlightAlreadyCancelled() {
        when(request.getRequestURI()).thenReturn("/api/v1/flights/LA123");
        FlightAlreadyCancelledException exception =
                new FlightAlreadyCancelledException("Flight already cancelled");

        ResponseEntity<Map<String, Object>> response =
                globalExceptionHandler.handleFlightException(request, exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Flight already cancelled",
                Objects.requireNonNull(response.getBody()).get("error"));
    }

    @Test
    void testHandleGenericException() {
        when(request.getRequestURI()).thenReturn("/api/v1/flights");
        Exception exception = new RuntimeException("Internal server error");

        ResponseEntity<Map<String, Object>> response =
                globalExceptionHandler.handleGenericException(request, exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                Objects.requireNonNull(response.getBody()).get("status"));
        assertEquals("Internal server error", response.getBody().get("error"));
    }

    @Test
    void testHandleGenericException_NullMessage() {
        Exception exception = new RuntimeException((String) null);

        ResponseEntity<Map<String, Object>> response =
                globalExceptionHandler.handleGenericException(request, exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(Objects.requireNonNull(response.getBody()).get("error"));
    }

    @Test
    void testResponseBodyContainsAllRequiredFields() {
        when(request.getRequestURI()).thenReturn("/api/v1/flights");
        FlightNotFoundException exception = new FlightNotFoundException("Test error");

        ResponseEntity<Map<String, Object>> response =
                globalExceptionHandler.handleFlightNotFoundException(request, exception);

        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("timestamp"));
        assertTrue(body.containsKey("status"));
        assertTrue(body.containsKey("error"));
        assertTrue(body.containsKey("path"));
    }
}
