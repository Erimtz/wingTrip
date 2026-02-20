package com.wingtrip.booking.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateBookingRequest {

    private Long userId;

    private Long flightId;

    @NotNull(message = "travelDate is required")
    private LocalDate travelDate;

    private LocalDate returnDate; // Opcional (null si es one-way)

    @Min(1)
    private int adultPassengers;

    private int childPassengers;

    private int infantPassengers;

    private Double totalAmount;

    private String currency; // Opcional, default "USD"

    private String specialRequests; // Opcional

}
