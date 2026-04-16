package com.wingtrip.flight.dto;

import com.wingtrip.flight.model.FlightDocument;
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
    private boolean isDirect;
    private int stops;
    private ServiceClass serviceClass;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FlightDTO(FlightDocument document) {
        this.id = document.getId();
        this.flightNumber = document.getFlightNumber();
        this.airline = document.getAirline();
        this.originAirport = document.getOriginAirport();
        this.destinationAirport = document.getDestinationAirport();
        this.departureTime = document.getDepartureTime();
        this.arrivalTime = document.getArrivalTime();
        this.price = document.getPrice();
        this.currency = document.getCurrency();
        this.availableSeats = document.getAvailableSeats();
        this.totalSeats = document.getTotalSeats();
        this.status = document.getStatus();
        this.isDirect = document.isDirect();
        this.stops = document.getStops();
        this.serviceClass = document.getServiceClass();
        this.createdAt = document.getCreatedAt();
        this.updatedAt = document.getUpdatedAt();
    }
}
