package com.wingtrip.booking.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wingtrip.booking.model.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingResponse {

    private Long bookingId;
    private String bookingReference;
    private LocalDateTime bookingDate;
    private BookingStatus bookingStatus;
    private LocalDate travelDate;
    private LocalDate returnDate;
    private int adultPassengers;
    private int childPassengers;
    private int infantPassengers;
    private int totalPassengers;
    private BigDecimal totalAmount;
    private String currency;
    private String specialRequests;
    private String bookingNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expiresAt;
    private Long userId;
    private Long flightId;
    private Long paymentId;
    private String userEmail;
    private String flightNumber;
    private String origin;
    private String destination;
}
