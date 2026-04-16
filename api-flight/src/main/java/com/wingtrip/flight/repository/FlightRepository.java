package com.wingtrip.flight.repository;

import com.wingtrip.flight.model.Flight;
import com.wingtrip.flight.model.ServiceClass;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends MongoRepository<Flight, String> {

    Optional<Flight> findByFlightNumber(String flightNumber);
    List<Flight> findByStatus(String status);
    List<Flight> findByAirline(String airline);
    List<Flight> findByOriginAirportAndDestinationAirport(String origin, String destination);
    List<Flight> findByServiceClass(ServiceClass serviceClass);
    List<Flight> findByDepartureTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    List<Flight> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    List<Flight> findByAvailableSeatsGreaterThan(Integer availableSeats);
    List<Flight> findByOriginAirportAndDestinationAirportAndStatus(String origin, String destination, String status);
}
