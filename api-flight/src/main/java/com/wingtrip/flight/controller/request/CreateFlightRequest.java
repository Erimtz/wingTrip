package com.wingtrip.flight.controller.request;

import com.wingtrip.flight.model.ServiceClass;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request to create a new flight")
public class CreateFlightRequest {

    @NotBlank(message = "The flight number is required")
    @Schema(description = "Unique flight number", example = "AA1234")
    private String flightNumber;

    @NotBlank(message = "The airline is required")
    @Schema(description = "Airline name", example = "American Airlines")
    private String airline;

    @NotBlank(message = "The airport of origin is required")
    @Schema(description = "Origin airport code", example = "JFK")
    private String originAirport;

    @NotBlank(message = "The destination airport is required")
    @Schema(description = "Destination airport code", example = "LAX")
    private String destinationAirport;

    @NotNull(message = "The departure date is required")
    @Schema(description = "Date and time of departure", example = "2026-08-15T10:30:00")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival date is required")
    @Schema(description = "Date and time of arrival", example = "2026-08-15T13:30:00")
    private LocalDateTime arrivalTime;

    @NotNull(message = "The price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "The price must be greater than 0")
    @Schema(description = "Flight price", example = "250.50")
    private BigDecimal price;

    @NotBlank(message = "Currency is required")
    @Schema(description = "Currency code", example = "USD")
    private String currency;

    @Min(value = 1, message = "The total number of seats must be at least 1")
    @Schema(description = "Total number of seats available", example = "200")
    private int totalSeats;

    @Min(value = 1, message = "There must be at least 1 seat available")
    @Schema(description = "Seats currently available", example = "150")
    private int availableSeats;

    @Schema(description = "Indicates if the flight is direct", example = "true")
    private boolean directFlight;

    @Min(value = 0, message = "The number of stops cannot be negative")
    @Schema(description = "Number of stops", example = "0")
    private int stops;

    @NotNull(message = "The class of service is required")
    @Schema(description = "Class of service", example = "ECONOMY")
    private ServiceClass serviceClass;
}

