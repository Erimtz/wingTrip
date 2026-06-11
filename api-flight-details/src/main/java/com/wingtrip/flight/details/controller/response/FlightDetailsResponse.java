package com.wingtrip.flight.details.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response with flight details data")
public class FlightDetailsResponse {
    @Schema(description = "Unique flight details ID", example = "507f1f77bcf86cd799439011")
    private String id;

    @Schema(description = "Model of the aircraft", example = "Boeing 737")
    private String aircraftModel;

    @Schema(description = "Gate number for departure", example = "A12")
    private String gateNumber;

    @Schema(description = "Terminal for departure", example = "Terminal 1")
    private String terminalOrigin;

    @Schema(description = "Terminal for arrival", example = "Terminal 2")
    private String terminalDestination;

    @Schema(description = "Indicates if the flight includes cabin baggage", example = "true")
    private boolean includesCabinBaggage;

    @Schema(description = "Indicates if the flight includes checked baggage", example = "true")
    private boolean includesCheckedBaggage;

    @Schema(description = "Price for extra baggage", example = "50.00")
    private String extraBaggagePrice;

    @Schema(description = "Seat selection policy", example = "Free seat selection for all passengers")
    private String seatSelectionPolicy;

    @Schema(description = "Meal service details", example = "Vegetarian meal included")
    private String mealService;

    @Schema(description = "Wi-Fi availability on the flight", example = "true")
    private boolean availableWifiConnection;

    @Schema(description = "In-flight entertainment options", example = "Movies, TV shows, music")
    private String entertainmentOptions;

    @Schema(description = "ID of the flight to which these details belong", example = "123e4567-e89b-12d3-a456-426614174000")
    private String flightId;

    @Schema(description = "Date and time when the flight details were created", example = "2026-08-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Date and time when the flight details were last updated", example = "2026-08-16T12:00:00")
    private LocalDateTime updatedAt;
}
