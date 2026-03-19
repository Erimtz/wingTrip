package com.wingtrip.payment.publisher;

import com.wingtrip.payment.config.RabbitMQConfig;
import com.wingtrip.payment.event.PaymentEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishPaymentProcessed(PaymentEventDTO event) {
        log.info("Publishing payment event for bookingId: {} with status: {}",
                event.getBookingId(), event.getPaymentStatus());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PAYMENT_EXCHANGE,
                RabbitMQConfig.PAYMENT_ROUTING_KEY,
                event
        );

        log.info("Payment event published successfully for bookingId: {}", event.getBookingId());
    }
}
