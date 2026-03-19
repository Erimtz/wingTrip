package com.wingtrip.booking.event;
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
    private String paymentStatus;
    private LocalDateTime paymentDate;
}
