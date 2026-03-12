package com.wingtrip.payment.service.impl;

import com.wingtrip.payment.dto.PaymentDTO;
import com.wingtrip.payment.exception.*;
import com.wingtrip.payment.model.PaymentEntity;
import com.wingtrip.payment.model.PaymentStatus;
import com.wingtrip.payment.repository.PaymentRepository;
import com.wingtrip.payment.service.PaymentService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;


    @Override
    public PaymentDTO createPayment(PaymentDTO paymentDTO) throws PaymentNotCreatedException {
        if (paymentDTO.getAmount() == null || paymentDTO.getAmount().signum() <= 0) {
            throw new PaymentNotCreatedException(MessageCode.INVALID_PAYMENT_AMOUNT);
        }

        try {
            PaymentEntity paymentEntity = PaymentEntity.builder()
                    .paymentType(paymentDTO.getPaymentType())
                    .paymentStatus(PaymentStatus.PENDING)
                    .paymentDate(LocalDateTime.now())
                    .amount(paymentDTO.getAmount())
                    .currency(paymentDTO.getCurrency())
                    .bookingId(paymentDTO.getBookingId())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            PaymentEntity saved = paymentRepository.save(paymentEntity);
            return new PaymentDTO(saved);
        } catch (Exception e) {
            throw new PaymentNotCreatedException(MessageCode.PAYMENT_NOT_CREATE);
        }
    }

    @Override
    public PaymentDTO findById(Long paymentId) throws PaymentNotFoundException {
        if (paymentId == null) {
            throw new PaymentNotFoundException(MessageCode.PAYMENT_NOT_FOUND_BY_ID);
        }

        Optional<PaymentEntity> optional = paymentRepository.findById(paymentId);
        if (optional.isEmpty()) {
            throw new PaymentNotFoundException(MessageCode.PAYMENT_NOT_FOUND_BY_ID);
        }
        return new PaymentDTO(optional.get());
    }

    @Override
    public List<PaymentDTO> findByBookingId(Long bookingId) throws PaymentNotFoundException {
        if (bookingId == null) {
            throw new PaymentNotFoundException(MessageCode.PAYMENT_NOT_FOUND_BY_BOOKING);
        }

        List<PaymentEntity> payments = paymentRepository.findByBookingId(bookingId);
        if (payments.isEmpty()) {
            throw new PaymentNotFoundException(MessageCode.PAYMENT_NOT_FOUND_BY_BOOKING);
        }
        return payments.stream()
                .map(PaymentDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDTO> findByStatus(PaymentStatus status) throws PaymentNotFoundException {
        List<PaymentEntity> payments = paymentRepository.findByPaymentStatus(status);

        if (payments.isEmpty()) {
            throw new PaymentNotFoundException(MessageCode.PAYMENT_NOT_FOUND);
        }

        return payments.stream()
                .map(PaymentDTO::new)
                .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "paymentService", fallbackMethod = "processPaymentFallback")
    @Retry(name = "paymentService")
    @RateLimiter(name = "paymentService")
    @Override
    public PaymentDTO processPayment(Long paymentId) throws PaymentNotFoundException, PaymentAlreadyProcessedException, PaymentFailedException {
        PaymentEntity entity = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(MessageCode.PAYMENT_NOT_FOUND_BY_ID));

        if (entity.getPaymentStatus() == PaymentStatus.SUCCESS) {
            throw new PaymentAlreadyProcessedException(MessageCode.PAYMENT_ALREADY_PROCESSED);
        }

        try {
            entity.setPaymentStatus(PaymentStatus.SUCCESS);
            entity.setPaymentDate(LocalDateTime.now());
            entity.setUpdatedAt(LocalDateTime.now());
            PaymentEntity updated = paymentRepository.save(entity);
            return new PaymentDTO(updated);
        } catch (Exception e) {
            entity.setPaymentStatus(PaymentStatus.FAILED);
            entity.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(entity);
            throw new PaymentFailedException(MessageCode.PAYMENT_FAILED);
        }
    }

    public PaymentDTO processPaymentFallback(Long paymentId, Exception ex) throws PaymentFailedException {
        throw new PaymentFailedException(MessageCode.PAYMENT_FAILED);
    }

    @CircuitBreaker(name = "paymentService", fallbackMethod = "refundPaymentFallback")
    @Retry(name = "paymentService")
    @RateLimiter(name = "paymentService")
    @Override
    public PaymentDTO refundPayment(Long paymentId) throws PaymentNotFoundException, PaymentAlreadyRefundedException, PaymentCannotBeRefundedException {
        PaymentEntity entity = paymentRepository.findById(paymentId)
                .orElseThrow(() ->  new PaymentNotFoundException(MessageCode.PAYMENT_NOT_FOUND_BY_ID));

        if (entity.getPaymentStatus() == PaymentStatus.REFUNDED) {
            throw new PaymentAlreadyRefundedException(MessageCode.PAYMENT_ALREADY_REFUNDED);
        }

        if (entity.getPaymentStatus() != PaymentStatus.SUCCESS) {
            throw new PaymentCannotBeRefundedException(MessageCode.PAYMENT_CANNOT_BE_REFUNDED);
        }

        entity.setPaymentStatus(PaymentStatus.REFUNDED);
        entity.setUpdatedAt(LocalDateTime.now());
        PaymentEntity updated = paymentRepository.save(entity);
        return new PaymentDTO(updated);
    }

    public PaymentDTO refundPaymentFallback(Long paymentId, Exception ex) throws PaymentCannotBeRefundedException {
        throw new PaymentCannotBeRefundedException(MessageCode.PAYMENT_CANNOT_BE_REFUNDED);
    }

    @Override
    public PaymentDTO updatePaymentStatus(Long paymentId, PaymentStatus status) throws PaymentNotFoundException, PaymentNotUpdatedException {
        if (paymentId == null) {
            throw new PaymentNotUpdatedException(MessageCode.PAYMENT_NOT_UPDATE);
        }

        PaymentEntity entity = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(MessageCode.PAYMENT_NOT_FOUND_BY_ID));

        entity.setPaymentStatus(status);
        entity.setUpdatedAt(LocalDateTime.now());
        PaymentEntity updated = paymentRepository.save(entity);
        return new PaymentDTO(updated);
    }
}
