package com.wingtrip.booking.dto;

import com.wingtrip.booking.model.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDTO {

    private Long bookingId;
    private String bookingReference;
    private LocalDateTime bookingDate;
    private LocalDate travelDate;
    private LocalDate returnDate;
    private int adultPassengers;
    private int childPassengers;
    private int infantPassengers;
    private int totalPassengers;
    private Double totalAmount;
    private String currency;
    private BookingStatus bookingStatus;
    private String specialRequests;
    private String bookingNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private LocalDateTime expiresAt;
    private Long userId;
    private Long flightId;
    private Long paymentId;
}
