package com.wingtrip.booking.dto;

import com.wingtrip.booking.model.BookingEntity;
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
    private BigDecimal totalAmount;
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

    public BookingDTO(BookingEntity bookingEntity) {
        this.bookingId = bookingEntity.getBookingId();
        this.bookingReference = bookingEntity.getBookingReference();
        this.bookingDate = bookingEntity.getBookingDate();
        this.travelDate = bookingEntity.getTravelDate();
        this.returnDate = bookingEntity.getReturnDate();
        this.adultPassengers = bookingEntity.getAdultPassengers();
        this.childPassengers = bookingEntity.getChildPassengers();
        this.infantPassengers = bookingEntity.getInfantPassengers();
        this.totalPassengers = bookingEntity.getTotalPassengers();
        this.totalAmount = bookingEntity.getTotalAmount();
        this.currency = bookingEntity.getCurrency();
        this.bookingStatus = bookingEntity.getBookingStatus();
        this.specialRequests = bookingEntity.getSpecialRequests();
        this.bookingNotes = bookingEntity.getBookingNotes();
        this.createdAt = bookingEntity.getCreatedAt();
        this.updateAt = bookingEntity.getUpdateAt();
        this.expiresAt = bookingEntity.getExpiresAt();
        this.userId = bookingEntity.getUserId();
        this.flightId = bookingEntity.getFlightId();
        this.paymentId = bookingEntity.getPaymentId();
    }
}
