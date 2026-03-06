package com.wingtrip.payment.controller;

import com.wingtrip.payment.controller.mapper.PaymentMapper;
import com.wingtrip.payment.controller.request.CreatePaymentRequest;
import com.wingtrip.payment.controller.response.PaymentResponse;
import com.wingtrip.payment.dto.PaymentDTO;
import com.wingtrip.payment.exception.*;
import com.wingtrip.payment.model.PaymentStatus;
import com.wingtrip.payment.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentController paymentController;

    private PaymentDTO paymentDTO;
    private PaymentResponse paymentResponse;
    private CreatePaymentRequest createRequest;

    @BeforeEach
    void setUp() {
        paymentDTO = PaymentDTO.builder()
                .paymentId(1L)
                .paymentType("CREDIT_CARD")
                .paymentStatus(PaymentStatus.PENDING)
                .paymentDate(LocalDateTime.now())
                .amount(new BigDecimal("450.00"))
                .currency("USD")
                .bookingId(1L)
                .build();

        paymentResponse = PaymentResponse.builder()
                .paymentId(1L)
                .paymentType("CREDIT_CARD")
                .paymentStatus(PaymentStatus.PENDING)
                .amount(new BigDecimal("450.00"))
                .currency("USD")
                .bookingId(1L)
                .build();

        createRequest = CreatePaymentRequest.builder()
                .paymentType("CREDIT_CARD")
                .amount(new BigDecimal("450.00"))
                .currency("USD")
                .bookingId(1L)
                .build();
    }

    @Test
    void createPayment_success() throws PaymentNotCreatedException {
        when(paymentMapper.toDTO(any(CreatePaymentRequest.class))).thenReturn(paymentDTO);
        when(paymentService.createPayment(any(PaymentDTO.class))).thenReturn(paymentDTO);
        when(paymentMapper.toResponse(any(PaymentDTO.class))).thenReturn(paymentResponse);

        ResponseEntity<PaymentResponse> result = paymentController.createPayment(createRequest);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1L, result.getBody().getPaymentId());
    }

    @Test
    void findById_success() throws PaymentNotFoundException {
        when(paymentService.findById(1L)).thenReturn(paymentDTO);
        when(paymentMapper.toResponse(any(PaymentDTO.class))).thenReturn(paymentResponse);

        ResponseEntity<PaymentResponse> result = paymentController.findById(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void findById_notFound_throwsException() throws PaymentNotFoundException {
        when(paymentService.findById(99L)).thenThrow(new PaymentNotFoundException(MessageCode.PAYMENT_NOT_FOUND_BY_ID));

        assertThrows(PaymentNotFoundException.class, () -> paymentController.findById(99L));
    }

    @Test
    void findByBookingId_success() throws PaymentNotFoundException {
        when(paymentService.findByBookingId(1L)).thenReturn(List.of(paymentDTO));
        when(paymentMapper.toResponse(any(PaymentDTO.class))).thenReturn(paymentResponse);

        ResponseEntity<List<PaymentResponse>> result = paymentController.findByBookingId(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertFalse(result.getBody().isEmpty());
    }

    @Test
    void findByStatus_success() throws PaymentNotFoundException {
        when(paymentService.findByStatus(PaymentStatus.PENDING)).thenReturn(List.of(paymentDTO));
        when(paymentMapper.toResponse(any(PaymentDTO.class))).thenReturn(paymentResponse);

        ResponseEntity<List<PaymentResponse>> result = paymentController.findByStatus(PaymentStatus.PENDING);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertFalse(result.getBody().isEmpty());
    }

    @Test
    void processPayment_success() throws PaymentNotFoundException, PaymentAlreadyProcessedException, PaymentFailedException {
        when(paymentService.processPayment(1L)).thenReturn(paymentDTO);
        when(paymentMapper.toResponse(any(PaymentDTO.class))).thenReturn(paymentResponse);

        ResponseEntity<PaymentResponse> result = paymentController.processPayment(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void processPayment_alreadyProcessed_throwsException() throws PaymentNotFoundException, PaymentAlreadyProcessedException, PaymentFailedException {
        when(paymentService.processPayment(1L)).thenThrow(new PaymentAlreadyProcessedException(MessageCode.PAYMENT_ALREADY_PROCESSED));

        assertThrows(PaymentAlreadyProcessedException.class, () -> paymentController.processPayment(1L));
    }

    @Test
    void refundPayment_success() throws PaymentNotFoundException, PaymentAlreadyRefundedException, PaymentCannotBeRefundedException {
        when(paymentService.refundPayment(1L)).thenReturn(paymentDTO);
        when(paymentMapper.toResponse(any(PaymentDTO.class))).thenReturn(paymentResponse);

        ResponseEntity<PaymentResponse> result = paymentController.refundPayment(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void refundPayment_alreadyRefunded_throwsException() throws PaymentNotFoundException, PaymentAlreadyRefundedException, PaymentCannotBeRefundedException {
        when(paymentService.refundPayment(1L)).thenThrow(new PaymentAlreadyRefundedException(MessageCode.PAYMENT_ALREADY_REFUNDED));

        assertThrows(PaymentAlreadyRefundedException.class, () -> paymentController.refundPayment(1L));
    }

    @Test
    void updatePaymentStatus_success() throws PaymentNotFoundException, PaymentNotUpdatedException {
        when(paymentService.updatePaymentStatus(eq(1L), eq(PaymentStatus.FAILED))).thenReturn(paymentDTO);
        when(paymentMapper.toResponse(any(PaymentDTO.class))).thenReturn(paymentResponse);

        ResponseEntity<PaymentResponse> result = paymentController.updatePaymentStatus(1L, PaymentStatus.FAILED);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void updatePaymentStatus_notFound_throwsException() throws PaymentNotFoundException, PaymentNotUpdatedException {
        when(paymentService.updatePaymentStatus(eq(99L), any(PaymentStatus.class)))
                .thenThrow(new PaymentNotFoundException(MessageCode.PAYMENT_NOT_FOUND_BY_ID));

        assertThrows(PaymentNotFoundException.class, () -> paymentController.updatePaymentStatus(99L, PaymentStatus.FAILED));
    }
}
