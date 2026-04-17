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
@Schema(description = "Request to update an existing flight")
public class UpdateFlightRequest {

    @Schema(description = "Airline name", example = "American Airlines")
    private String airline;

    @Schema(description = "Origin airport code", example = "JFK")
    private String originAirport;

    @Schema(description = "Destination airport code", example = "LAX")
    private String destinationAirport;

    @Schema(description = "Date and time of departure", example = "2026-08-15T10:30:00")
    private LocalDateTime departureTime;

    @Schema(description = "Date and time of arrival", example = "2026-08-15T13:30:00")
    private LocalDateTime arrivalTime;

    @DecimalMin(value = "0.0", inclusive = false, message = "The price must be greater than 0")
    @Schema(description = "Flight price", example = "250.50")
    private BigDecimal price;

    @Schema(description = "Currency code", example = "USD")
    private String currency;

    @Min(value = 0, message = "The available seats cannot be negative.")
    @Schema(description = "Seats currently available", example = "150")
    private Integer availableSeats;

    @Min(value = 1, message = "The total number of seats must be at least 1")
    @Schema(description = "Total number of seats available", example = "200")
    private Integer totalSeats;

    @Schema(description = "Indicates if the flight is direct", example = "true")
    private Boolean directFlight;

    @Min(value = 0, message = "The number of stops cannot be negative")
    @Schema(description = "Number of stops", example = "0")
    private Integer stops;

    @Schema(description = "Class of service", example = "ECONOMY")
    private ServiceClass serviceClass;
}

