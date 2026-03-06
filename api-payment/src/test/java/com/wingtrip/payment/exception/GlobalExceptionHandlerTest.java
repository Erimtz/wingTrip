package com.wingtrip.payment.exception;

import com.wingtrip.payment.controller.errorhandling.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/payment/test");
    }

    @Test
    void handleCustomException_paymentNotFoundException() {
        PaymentNotFoundException ex = new PaymentNotFoundException(MessageCode.PAYMENT_NOT_FOUND_BY_ID);

        ResponseEntity<Map<String, Object>> result = exceptionHandler.handleCustomException(request, ex);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(400, result.getBody().get("Status"));
    }

    @Test
    void handleCustomException_paymentNotCreatedException() {
        PaymentNotCreatedException ex = new PaymentNotCreatedException(MessageCode.PAYMENT_NOT_CREATE);

        ResponseEntity<Map<String, Object>> result = exceptionHandler.handleCustomException(request, ex);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void handleCustomException_paymentAlreadyProcessedException() {
        PaymentAlreadyProcessedException ex = new PaymentAlreadyProcessedException(MessageCode.PAYMENT_ALREADY_PROCESSED);

        ResponseEntity<Map<String, Object>> result = exceptionHandler.handleCustomException(request, ex);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void handleCustomException_paymentAlreadyRefundedException() {
        PaymentAlreadyRefundedException ex = new PaymentAlreadyRefundedException(MessageCode.PAYMENT_ALREADY_REFUNDED);

        ResponseEntity<Map<String, Object>> result = exceptionHandler.handleCustomException(request, ex);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void handleGenericException_returnsInternalServerError() {
        Exception ex = new RuntimeException("Unexpected error");

        ResponseEntity<Map<String, Object>> result = exceptionHandler.handleGenericException(request, ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(500, result.getBody().get("Status"));
    }

    @Test
    void handleGenericException_withCustomException_returnsNull() {
        PaymentNotFoundException ex = new PaymentNotFoundException(MessageCode.PAYMENT_NOT_FOUND_BY_ID);

        ResponseEntity<Map<String, Object>> result = exceptionHandler.handleGenericException(request, ex);

        assertNull(result);
    }
}
