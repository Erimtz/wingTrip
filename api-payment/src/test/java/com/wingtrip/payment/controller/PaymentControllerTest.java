package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wingtrip.payment.controller.PaymentController;
import com.wingtrip.payment.controller.mapper.PaymentMapper;
import com.wingtrip.payment.controller.request.CreatePaymentRequest;
import com.wingtrip.payment.controller.response.PaymentResponse;
import com.wingtrip.payment.dto.PaymentDTO;
import com.wingtrip.payment.exception.*;
import com.wingtrip.payment.model.PaymentStatus;
import com.wingtrip.payment.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentServiceImpl paymentService;

    @MockBean
    private PaymentMapper paymentMapper;

    private ObjectMapper objectMapper;
    private PaymentDTO paymentDTO;
    private PaymentResponse paymentResponse;
    private CreatePaymentRequest createRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        paymentDTO = PaymentDTO.builder()
                .paymentId(1L)
                .paymentType("CREDIT_CARD")
                .paymentStatus(PaymentStatus.PENDING)
                .paymentDate(LocalDateTime.now())
                .amount(new BigDecimal("450.00"))
                .currency("USD")
                .bookingId(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        paymentResponse = PaymentResponse.builder()
                .paymentId(1L)
                .paymentType("CREDIT_CARD")
                .paymentStatus(PaymentStatus.PENDING)
                .amount(new BigDecimal("450.00"))
                .currency("USD")
                .bookingId(1L)
                .build();

        createRequest = CreatePaymentRequest.builder()
                .paymentType("CREDIT_CARD")
                .amount(new BigDecimal("450.00"))
                .currency("USD")
                .bookingId(1L)
                .build();
    }

    @Test
    void createPayment_success() throws Exception {
        when(paymentMapper.toDTO(any(CreatePaymentRequest.class))).thenReturn(paymentDTO);
        when(paymentService.createPayment(any(PaymentDTO.class))).thenReturn(paymentDTO);
        when(paymentMapper.toResponse(any(PaymentDTO.class))).thenReturn(paymentResponse);

        mockMvc.perform(post("/api/v1/payment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paymentId").value(1L))
                .andExpect(jsonPath("$.paymentType").value("CREDIT_CARD"))
                .andExpect(jsonPath("$.paymentStatus").value("PENDING"));
    }

    @Test
    void createPayment_invalidRequest_returnsBadRequest() throws Exception {
        CreatePaymentRequest invalidRequest = new CreatePaymentRequest();

        mockMvc.perform(post("/api/v1/payment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findById_success() throws Exception {
        when(paymentService.findById(1L)).thenReturn(paymentDTO);
        when(paymentMapper.toResponse(any(PaymentDTO.class))).thenReturn(paymentResponse);

        mockMvc.perform(get("/api/v1/payment/find/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value(1L));
    }

    @Test
    void findById_notFound_returns400() throws Exception {
        when(paymentService.findById(99L)).thenThrow(new PaymentNotFoundException(MessageCode.PAYMENT_NOT_FOUND_BY_ID));

        mockMvc.perform(get("/api/v1/payment/find/99"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findByBookingId_success() throws Exception {
        when(paymentService.findByBookingId(1L)).thenReturn(List.of(paymentDTO));
        when(paymentMapper.toResponse(any(PaymentDTO.class))).thenReturn(paymentResponse);

        mockMvc.perform(get("/api/v1/payment/find-by-booking/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].paymentId").value(1L));
    }

    @Test
    void findByStatus_success() throws Exception {
        when(paymentService.findByStatus(PaymentStatus.PENDING)).thenReturn(List.of(paymentDTO));
        when(paymentMapper.toResponse(any(PaymentDTO.class))).thenReturn(paymentResponse);

        mockMvc.perform(get("/api/v1/payment/find-by-status")
                        .param("status", "PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].paymentId").value(1L));
    }

    @Test
    void processPayment_success() throws Exception {
        paymentResponse.setPaymentStatus(PaymentStatus.SUCCESS);
        when(paymentService.processPayment(1L)).thenReturn(paymentDTO);
        when(paymentMapper.toResponse(any(PaymentDTO.class))).thenReturn(paymentResponse);

        mockMvc.perform(put("/api/v1/payment/process/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value(1L));
    }

    @Test
    void processPayment_alreadyProcessed_returns400() throws Exception {
        when(paymentService.processPayment(1L)).thenThrow(new PaymentAlreadyProcessedException(MessageCode.PAYMENT_ALREADY_PROCESSED));

        mockMvc.perform(put("/api/v1/payment/process/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void refundPayment_success() throws Exception {
        paymentResponse.setPaymentStatus(PaymentStatus.REFUNDED);
        when(paymentService.refundPayment(1L)).thenReturn(paymentDTO);
        when(paymentMapper.toResponse(any(PaymentDTO.class))).thenReturn(paymentResponse);

        mockMvc.perform(put("/api/v1/payment/refund/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value(1L));
    }

    @Test
    void refundPayment_alreadyRefunded_returns400() throws Exception {
        when(paymentService.refundPayment(1L)).thenThrow(new PaymentAlreadyRefundedException(MessageCode.PAYMENT_ALREADY_REFUNDED));

        mockMvc.perform(put("/api/v1/payment/refund/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatePaymentStatus_success() throws Exception {
        paymentResponse.setPaymentStatus(PaymentStatus.FAILED);
        when(paymentService.updatePaymentStatus(eq(1L), eq(PaymentStatus.FAILED))).thenReturn(paymentDTO);
        when(paymentMapper.toResponse(any(PaymentDTO.class))).thenReturn(paymentResponse);

        mockMvc.perform(put("/api/v1/payment/update-status/1")
                        .param("status", "FAILED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value(1L));
    }

    @Test
    void updatePaymentStatus_notFound_returns400() throws Exception {
        when(paymentService.updatePaymentStatus(eq(99L), any(PaymentStatus.class)))
                .thenThrow(new PaymentNotFoundException(MessageCode.PAYMENT_NOT_FOUND_BY_ID));

        mockMvc.perform(put("/api/v1/payment/update-status/99")
                        .param("status", "FAILED"))
                .andExpect(status().isBadRequest());
    }
}
