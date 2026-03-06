package com.wingtrip.booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "booking")
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private long bookingId;

    @Column(name = "booking_reference", nullable = false)
    private String bookingReference;

    @Column(name = "booking_date", nullable = false)
    private LocalDateTime bookingDate;

    @Column(name = "travel_date", nullable = false)
    private LocalDate travelDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "adult_passengers")
    private int adultPassengers;

    @Column(name = "child_passengers")
    private int childPassengers;

    @Column(name = "infant_passengers")
    private int infantPassengers;

    @Column(name = "total_passengers", insertable = false, updatable = false)
    private int totalPassengers;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "currency")
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status")
    private BookingStatus bookingStatus;

    @Column(name = "special_requests")
    private String specialRequests;

    @Column(name = "booking_notes")
    private String bookingNotes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updateAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "flight_id")
    private Long flightId;

    @Column(name = "payment_id")
    private Long paymentId;
}
