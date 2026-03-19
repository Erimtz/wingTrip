package com.wingtrip.payment.event;
import com.wingtrip.payment.model.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentEventDTO {

    private Long paymentId;
    private Long bookingId;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;
}
