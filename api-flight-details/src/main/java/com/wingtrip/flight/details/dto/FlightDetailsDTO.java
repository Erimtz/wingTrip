package com.wingtrip.flight.details.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightDetailsDTO {
    private String id;
    private String aircraftModel;
    private String gateNumber;
    private String terminalOrigin;
    private String terminalDestination;
    private boolean includesCabinBaggage;
    private boolean includesCheckedBaggage;
    private BigDecimal extraBaggagePrice;
    private String seatSelectionPolicy;
    private String mealService;
    private Boolean availableWifiConnection;
    private String entertainmentOptions;
    private String flightId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
