package com.wingtrip.payment.controller;

import com.wingtrip.payment.controller.doc.PaymentControllerDoc;
import com.wingtrip.payment.controller.mapper.PaymentMapper;
import com.wingtrip.payment.controller.request.CreatePaymentRequest;
import com.wingtrip.payment.controller.response.PaymentResponse;
import com.wingtrip.payment.dto.PaymentDTO;
import com.wingtrip.payment.exception.*;
import com.wingtrip.payment.model.PaymentStatus;
import com.wingtrip.payment.service.impl.PaymentServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/payment")
public class PaymentController implements PaymentControllerDoc {

    private final PaymentMapper paymentMapper;
    private final PaymentServiceImpl paymentService;


    @Override
    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody CreatePaymentRequest request) throws PaymentNotCreatedException {
        log.info("Create new payment request received: {}", request);
        PaymentDTO paymentDTO = paymentMapper.toDTO(request);
        PaymentDTO createdPayment = paymentService.createPayment(paymentDTO);
        PaymentResponse response = paymentMapper.toResponse(createdPayment);
        log.info("Payment created successfully: {}", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @GetMapping("/find/{paymentId}")
    public ResponseEntity<PaymentResponse> findById(@PathVariable Long paymentId) throws PaymentNotFoundException {
        log.info("Finding payment with ID: {}", paymentId);
        PaymentDTO paymentDTO = paymentService.findById(paymentId);
        PaymentResponse response = paymentMapper.toResponse(paymentDTO);
        log.info("Payment found successfully with ID: {}", response);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/find-by-booking/{bookingId}")
    public ResponseEntity<List<PaymentResponse>> findByBookingId(@PathVariable Long bookingId) throws PaymentNotFoundException {
        log.info("Finding payment for booking ID: {}", bookingId);
        List<PaymentDTO> payments = paymentService.findByBookingId(bookingId);
        List<PaymentResponse> responses = payments.stream()
                .map(paymentMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Found {} payments for booking ID: {}", responses.size(), bookingId);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/find-by-status")
    public ResponseEntity<List<PaymentResponse>> findByStatus(@RequestParam PaymentStatus status) throws PaymentNotFoundException {
        log.info("Finding payments with status: {}", status);
        List<PaymentDTO> payments = paymentService.findByStatus(status);
        List<PaymentResponse> responses = payments.stream()
                .map(paymentMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Found {} payments with status: {}", responses.size(), status);
        return ResponseEntity.ok(responses);
    }

    @Override
    @PutMapping("/process/{paymentId}")
    public ResponseEntity<PaymentResponse> processPayment(@PathVariable Long paymentId) throws PaymentNotFoundException, PaymentAlreadyProcessedException, PaymentFailedException {
        log.info("Processing payment with ID: {}", paymentId);
        PaymentDTO processedPayment = paymentService.processPayment(paymentId);
        PaymentResponse response = paymentMapper.toResponse(processedPayment);
        log.info("Payment processed successfully with ID: {}", paymentId);
        return ResponseEntity.ok(response);
    }

    @Override
    @PutMapping("/refund/{paymentId}")
    public ResponseEntity<PaymentResponse> refundPayment(@PathVariable Long paymentId) throws PaymentNotFoundException, PaymentAlreadyRefundedException, PaymentCannotBeRefundedException {
        log.info("Refunding payment with ID: {}", paymentId);
        PaymentDTO paymentDTO = paymentService.refundPayment(paymentId);
        PaymentResponse response = paymentMapper.toResponse(paymentDTO);
        log.info("Payment refunded successfully with ID: {}", paymentId);
        return ResponseEntity.ok(response);
    }

    @Override
    @PutMapping("/update-status/{paymentId}")
    public ResponseEntity<PaymentResponse> updatePaymentStatus(@PathVariable Long paymentId, @RequestParam PaymentStatus status) throws PaymentNotFoundException, PaymentNotUpdatedException {
        log.info("Updating payment status with ID: {} to {}", paymentId, status);
        PaymentDTO paymentDTO = paymentService.updatePaymentStatus(paymentId, status);
        PaymentResponse response = paymentMapper.toResponse(paymentDTO);
        log.info("Payment status updated successfully with ID: {} to {}", paymentId, status);
        return ResponseEntity.ok(response);
    }
}
