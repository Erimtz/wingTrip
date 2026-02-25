package com.wingtrip.payment.service;


import com.wingtrip.payment.dto.PaymentDTO;
import com.wingtrip.payment.exception.*;
import com.wingtrip.payment.model.PaymentStatus;

import java.util.List;

public interface PaymentService {

    PaymentDTO createPayment(PaymentDTO paymentDTO) throws PaymentNotCreatedException;

    PaymentDTO findById(Long paymentId) throws PaymentNotFoundException;

    PaymentDTO findByBookingId(Long bookingId) throws PaymentNotFoundException;

    List<PaymentDTO> findByStatus(PaymentStatus status) throws PaymentNotFoundException;

    PaymentDTO processPayment(Long paymentId) throws PaymentNotFoundException, PaymentAlreadyProcessedException, PaymentFailedException;

    PaymentDTO refundPayment(Long paymentId) throws PaymentNotFoundException, PaymentAlreadyRefundedException, PaymentCannotBeRefundedException;

    PaymentDTO updatePaymentStatus(Long paymentId, PaymentStatus status) throws PaymentNotFoundException, PaymentNotUpdatedException;

}
