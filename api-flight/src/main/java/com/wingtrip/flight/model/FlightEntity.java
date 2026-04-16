package com.wingtrip.flight.model;

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
@Document(collection = "flights")
public class FlightEntity {

    @Id
    private String id;

    @Field("flight_number")
    private String flightNumber;

    @Field("airline")
    private String airline;

    @Field("origin_airport")
    private String originAirport;

    @Field("destination_airport")
    private String destinationAirport;

    @Field("departure_time")
    private LocalDateTime departureTime;

    @Field("arrival_time")
    private LocalDateTime arrivalTime;

    @Field("price")
    private BigDecimal price;

    @Field("currency")
    private String currency;

    @Field("available_seats")
    private int availableSeats;

    @Field("total_seats")
    private int totalSeats;

    @Field("status")
    private FlightStatus status;

    @Field("is_direct")
    private boolean isDirect;

    @Field("stops")
    private int stops;

    @Field("service_class")
    private ServiceClass serviceClass;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;
}
