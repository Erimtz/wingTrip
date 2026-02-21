package com.wingtrip.dto;

import com.wingtrip.model.PaymentEntity;
import com.wingtrip.model.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {

    private Long paymentId;
    private String paymentType;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;
    private BigDecimal amount;
    private String currency;
    private Long bookingId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PaymentDTO(PaymentEntity entity) {
        this.paymentId = entity.getPaymentId();
        this.paymentType = entity.getPaymentType();
        this.paymentStatus = entity.getPaymentStatus();
        this.paymentDate = entity.getPaymentDate();
        this.amount = entity.getAmount();
        this.currency = entity.getCurrency();
        this.bookingId = entity.getBookingId();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
