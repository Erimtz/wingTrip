package com.wingtrip.flight.details.service.impl;

import com.wingtrip.flight.details.controller.mapper.FlightDetailsMapper;
import com.wingtrip.flight.details.dto.FlightDetailsDTO;
import com.wingtrip.flight.details.exception.FlightDetailsAlreadyExistsException;
import com.wingtrip.flight.details.exception.FlightDetailsNotCreateException;
import com.wingtrip.flight.details.exception.FlightDetailsNotFoundException;
import com.wingtrip.flight.details.exception.MessageCode;
import com.wingtrip.flight.details.model.FlightDetailsEntity;
import com.wingtrip.flight.details.repository.FlightDetailsRepository;
import com.wingtrip.flight.details.service.FlightDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class FlightDetailsImpl implements FlightDetailsService {

    private final FlightDetailsRepository flightDetailsRepository;
    private final FlightDetailsMapper flightDetailsMapper;


    @Override
    public List<FlightDetailsDTO> getAllFlightDetails() throws FlightDetailsNotFoundException {
        log.info("Getting all flight details");
        try {
            return flightDetailsRepository.findAll()
                    .stream()
                    .map(flightDetailsMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Error retrieving all flight details", ex);
            throw new FlightDetailsNotFoundException(MessageCode.FLIGHT_DETAILS_NOT_FOUND);
        }
    }

    @Override
    public Optional<FlightDetailsDTO> getFlightDetailsById(String id) throws FlightDetailsNotFoundException {
        log.info("Getting flight details by id: {}", id);
        try {
            return flightDetailsRepository.findById(id)
                    .map(flightDetailsMapper::toDTO);
        } catch (Exception ex) {
            log.error("Error retrieving flight details by id: {}", id, ex);
            return Optional.empty();
        }
    }

    @Override
    public Optional<FlightDetailsDTO> getFlightDetailsByFlightId(String flightId) throws FlightDetailsNotFoundException {
        log.info("Getting flight details by flightId: {}", flightId);
        try {
            return flightDetailsRepository.findByFlightId(flightId)
                    .map(flightDetailsMapper::toDTO);
        } catch (Exception ex) {
            log.error("Error retrieving flight details by flightId: {}", flightId, ex);
            return Optional.empty();
        }
    }

    @Override
    public FlightDetailsDTO createFlightDetails(FlightDetailsDTO flightDetailsDTO) throws FlightDetailsNotCreateException {
        log.info("Creating flight details for flightId: {}", flightDetailsDTO.getFlightId());
        try {
            if (flightDetailsRepository.existsByFlightId(flightDetailsDTO.getFlightId())) {
                throw new FlightDetailsAlreadyExistsException(MessageCode.FLIGHT_DETAILS_ALREADY_EXISTS);
            }

            FlightDetailsEntity entity = FlightDetailsEntity.builder()
                    .aircraftModel(flightDetailsDTO.getAircraftModel())
                    .gateNumber(flightDetailsDTO.getGateNumber())
                    .terminalOrigin(flightDetailsDTO.getTerminalOrigin())
                    .terminalDestination(flightDetailsDTO.getTerminalDestination())
                    .includesCabinBaggage(flightDetailsDTO.isIncludesCabinBaggage())
                    .includesCheckedBaggage(flightDetailsDTO.isIncludesCheckedBaggage())
                    .extraBaggagePrice(flightDetailsDTO.getExtraBaggagePrice())
                    .seatSelectionPolicy(flightDetailsDTO.getSeatSelectionPolicy())
                    .mealService(flightDetailsDTO.getMealService())
                    .availableWifiConnection(flightDetailsDTO.getAvailableWifiConnection())
                    .entertainmentOptions(flightDetailsDTO.getEntertainmentOptions())
                    .flightId(flightDetailsDTO.getFlightId())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            FlightDetailsEntity saved = flightDetailsRepository.save(entity);
            log.info("Flight details created for flightId: {}", saved.getFlightId());
            return flightDetailsMapper.toDTO(saved);
        } catch (FlightDetailsAlreadyExistsException e) {
            throw e;
        } catch (Exception ex) {
            log.error("Error creating flight details", ex);
            throw new FlightDetailsNotCreateException(MessageCode.FLIGHT_DETAILS_NOT_CREATE);
        }
    }

    @Override
    public FlightDetailsDTO updateFlightDetails(String id, FlightDetailsDTO flightDetailsDTO) throws FlightDetailsNotFoundException {
        log.info("Updating flight details by id: {}", id);
        try {
            FlightDetailsEntity entity = flightDetailsRepository.findById(id)
                    .orElseThrow(() -> new FlightDetailsNotFoundException(
                            String.format(MessageCode.FLIGHT_DETAILS_NOT_FOUND_BY_ID, id)));

            if (flightDetailsDTO.getAircraftModel() != null) entity.setAircraftModel(flightDetailsDTO.getAircraftModel());
            if (flightDetailsDTO.getGateNumber() != null) entity.setGateNumber(flightDetailsDTO.getGateNumber());
            if (flightDetailsDTO.getTerminalOrigin() != null) entity.setTerminalOrigin(flightDetailsDTO.getTerminalOrigin());
            if (flightDetailsDTO.getTerminalDestination() != null) entity.setTerminalDestination(flightDetailsDTO.getTerminalDestination());
            if (flightDetailsDTO.getExtraBaggagePrice() != null) entity.setExtraBaggagePrice(flightDetailsDTO.getExtraBaggagePrice());
            if (flightDetailsDTO.getSeatSelectionPolicy() != null) entity.setSeatSelectionPolicy(flightDetailsDTO.getSeatSelectionPolicy());
            if (flightDetailsDTO.getMealService() != null) entity.setMealService(flightDetailsDTO.getMealService());
            if (flightDetailsDTO.getEntertainmentOptions() != null) entity.setEntertainmentOptions(flightDetailsDTO.getEntertainmentOptions());

            entity.setIncludesCabinBaggage(flightDetailsDTO.isIncludesCabinBaggage());
            entity.setIncludesCheckedBaggage(flightDetailsDTO.isIncludesCheckedBaggage());
            entity.setAvailableWifiConnection(flightDetailsDTO.getAvailableWifiConnection());
            entity.setUpdatedAt(LocalDateTime.now());

            FlightDetailsEntity updated = flightDetailsRepository.save(entity);
            log.info("Flight details updated for id: {}", id);
            return flightDetailsMapper.toDTO(updated);
        } catch (FlightDetailsNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            log.error("Error updating flight details: {}", id, e);
            throw new FlightDetailsNotFoundException(MessageCode.FLIGHT_DETAILS_NOT_FOUND);
        }
    }

    @Override
    public boolean deleteFlightDetails(String id) {
        log.info("Deleting flight details by id: {}", id);
        try {
            if (!flightDetailsRepository.existsById(id)) {
                log.warn("Flight details not found to delete by id: {}", id);
                return false;
            }
            flightDetailsRepository.deleteById(id);
            log.info("Flight details deleted by id: {}", id);
            return true;
        } catch (Exception ex) {
            log.error("Error deleting flight details by id: {}", id, ex);
            return false;
        }
    }

    @Override
    public boolean deleteFlightDetailsByFlightId(String flightId) {
        log.info("Deleting flight details by flightId: {}", flightId);
        try {
            if (!flightDetailsRepository.existsByFlightId(flightId)) {
                log.warn("Flight details not found to delete by flightId: {}", flightId);
                return false;
            }
            flightDetailsRepository.deleteByFlightId(flightId);
            log.info("Flight details deleted by flightId: {}", flightId);
            return true;
        } catch (Exception ex) {
            log.error("Error deleting flight details by flightId: {}", flightId, ex);
            return false;
        }
    }

    @Override
    public List<FlightDetailsDTO> getFlightDetailsByAircraftModel(String aircraftModel) throws FlightDetailsNotFoundException {
        log.info("Getting flight details by aircraftModel: {}", aircraftModel);
        try {
            return flightDetailsRepository.findByAircraftModel(aircraftModel)
                    .stream()
                    .map(flightDetailsMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Error retrieving flight details by aircraftModel: {}", aircraftModel, ex);
            throw new FlightDetailsNotFoundException(MessageCode.FLIGHT_DETAILS_NOT_FOUND);
        }
    }

    @Override
    public List<FlightDetailsDTO> getFlightDetailsByMealService(String mealService) throws FlightDetailsNotFoundException {
        log.info("Getting flight details by mealService: {}", mealService);
        try {
            return flightDetailsRepository.findByMealService(mealService)
                    .stream()
                    .map(flightDetailsMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Error retrieving flight details by mealService: {}", mealService, ex);
            throw new FlightDetailsNotFoundException(MessageCode.FLIGHT_DETAILS_NOT_FOUND);
        }
    }

    @Override
    public List<FlightDetailsDTO> getFlightDetailsByBaggageOptions(boolean cabinBaggage, boolean checkedBaggage) throws FlightDetailsNotFoundException {
        log.info("Getting flight details by baggage options: cabin={}, checked={}", cabinBaggage, checkedBaggage);
        try {
            return flightDetailsRepository.findByIncludesCabinBaggageAndIncludesCheckedBaggage(cabinBaggage, checkedBaggage)
                    .stream()
                    .map(flightDetailsMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Error retrieving flight details by baggage options", ex);
            throw new FlightDetailsNotFoundException(MessageCode.FLIGHT_DETAILS_NOT_FOUND);
        }
    }

    @Override
    public List<FlightDetailsDTO> getFlightDetailsByWifiAndMeal(Boolean wifi, String mealService) throws FlightDetailsNotFoundException {
        log.info("Getting flight details by wifi={} and mealService={}", wifi, mealService);
        try {
            return flightDetailsRepository.findByAvailableWifiConnectionAndMealService(wifi, mealService)
                    .stream()
                    .map(flightDetailsMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Error retrieving flight details by wifi and meal", ex);
            throw new FlightDetailsNotFoundException(MessageCode.FLIGHT_DETAILS_NOT_FOUND);
        }
    }

    @Override
    public List<FlightDetailsDTO> getFlightDetailsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) throws FlightDetailsNotFoundException {
        log.info("Getting flight details by price range: min={}, max={}", minPrice, maxPrice);
        try {
            return flightDetailsRepository.findByExtraBaggagePriceBetween(minPrice, maxPrice)
                    .stream()
                    .map(flightDetailsMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Error retrieving flight details by price range", ex);
            throw new FlightDetailsNotFoundException(MessageCode.FLIGHT_DETAILS_NOT_FOUND);
        }
    }
}
