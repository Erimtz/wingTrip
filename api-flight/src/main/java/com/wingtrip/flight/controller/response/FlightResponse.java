package com.wingtrip.flight.controller.response;

import com.wingtrip.flight.model.ServiceClass;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Response with flight data")
public class FlightResponse {

    @Schema(description = "Unique flight ID", example = "507f1f77bcf86cd799439011")
    private String id;

    @Schema(description = "Unique flight number", example = "AA1234")
    private String flightNumber;

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

    @Schema(description = "Flight price", example = "250.50")
    private BigDecimal price;

    @Schema(description = "Currency code", example = "USD")
    private String currency;

    @Schema(description = "Seats currently available", example = "150")
    private int availableSeats;

    @Schema(description = "Total number of seats", example = "200")
    private int totalSeats;

    @Schema(description = "Flight status", example = "ACTIVE")
    private String status;

    @Schema(description = "Indicates if the flight is direct", example = "true")
    private boolean directFlight;

    @Schema(description = "Number of stops", example = "0")
    private int stops;

    @Schema(description = "Class of service", example = "ECONOMY")
    private ServiceClass serviceClass;
}

