package com.wingtrip.flight.repository;

import com.wingtrip.flight.model.FlightEntity;
import com.wingtrip.flight.model.ServiceClass;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends MongoRepository<FlightEntity, String> {

    Optional<FlightEntity> findByFlightNumber(String flightNumber);
    List<FlightEntity> findByStatus(String status);
    List<FlightEntity> findByAirline(String airline);
    List<FlightEntity> findByOriginAirportAndDestinationAirport(String origin, String destination);
    List<FlightEntity> findByServiceClass(ServiceClass serviceClass);
    List<FlightEntity> findByDepartureTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    List<FlightEntity> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    List<FlightEntity> findByAvailableSeatsGreaterThan(Integer availableSeats);
    List<FlightEntity> findByOriginAirportAndDestinationAirportAndStatus(String origin, String destination, String status);

    /**
     * Search for flights by origin, destination, and departure date range
     */
    List<FlightEntity> findByOriginAirportAndDestinationAirportAndDepartureTimeBetween(
            String originAirport,
            String destinationAirport,
            LocalDateTime startDeparture,
            LocalDateTime endDeparture);
}
