package com.wingtrip.payment.controller.response;

import com.wingtrip.payment.model.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Long paymentId;
    private String paymentType;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;
    private BigDecimal amount;
    private String currency;
    private Long bookingId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
