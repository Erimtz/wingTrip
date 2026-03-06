package com.wingtrip.booking.controller.response;

import com.wingtrip.booking.model.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingListResponse {

    private Long bookingId;
    private String bookingReference;
    private LocalDateTime bookingDate;
    private LocalDate travelDate;
    private LocalDate returnDate;
    private int totalPassengers;
    private BigDecimal totalAmount;
    private String currency;
    private BookingStatus bookingStatus;

    // Información útil para listar reservas
    private String flightNumber;  // Útil para mostrar en UI
    private String origin;
    private String destination;
}
