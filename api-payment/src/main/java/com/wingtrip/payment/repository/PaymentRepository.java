package com.wingtrip.payment.repository;

import com.wingtrip.payment.model.PaymentEntity;
import com.wingtrip.payment.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    Optional<PaymentEntity> findByBookingId(Long bookingId);
    List<PaymentEntity> findByPaymentStatus(PaymentStatus paymentStatus);
    List<PaymentEntity> findByBookingIdAndPaymentStatus(Long bookingId, PaymentStatus paymentStatus);
}
