package com.wingtrip.flight.service.impl;

import com.wingtrip.flight.dto.FlightDTO;
import com.wingtrip.flight.exception.FlightNotCreatedException;
import com.wingtrip.flight.exception.FlightNotFoundException;
import com.wingtrip.flight.exception.MessageCode;
import com.wingtrip.flight.controller.mapper.FlightMapper;
import com.wingtrip.flight.model.FlightEntity;
import com.wingtrip.flight.model.FlightStatus;
import com.wingtrip.flight.repository.FlightRepository;
import com.wingtrip.flight.service.FlightService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;

    @Override
    public List<FlightDTO> getAllFlights() throws FlightNotFoundException {
        log.info("Getting all flights");
        try {
            return flightRepository.findAll()
                    .stream()
                    .map(flightMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Error retrieving all flights", ex);
            throw new FlightNotFoundException(MessageCode.FLIGHT_NOT_FOUND);
        }
    }

    @Override
    public Optional<FlightDTO> getFlightById(String flightNumber) {
        log.info("Obtaining flight number: {}", flightNumber);
        try {
            return flightRepository.findByFlightNumber(flightNumber)
                    .map(flightMapper::toDTO);
        } catch (Exception ex) {
            log.error("Error retrieving flight number: {}", flightNumber, ex);
            return Optional.empty();
        }
    }

    @Override
    public List<FlightDTO> searchFlights(String origin, String destination, String departureDate) throws FlightNotFoundException {
        log.info("Searching for flights: origin={}, destination={}, date={}", origin, destination, departureDate);
        try {
            LocalDate date = LocalDate.parse(departureDate);
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

            return flightRepository.findByOriginAirportAndDestinationAirportAndDepartureBetween(
                    origin, destination, startOfDay, endOfDay)
                    .stream()
                    .map(flightMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Error searching for flights", ex);
            throw new FlightNotFoundException(MessageCode.FLIGHT_NOT_FOUND);
        }
    }

    @Override
    public FlightDTO createFlight(FlightDTO flightDTO) throws FlightNotCreatedException {
        log.info("Creating a new flight: {}", flightDTO.getFlightNumber());

        try {
            // Validar que no exista un vuelo con el mismo número
            if (flightRepository.findByFlightNumber(flightDTO.getFlightNumber()).isPresent()) {
                throw new FlightNotCreatedException(MessageCode.FLIGHT_NOT_CREATE);
            }

            FlightEntity entity = FlightEntity.builder()
                    .flightNumber(flightDTO.getFlightNumber())
                    .airline(flightDTO.getAirline())
                    .originAirport(flightDTO.getOriginAirport())
                    .destinationAirport(flightDTO.getDestinationAirport())
                    .departureTime(flightDTO.getDepartureTime())
                    .arrivalTime(flightDTO.getArrivalTime())
                    .price(flightDTO.getPrice())
                    .currency(flightDTO.getCurrency())
                    .availableSeats(flightDTO.getAvailableSeats())
                    .totalSeats(flightDTO.getTotalSeats())
                    .status(FlightStatus.SCHEDULED)
                    .isDirect(flightDTO.isDirect())
                    .stops(flightDTO.getStops())
                    .serviceClass(flightDTO.getServiceClass())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            FlightEntity saved = flightRepository.save(entity);
            log.info("Flight successfully created: {}", saved.getFlightNumber());
            return flightMapper.toDTO(saved);
        } catch (FlightNotCreatedException e) {
            throw e;
        } catch (Exception ex) {
            log.error("Error creating flight", ex);
            throw new FlightNotCreatedException(MessageCode.FLIGHT_NOT_CREATE);
        }
    }

    @Override
    public FlightDTO updateFlight(String flightNumber, FlightDTO flightDTO) throws FlightNotFoundException {
        log.info("Updating flight: {}", flightNumber);

        try {
            FlightEntity entity = flightRepository.findByFlightNumber(flightNumber)
                    .orElseThrow(() -> new FlightNotFoundException(MessageCode.FLIGHT_NOT_FOUND_BY_NUMBER));

            // Actualizar solo los campos que cambien
            if (flightDTO.getAirline() != null) entity.setAirline(flightDTO.getAirline());
            if (flightDTO.getDepartureTime() != null) entity.setDepartureTime(flightDTO.getDepartureTime());
            if (flightDTO.getArrivalTime() != null) entity.setArrivalTime(flightDTO.getArrivalTime());
            if (flightDTO.getPrice() != null) entity.setPrice(flightDTO.getPrice());
            if (flightDTO.getCurrency() != null) entity.setCurrency(flightDTO.getCurrency());
            if (flightDTO.getAvailableSeats() > 0) entity.setAvailableSeats(flightDTO.getAvailableSeats());
            if (flightDTO.getTotalSeats() > 0) entity.setTotalSeats(flightDTO.getTotalSeats());
            if (flightDTO.getServiceClass() != null) entity.setServiceClass(flightDTO.getServiceClass());

            entity.setUpdatedAt(LocalDateTime.now());
            FlightEntity updated = flightRepository.save(entity);
            log.info("Flight updated successfully: {}", flightNumber);
            return flightMapper.toDTO(updated);
        } catch (FlightNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            log.error("Error updating flight: {}", flightNumber, e);
            throw new FlightNotFoundException(MessageCode.FLIGHT_NOT_FOUND);
        }
    }

    @Override
    public boolean deleteFlight(String flightNumber) {
        log.info("Eliminating flight: {}", flightNumber);

        try {
            Optional<FlightEntity> flight = flightRepository.findByFlightNumber(flightNumber);
            if (flight.isEmpty()) {
                log.warn("Flight not found to delete: {}", flightNumber);
                return false;
            }

            flightRepository.delete(flight.get());
            log.info("Flight successfully deleted: {}", flightNumber);
            return true;
        } catch (Exception ex) {
            log.error("Error deleting flight: {}", flightNumber, ex);
            return false;
        }
    }
}
