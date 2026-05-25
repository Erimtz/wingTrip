package com.wingtrip.flight.details.repository;

import com.wingtrip.flight.details.model.FlightDetailsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightDetailsRepository extends MongoRepository<FlightDetailsEntity, String> {

    Optional<FlightDetailsEntity> findByFlightId(String flightId);
    boolean existsByFlightId(String flightId);
    void deleteByFlightId(String flightId);
    List<FlightDetailsEntity> findByIncludesCabinBaggage(boolean includesCabinBaggage);
    List<FlightDetailsEntity> findByIncludesCheckedBaggage(boolean includesCheckedBaggage);
    List<FlightDetailsEntity> findByAvailableWifiConnection(Boolean isAvailableWifiConnection);
    List<FlightDetailsEntity> findByMealService(String mealService);
    List<FlightDetailsEntity> findByAircraftModel(String aircraftModel);
    List<FlightDetailsEntity> findByGateNumber(String gateNumber);
    List<FlightDetailsEntity> findByTerminalOrigin(String terminalOrigin);
    List<FlightDetailsEntity> findByTerminalDestination(String terminalDestination);
    List<FlightDetailsEntity> findByExtraBaggagePriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    List<FlightDetailsEntity> findBySeatSelectionPolicy(String seatSelectionPolicy);
    List<FlightDetailsEntity> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    List<FlightDetailsEntity> findByIncludesCabinBaggageAndIncludesCheckedBaggage(boolean cabin, boolean checked);
    List<FlightDetailsEntity> findByAvailableWifiConnectionAndMealService(Boolean wifi, String meal);
}
