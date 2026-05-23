package com.wingtrip.flight.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.wingtrip.flight.controller.mapper.FlightMapper;
import com.wingtrip.flight.dto.FlightDTO;
import com.wingtrip.flight.exception.FlightNotCreatedException;
import com.wingtrip.flight.exception.FlightNotFoundException;
import com.wingtrip.flight.model.FlightEntity;
import com.wingtrip.flight.model.FlightStatus;
import com.wingtrip.flight.model.ServiceClass;
import com.wingtrip.flight.repository.FlightRepository;
import com.wingtrip.flight.service.impl.FlightServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceImplTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private FlightMapper flightMapper;

    @InjectMocks
    private FlightServiceImpl flightService;

    private FlightEntity flightEntity;
    private FlightDTO flightDTO;

    @BeforeEach
    void setUp() {
        LocalDateTime departureTime = LocalDateTime.of(2024, 12, 20, 10, 0, 0);
        LocalDateTime arrivalTime = LocalDateTime.of(2024, 12, 20, 12, 30, 0);

        flightEntity = FlightEntity.builder()
                .id("1")
                .flightNumber("LA123")
                .airline("LATAM")
                .originAirport("MDE")
                .destinationAirport("BOG")
                .departureTime(departureTime)
                .arrivalTime(arrivalTime)
                .price(new BigDecimal("250000.0"))
                .currency("COP")
                .availableSeats(180)
                .totalSeats(180)
                .status(FlightStatus.SCHEDULED)
                .directFlight(true)
                .stops(0)
                .serviceClass(ServiceClass.ECONOMY)
                .build();

        flightDTO = FlightDTO.builder()
                .id("1")
                .flightNumber("LA123")
                .airline("LATAM")
                .originAirport("MDE")
                .destinationAirport("BOG")
                .departureTime(departureTime)
                .arrivalTime(arrivalTime)
                .price(new BigDecimal("250000.0"))
                .currency("COP")
                .availableSeats(180)
                .totalSeats(180)
                .directFlight(true)
                .stops(0)
                .serviceClass(ServiceClass.ECONOMY)
                .build();
    }

    @Test
    void testGetAllFlights() throws FlightNotFoundException {
        when(flightRepository.findAll()).thenReturn(List.of(flightEntity));
        when(flightMapper.toDTO(flightEntity)).thenReturn(flightDTO);

        List<FlightDTO> result = flightService.getAllFlights();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(flightRepository, times(1)).findAll();
    }

    @Test
    void testGetAllFlights_ThrowsException() {
        when(flightRepository.findAll()).thenThrow(new RuntimeException("DB error"));

        assertThrows(FlightNotFoundException.class, () -> flightService.getAllFlights());
    }

    @Test
    void testGetFlightById() {
        when(flightRepository.findByFlightNumber("LA123")).thenReturn(Optional.of(flightEntity));
        when(flightMapper.toDTO(flightEntity)).thenReturn(flightDTO);

        Optional<FlightDTO> result = flightService.getFlightById("LA123");

        assertTrue(result.isPresent());
        assertEquals("LA123", result.get().getFlightNumber());
        verify(flightRepository, times(1)).findByFlightNumber("LA123");
    }

    @Test
    void testGetFlightByIdNotFound() {
        when(flightRepository.findByFlightNumber("INVALID")).thenReturn(Optional.empty());

        Optional<FlightDTO> result = flightService.getFlightById("INVALID");

        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateFlight_Success() throws FlightNotCreatedException {
        when(flightRepository.findByFlightNumber("LA123")).thenReturn(Optional.empty());
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flightEntity);
        when(flightMapper.toDTO(flightEntity)).thenReturn(flightDTO);

        FlightDTO result = flightService.createFlight(flightDTO);

        assertNotNull(result);
        assertEquals("LA123", result.getFlightNumber());
        verify(flightRepository, times(1)).save(any(FlightEntity.class));
    }

    @Test
    void testCreateFlight_AlreadyExists_ThrowsException() {
        when(flightRepository.findByFlightNumber("LA123")).thenReturn(Optional.of(flightEntity));

        assertThrows(FlightNotCreatedException.class, () -> flightService.createFlight(flightDTO));
        verify(flightRepository, never()).save(any());
    }

    @Test
    void testUpdateFlight_Success() throws FlightNotFoundException {
        when(flightRepository.findByFlightNumber("LA123")).thenReturn(Optional.of(flightEntity));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flightEntity);
        when(flightMapper.toDTO(flightEntity)).thenReturn(flightDTO);

        FlightDTO result = flightService.updateFlight("LA123", flightDTO);

        assertNotNull(result);
        verify(flightRepository, times(1)).save(any(FlightEntity.class));
    }

    @Test
    void testUpdateFlight_NotFound_ThrowsException() {
        when(flightRepository.findByFlightNumber("INVALID")).thenReturn(Optional.empty());

        assertThrows(FlightNotFoundException.class,
                () -> flightService.updateFlight("INVALID", flightDTO));
    }

    @Test
    void testUpdateFlight_OnlyUpdatesNonNullFields() throws FlightNotFoundException {
        FlightDTO partialUpdate = FlightDTO.builder()
                .airline("LATAM Updated")
                .price(new BigDecimal("300000.0"))
                .build();

        when(flightRepository.findByFlightNumber("LA123")).thenReturn(Optional.of(flightEntity));
        when(flightRepository.save(any())).thenReturn(flightEntity);
        when(flightMapper.toDTO(flightEntity)).thenReturn(flightDTO);

        flightService.updateFlight("LA123", partialUpdate);

        verify(flightRepository, times(1)).save(any());
    }

    @Test
    void testDeleteFlight_Success() {
        when(flightRepository.findByFlightNumber("LA123")).thenReturn(Optional.of(flightEntity));

        boolean result = flightService.deleteFlight("LA123");

        assertTrue(result);
        verify(flightRepository, times(1)).delete(flightEntity);
    }

    @Test
    void testDeleteFlight_NotFound() {
        when(flightRepository.findByFlightNumber("INVALID")).thenReturn(Optional.empty());

        boolean result = flightService.deleteFlight("INVALID");

        assertFalse(result);
        verify(flightRepository, never()).delete(any());
    }

    @Test
    void testSearchFlights_Success() throws FlightNotFoundException {
        LocalDateTime start = LocalDate.parse("2024-12-20").atStartOfDay();
        LocalDateTime end = LocalDate.parse("2024-12-20").atTime(LocalTime.MAX);

        when(flightRepository.findByOriginAirportAndDestinationAirportAndDepartureTimeBetween(
                "MDE", "BOG", start, end)).thenReturn(List.of(flightEntity));
        when(flightMapper.toDTO(flightEntity)).thenReturn(flightDTO);

        List<FlightDTO> result = flightService.searchFlights("MDE", "BOG", "2024-12-20");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testSearchFlights_InvalidDate_ThrowsException() {
        assertThrows(FlightNotFoundException.class,
                () -> flightService.searchFlights("MDE", "BOG", "fecha-invalida"));
    }
}
