package com.wingtrip.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wingtrip.booking.controller.mapper.BookingMapper;
import com.wingtrip.booking.controller.request.CreateBookingRequest;
import com.wingtrip.booking.controller.response.BookingResponse;
import com.wingtrip.booking.dto.BookingDTO;
import com.wingtrip.booking.exception.BookingNotFoundByIdException;
import com.wingtrip.booking.exception.MessageCode;
import com.wingtrip.booking.model.BookingStatus;
import com.wingtrip.booking.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class, properties = {
        "spring.cloud.config.enabled=false",
        "spring.cloud.discovery.enabled=false",
        "eureka.client.enabled=false",
        "spring.autoconfigure.exclude=org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration"
})
@AutoConfigureMockMvc(addFilters = false)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingServiceImpl bookingService;

    @MockBean
    private BookingMapper bookingMapper;

    private BookingDTO commonDto;
    private BookingResponse commonResponse;

    @BeforeEach
    void setUp() {
        commonDto = BookingDTO.builder()
                .bookingId(1L)
                .bookingReference("REF-123")
                .bookingStatus(BookingStatus.PENDING)
                .build();

        commonResponse = BookingResponse.builder()
                .bookingId(1L)
                .bookingReference("REF-123")
                .bookingStatus(BookingStatus.PENDING)
                .build();
    }

    @Test
    void testCreateBooking_Success() throws Exception {
        CreateBookingRequest request = new CreateBookingRequest();
        request.setUserId(1L);
        request.setFlightId(100L);
        request.setTravelDate(LocalDate.now().plusDays(1));
        request.setAdultPassengers(1);
        request.setChildPassengers(0);
        request.setInfantPassengers(0);

        when(bookingMapper.toDTO(any(CreateBookingRequest.class))).thenReturn(commonDto);
        when(bookingService.createBooking(any(BookingDTO.class))).thenReturn(commonDto);
        when(bookingMapper.toResponse(any(BookingDTO.class))).thenReturn(commonResponse);

        mockMvc.perform(post("/api/v1/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookingId").value(1L));
    }

    @Test
    void testFindById_Success() throws Exception {
        when(bookingService.findById(1L)).thenReturn(commonDto);
        when(bookingMapper.toResponse(any())).thenReturn(commonResponse);

        mockMvc.perform(get("/api/v1/booking/find/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(1L));
    }

    @Test
    void testFindById_NotFound() throws Exception {
        when(bookingService.findById(99L))
                .thenThrow(new BookingNotFoundByIdException(MessageCode.BOOKING_NOT_FOUND_BY_ID));

        mockMvc.perform(get("/api/v1/booking/find/99"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testConfirmBooking_Success() throws Exception {
        when(bookingService.findById(anyLong())).thenReturn(commonDto);
        when(bookingMapper.toResponse(any())).thenReturn(commonResponse);

        mockMvc.perform(put("/api/v1/booking/confirm/1")
                        .param("paymentId", "555"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindAllExpiredBookings_Success() throws Exception {
        when(bookingService.findAllExpiredBookings()).thenReturn(Collections.singletonList(commonDto));
        when(bookingMapper.toResponse(any())).thenReturn(commonResponse);

        mockMvc.perform(get("/api/v1/booking/expired"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testFindByReference_Success() throws Exception {
        when(bookingService.findByReference("REF-123")).thenReturn(commonDto);
        when(bookingMapper.toResponse(any())).thenReturn(commonResponse);

        mockMvc.perform(get("/api/v1/booking/find-by-reference/REF-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingReference").value("REF-123"));
    }

    @Test
    void testCancelBooking_Success() throws Exception {
        // No necesitamos mockear el void de cancelBooking, Mockito lo ignora por defecto
        when(bookingService.findById(1L)).thenReturn(commonDto);
        when(bookingMapper.toResponse(any())).thenReturn(commonResponse);

        mockMvc.perform(put("/api/v1/booking/cancel/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindBookingsByUserId_Success() throws Exception {
        when(bookingService.findBookingsByUserId(1L)).thenReturn(Collections.singletonList(commonDto));
        when(bookingMapper.toResponse(any())).thenReturn(commonResponse);

        mockMvc.perform(get("/api/v1/booking/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookingId").value(1L));
    }

    @Test
    void testFindBookingsByUserIdAndStatus_Success() throws Exception {
        when(bookingService.findBookingsByUserIdAndStatus(1L, BookingStatus.PENDING))
                .thenReturn(Collections.singletonList(commonDto));
        when(bookingMapper.toResponse(any())).thenReturn(commonResponse);

        mockMvc.perform(get("/api/v1/booking/user/1/status")
                        .param("status", "PENDING"))
                .andExpect(status().isOk());
    }

    @Test
    void testMarkAsPaid_Success() throws Exception {
        when(bookingService.findById(1L)).thenReturn(commonDto);
        when(bookingMapper.toResponse(any())).thenReturn(commonResponse);

        mockMvc.perform(put("/api/v1/booking/mark-paid/1")
                        .param("paymentId", "999"))
                .andExpect(status().isOk());
    }

    @Test
    void testIsBookingExpired_Success() throws Exception {
        when(bookingService.isBookingExpired(1L)).thenReturn(true);
        when(bookingService.findById(1L)).thenReturn(commonDto);
        when(bookingMapper.toResponse(any())).thenReturn(commonResponse);

        mockMvc.perform(get("/api/v1/booking/is-expired/1"))
                .andExpect(status().isOk());
    }
}

