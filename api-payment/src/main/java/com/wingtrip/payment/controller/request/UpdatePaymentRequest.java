package com.wingtrip.payment.controller.request;

import com.wingtrip.payment.model.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePaymentRequest {

    @NotNull(message = "Payment status is required")
    private PaymentStatus paymentStatus;
}
