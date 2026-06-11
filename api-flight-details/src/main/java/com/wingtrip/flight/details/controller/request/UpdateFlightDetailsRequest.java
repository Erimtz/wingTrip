package com.wingtrip.flight.details.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request to update flight details for a specific flight")
public class UpdateFlightDetailsRequest {

    @Schema(description = "Model of the aircraft", example = "Boeing 737")
    private String aircraftModel;

    @Schema(description = "Gate number for departure", example = "A12")
    private String gateNumber;

    @Schema(description = "Terminal for departure", example = "Terminal 1")
    private String terminalOrigin;

    @Schema(description = "Terminal for arrival", example = "Terminal 2")
    private String terminalDestination;

    @Schema(description = "Indicates if the flight includes cabin baggage", example = "true")
    private Boolean includesCabinBaggage;

    @Schema(description = "Indicates if the flight includes checked baggage", example = "true")
    private Boolean includesCheckedBaggage;

    @PositiveOrZero(message = "Extra baggage price must be zero or positive")
    @Schema(description = "Price for extra baggage", example = "50.00")
    private BigDecimal extraBaggagePrice;

    @Schema(description = "Seat selection policy", example = "Free seat selection for all passengers")
    private String seatSelectionPolicy;

    @Schema(description = "Meal service details", example = "Vegetarian meal included, no nuts")
    private String mealService;

    @Schema(description = "Wi-Fi availability on the flight", example = "true")
    private Boolean availableWifiConnection;

    @Schema(description = "In-flight entertainment options", example = "Movies, TV shows, music")
    private String entertainmentOptions;
}
