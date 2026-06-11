package com.wingtrip.flight.details.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request to create flight details for a specific flight")
public class CreateFlightDetailsRequest {

    @NotBlank(message = "Aircraft model is required")
    @Schema(description = "Model of the aircraft", example = "Boeing 737")
    private String aircraftModel;

    @NotBlank(message = "Gate number is required")
    @Schema(description = "Gate number for departure", example = "A12")
    private String gateNumber;

    @NotBlank(message = "Terminal origin is required")
    @Schema(description = "Terminal for departure", example = "Terminal 1")
    private String terminalOrigin;

    @NotBlank(message = "Terminal destination is required")
    @Schema(description = "Terminal for arrival", example = "Terminal 2")
    private String terminalDestination;

    @Schema(description = "Indicates if the flight includes cabin baggage", example = "true")
    private boolean includesCabinBaggage;

    @Schema(description = "Indicates if the flight includes checked baggage", example = "true")
    private boolean includesCheckedBaggage;

    @PositiveOrZero(message = "Extra baggage price must be zero or positive")
    @Schema(description = "Price for extra baggage", example = "50.00")
    private BigDecimal extraBaggagePrice;

    @Schema(description = "Seat selection policy", example = "Free seat selection for all passengers")
    private String seatSelectionPolicy;

    @Schema(description = "Meal service details", example = "Vegetarian meal included")
    private String mealService;

    @Schema(description = "Wi-Fi availability on the flight", example = "true")
    private boolean availableWifiConnection;

    @Schema(description = "In-flight entertainment options", example = "Movies, TV shows, music")
    private String entertainmentOptions;

    @NotBlank(message = "Flight ID is required")
    @Schema(description = "ID of the flight to which these details belong", example = "123e4567-e89b-12d3-a456-426614174000")
    private String flightId;
}
