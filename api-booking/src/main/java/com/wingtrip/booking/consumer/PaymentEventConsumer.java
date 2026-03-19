package com.wingtrip.booking.consumer;
import com.wingtrip.booking.config.RabbitMQConfig;
import com.wingtrip.booking.event.PaymentEventDTO;
import com.wingtrip.booking.exception.BookingNotUpdateException;
import com.wingtrip.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentEventConsumer {

    private final BookingService bookingService;

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_QUEUE)
    public void handlePaymentEvent(PaymentEventDTO event) {
        log.info("Received payment event for bookingId: {} with status: {}",
                event.getBookingId(), event.getPaymentStatus());

        try {
            if ("SUCCESS".equals(event.getPaymentStatus())) {
                bookingService.markAsPaid(event.getBookingId(), event.getPaymentId());
                log.info("Booking {} marked as PAID with paymentId: {}",
                        event.getBookingId(), event.getPaymentId());
            } else {
                log.warn("Payment event received with status: {} for bookingId: {}",
                        event.getPaymentStatus(), event.getBookingId());
            }
        } catch (BookingNotUpdateException e) {
            log.error("Error updating booking {} after payment event: {}",
                    event.getBookingId(), e.getMessage());
        }
    }
}
