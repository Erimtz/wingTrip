package com.wingtrip.flight.details.service;

import com.wingtrip.flight.details.dto.FlightDetailsDTO;
import com.wingtrip.flight.details.exception.FlightDetailsNotCreateException;
import com.wingtrip.flight.details.exception.FlightDetailsNotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface FlightDetailsService {

    List<FlightDetailsDTO> getAllFlightDetails() throws FlightDetailsNotFoundException;

    Optional<FlightDetailsDTO> getFlightDetailsById(String id) throws FlightDetailsNotFoundException;

    Optional<FlightDetailsDTO> getFlightDetailsByFlightId(String flightId) throws FlightDetailsNotFoundException;

    FlightDetailsDTO createFlightDetails(FlightDetailsDTO flightDetailsDTO) throws FlightDetailsNotCreateException;

    FlightDetailsDTO updateFlightDetails(String id, FlightDetailsDTO flightDetailsDTO) throws FlightDetailsNotFoundException;

    boolean deleteFlightDetails(String id);

    boolean deleteFlightDetailsByFlightId(String flightId);

    List<FlightDetailsDTO> getFlightDetailsByAircraftModel(String aircraftModel) throws FlightDetailsNotFoundException;

    List<FlightDetailsDTO> getFlightDetailsByMealService(String mealService) throws FlightDetailsNotFoundException;

    List<FlightDetailsDTO> getFlightDetailsByBaggageOptions(boolean cabinBaggage, boolean checkedBaggage) throws FlightDetailsNotFoundException;

    List<FlightDetailsDTO> getFlightDetailsByWifiAndMeal(Boolean wifi, String mealService) throws FlightDetailsNotFoundException;

    List<FlightDetailsDTO> getFlightDetailsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) throws FlightDetailsNotFoundException;
}
