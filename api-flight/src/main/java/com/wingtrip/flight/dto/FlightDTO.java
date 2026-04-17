package com.wingtrip.flight.dto;

import com.wingtrip.flight.model.FlightStatus;
import com.wingtrip.flight.model.ServiceClass;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightDTO {
    private String id;
    private String flightNumber;
    private String airline;
    private String originAirport;
    private String destinationAirport;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private BigDecimal price;
    private String currency;
    private int availableSeats;
    private int totalSeats;
    private FlightStatus status;
    private boolean directFlight;
    private int stops;
    private ServiceClass serviceClass;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
