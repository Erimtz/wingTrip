package com.wingtrip.flight.repository;

import com.wingtrip.flight.model.FlightDocument;
import com.wingtrip.flight.model.ServiceClass;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends MongoRepository<FlightDocument, String> {

    Optional<FlightDocument> findByFlightNumber(String flightNumber);
    List<FlightDocument> findByStatus(String status);
    List<FlightDocument> findByAirline(String airline);
    List<FlightDocument> findByOriginAirportAndDestinationAirport(String origin, String destination);
    List<FlightDocument> findByServiceClass(ServiceClass serviceClass);
    List<FlightDocument> findByDepartureTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    List<FlightDocument> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    List<FlightDocument> findByAvailableSeatsGreaterThan(Integer availableSeats);
    List<FlightDocument> findByOriginAirportAndDestinationAirportAndStatus(String origin, String destination, String status);
}
