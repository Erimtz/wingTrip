package com.wingtrip.flight.details.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "flight_details")
public class FlightDetailsEntity {

    @Id
    private String id;

    @Field("aircraft_model")
    private String aircraftModel;

    @Field("gate_number")
    private String gateNumber;

    @Field("terminal_origin")
    private String terminalOrigin;

    @Field("terminal_destination")
    private String terminalDestination;

    @Field("includes_cabin_baggage")
    private boolean includesCabinBaggage;

    @Field("includes_checked_baggage")
    private boolean includesCheckedBaggage;

    @Field("extra_baggage_price")
    private BigDecimal extraBaggagePrice;

    @Field("seat_selection_policy")
    private String seatSelectionPolicy;

    @Field("meal_service")
    private String mealService;

    @Field("available_wifi_connection")
    private Boolean availableWifiConnection;

    @Field("flight_id")
    private String flightId;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;
}
