package mapper;

import com.wingtrip.payment.controller.mapper.PaymentMapper;
import com.wingtrip.payment.controller.request.CreatePaymentRequest;
import com.wingtrip.payment.controller.response.PaymentResponse;
import com.wingtrip.payment.dto.PaymentDTO;
import com.wingtrip.payment.model.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
public class PaymentMapperTest {

    private final PaymentMapper paymentMapper = Mappers.getMapper(PaymentMapper.class);

    @Test
    void toDTO_success() {
        CreatePaymentRequest request = CreatePaymentRequest.builder()
                .paymentType("CREDIT_CARD")
                .amount(new BigDecimal("450.00"))
                .currency("USD")
                .bookingId(1L)
                .build();

        PaymentDTO result = paymentMapper.toDTO(request);

        assertNotNull(result);
        assertEquals("CREDIT_CARD", result.getPaymentType());
        assertEquals(new BigDecimal("450.00"), result.getAmount());
        assertEquals("USD", result.getCurrency());
        assertEquals(1L, result.getBookingId());
    }

    @Test
    void toDTO_nullRequest_returnsNull() {
        PaymentDTO result = paymentMapper.toDTO(null);
        assertNull(result);
    }

    @Test
    void toResponse_success() {
        PaymentDTO paymentDTO = PaymentDTO.builder()
                .paymentId(1L)
                .paymentType("CREDIT_CARD")
                .paymentStatus(PaymentStatus.PENDING)
                .amount(new BigDecimal("450.00"))
                .currency("USD")
                .bookingId(1L)
                .build();

        PaymentResponse result = paymentMapper.toResponse(paymentDTO);

        assertNotNull(result);
        assertEquals(1L, result.getPaymentId());
        assertEquals("CREDIT_CARD", result.getPaymentType());
        assertEquals(PaymentStatus.PENDING, result.getPaymentStatus());
        assertEquals(new BigDecimal("450.00"), result.getAmount());
        assertEquals("USD", result.getCurrency());
        assertEquals(1L, result.getBookingId());
    }

    @Test
    void toResponse_nullDTO_returnsNull() {
        PaymentResponse result = paymentMapper.toResponse(null);
        assertNull(result);
    }
}
