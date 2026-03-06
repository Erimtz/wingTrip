package com.wingtrip.payment.service;

import com.wingtrip.payment.dto.PaymentDTO;
import com.wingtrip.payment.exception.*;
import com.wingtrip.payment.model.PaymentEntity;
import com.wingtrip.payment.model.PaymentStatus;
import com.wingtrip.payment.repository.PaymentRepository;
import com.wingtrip.payment.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private PaymentEntity paymentEntity;
    private PaymentDTO paymentDTO;

    @BeforeEach
    void setUp() {
        paymentEntity = PaymentEntity.builder()
                .paymentId(1L)
                .paymentType("CREDIT_CARD")
                .paymentStatus(PaymentStatus.PENDING)
                .paymentDate(LocalDateTime.now())
                .amount(new BigDecimal("450.00"))
                .currency("USD")
                .bookingId(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        paymentDTO = new PaymentDTO(paymentEntity);
    }

    // ===== createPayment =====

    @Test
    void createPayment_success() throws PaymentNotCreatedException {
        when(paymentRepository.save(any(PaymentEntity.class))).thenReturn(paymentEntity);

        PaymentDTO result = paymentService.createPayment(paymentDTO);

        assertNotNull(result);
        assertEquals(PaymentStatus.PENDING, result.getPaymentStatus());
        verify(paymentRepository, times(1)).save(any(PaymentEntity.class));
    }

    @Test
    void createPayment_invalidAmount_throwsException() {
        paymentDTO.setAmount(BigDecimal.ZERO);

        assertThrows(PaymentNotCreatedException.class, () -> paymentService.createPayment(paymentDTO));
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void createPayment_nullAmount_throwsException() {
        paymentDTO.setAmount(null);

        assertThrows(PaymentNotCreatedException.class, () -> paymentService.createPayment(paymentDTO));
        verify(paymentRepository, never()).save(any());
    }

    // ===== findById =====

    @Test
    void findById_success() throws PaymentNotFoundException {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(paymentEntity));

        PaymentDTO result = paymentService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getPaymentId());
    }

    @Test
    void findById_nullId_throwsException() {
        assertThrows(PaymentNotFoundException.class, () -> paymentService.findById(null));
        verify(paymentRepository, never()).findById(any());
    }

    @Test
    void findById_notFound_throwsException() {
        when(paymentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PaymentNotFoundException.class, () -> paymentService.findById(99L));
    }

    // ===== findByBookingId =====

    @Test
    void findByBookingId_success() throws PaymentNotFoundException {
        when(paymentRepository.findByBookingId(1L)).thenReturn(List.of(paymentEntity));

        List<PaymentDTO> result = paymentService.findByBookingId(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void findByBookingId_nullId_throwsException() {
        assertThrows(PaymentNotFoundException.class, () -> paymentService.findByBookingId(null));
        verify(paymentRepository, never()).findByBookingId(any());
    }

    @Test
    void findByBookingId_notFound_throwsException() {
        when(paymentRepository.findByBookingId(99L)).thenReturn(List.of());

        assertThrows(PaymentNotFoundException.class, () -> paymentService.findByBookingId(99L));
    }

    // ===== findByStatus =====

    @Test
    void findByStatus_success() throws PaymentNotFoundException {
        when(paymentRepository.findByPaymentStatus(PaymentStatus.PENDING)).thenReturn(List.of(paymentEntity));

        List<PaymentDTO> result = paymentService.findByStatus(PaymentStatus.PENDING);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void findByStatus_notFound_throwsException() {
        when(paymentRepository.findByPaymentStatus(PaymentStatus.REFUNDED)).thenReturn(List.of());

        assertThrows(PaymentNotFoundException.class, () -> paymentService.findByStatus(PaymentStatus.REFUNDED));
    }

    // ===== processPayment =====

    @Test
    void processPayment_success() throws PaymentNotFoundException, PaymentAlreadyProcessedException, PaymentFailedException {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(paymentEntity));
        when(paymentRepository.save(any(PaymentEntity.class))).thenReturn(paymentEntity);

        PaymentDTO result = paymentService.processPayment(1L);

        assertNotNull(result);
        verify(paymentRepository, times(1)).save(any(PaymentEntity.class));
    }

    @Test
    void processPayment_alreadyProcessed_throwsException() {
        paymentEntity.setPaymentStatus(PaymentStatus.SUCCESS);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(paymentEntity));

        assertThrows(PaymentAlreadyProcessedException.class, () -> paymentService.processPayment(1L));
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void processPayment_notFound_throwsException() {
        when(paymentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PaymentNotFoundException.class, () -> paymentService.processPayment(99L));
    }

    // ===== refundPayment =====

    @Test
    void refundPayment_success() throws PaymentNotFoundException, PaymentAlreadyRefundedException, PaymentCannotBeRefundedException {
        paymentEntity.setPaymentStatus(PaymentStatus.SUCCESS);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(paymentEntity));
        when(paymentRepository.save(any(PaymentEntity.class))).thenReturn(paymentEntity);

        PaymentDTO result = paymentService.refundPayment(1L);

        assertNotNull(result);
        verify(paymentRepository, times(1)).save(any(PaymentEntity.class));
    }

    @Test
    void refundPayment_alreadyRefunded_throwsException() {
        paymentEntity.setPaymentStatus(PaymentStatus.REFUNDED);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(paymentEntity));

        assertThrows(PaymentAlreadyRefundedException.class, () -> paymentService.refundPayment(1L));
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void refundPayment_cannotBeRefunded_throwsException() {
        paymentEntity.setPaymentStatus(PaymentStatus.PENDING);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(paymentEntity));

        assertThrows(PaymentCannotBeRefundedException.class, () -> paymentService.refundPayment(1L));
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void refundPayment_notFound_throwsException() {
        when(paymentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PaymentNotFoundException.class, () -> paymentService.refundPayment(99L));
    }

    // ===== updatePaymentStatus =====

    @Test
    void updatePaymentStatus_success() throws PaymentNotFoundException, PaymentNotUpdatedException {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(paymentEntity));
        when(paymentRepository.save(any(PaymentEntity.class))).thenReturn(paymentEntity);

        PaymentDTO result = paymentService.updatePaymentStatus(1L, PaymentStatus.FAILED);

        assertNotNull(result);
        verify(paymentRepository, times(1)).save(any(PaymentEntity.class));
    }

    @Test
    void updatePaymentStatus_nullId_throwsException() {
        assertThrows(PaymentNotUpdatedException.class, () -> paymentService.updatePaymentStatus(null, PaymentStatus.FAILED));
        verify(paymentRepository, never()).findById(any());
    }

    @Test
    void updatePaymentStatus_notFound_throwsException() {
        when(paymentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PaymentNotFoundException.class, () -> paymentService.updatePaymentStatus(99L, PaymentStatus.FAILED));
    }
}
