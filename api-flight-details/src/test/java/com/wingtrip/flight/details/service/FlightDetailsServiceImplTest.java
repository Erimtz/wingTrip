package com.wingtrip.flight.details.service;

import com.wingtrip.flight.details.controller.mapper.FlightDetailsMapper;
import com.wingtrip.flight.details.dto.FlightDetailsDTO;
import com.wingtrip.flight.details.exception.FlightDetailsAlreadyExistsException;
import com.wingtrip.flight.details.exception.FlightDetailsNotCreateException;
import com.wingtrip.flight.details.exception.FlightDetailsNotFoundException;
import com.wingtrip.flight.details.model.FlightDetailsEntity;
import com.wingtrip.flight.details.repository.FlightDetailsRepository;
import com.wingtrip.flight.details.service.impl.FlightDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightDetailsServiceImplTest {

    @Mock
    private FlightDetailsRepository flightDetailsRepository;

    @Mock
    private FlightDetailsMapper flightDetailsMapper;

    @InjectMocks
    private FlightDetailsImpl flightDetailsService;

    private FlightDetailsEntity flightDetailsEntity;
    private FlightDetailsDTO flightDetailsDTO;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        flightDetailsEntity = FlightDetailsEntity.builder()
                .id("507f1f77bcf86cd799439011")
                .aircraftModel("Boeing 737")
                .gateNumber("A12")
                .terminalOrigin("Terminal 1")
                .terminalDestination("Terminal 2")
                .includesCabinBaggage(true)
                .includesCheckedBaggage(true)
                .extraBaggagePrice(new BigDecimal("50.00"))
                .seatSelectionPolicy("Free seat selection")
                .mealService("Vegetarian meal included")
                .availableWifiConnection(true)
                .entertainmentOptions("Movies, TV shows, music")
                .flightId("123e4567-e89b-12d3-a456-426614174000")
                .createdAt(now)
                .updatedAt(now)
                .build();

        flightDetailsDTO = FlightDetailsDTO.builder()
                .id("507f1f77bcf86cd799439011")
                .aircraftModel("Boeing 737")
                .gateNumber("A12")
                .terminalOrigin("Terminal 1")
                .terminalDestination("Terminal 2")
                .includesCabinBaggage(true)
                .includesCheckedBaggage(true)
                .extraBaggagePrice(new BigDecimal("50.00"))
                .seatSelectionPolicy("Free seat selection")
                .mealService("Vegetarian meal included")
                .availableWifiConnection(true)
                .entertainmentOptions("Movies, TV shows, music")
                .flightId("123e4567-e89b-12d3-a456-426614174000")
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    @Test
    void testGetAllFlightDetails_Success() throws FlightDetailsNotFoundException {
        when(flightDetailsRepository.findAll()).thenReturn(List.of(flightDetailsEntity));
        when(flightDetailsMapper.toDTO(flightDetailsEntity)).thenReturn(flightDetailsDTO);

        List<FlightDetailsDTO> result = flightDetailsService.getAllFlightDetails();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Boeing 737", result.get(0).getAircraftModel());
        verify(flightDetailsRepository, times(1)).findAll();
    }

    @Test
    void testGetAllFlightDetails_ThrowsException() {
        when(flightDetailsRepository.findAll()).thenThrow(new RuntimeException("DB error"));

        assertThrows(FlightDetailsNotFoundException.class, 
                () -> flightDetailsService.getAllFlightDetails());
    }

    @Test
    void testGetFlightDetailsById_Success() throws FlightDetailsNotFoundException {
        when(flightDetailsRepository.findById("507f1f77bcf86cd799439011"))
                .thenReturn(Optional.of(flightDetailsEntity));
        when(flightDetailsMapper.toDTO(flightDetailsEntity)).thenReturn(flightDetailsDTO);

        Optional<FlightDetailsDTO> result = flightDetailsService.getFlightDetailsById("507f1f77bcf86cd799439011");

        assertTrue(result.isPresent());
        assertEquals("Boeing 737", result.get().getAircraftModel());
        verify(flightDetailsRepository, times(1)).findById("507f1f77bcf86cd799439011");
    }

    @Test
    void testGetFlightDetailsById_NotFound() {
        when(flightDetailsRepository.findById("INVALID")).thenReturn(Optional.empty());

        Optional<FlightDetailsDTO> result = flightDetailsService.getFlightDetailsById("INVALID");

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetFlightDetailsByFlightId_Success() throws FlightDetailsNotFoundException {
        when(flightDetailsRepository.findByFlightId("123e4567-e89b-12d3-a456-426614174000"))
                .thenReturn(Optional.of(flightDetailsEntity));
        when(flightDetailsMapper.toDTO(flightDetailsEntity)).thenReturn(flightDetailsDTO);

        Optional<FlightDetailsDTO> result = flightDetailsService.getFlightDetailsByFlightId("123e4567-e89b-12d3-a456-426614174000");

        assertTrue(result.isPresent());
        assertEquals("Boeing 737", result.get().getAircraftModel());
        verify(flightDetailsRepository, times(1)).findByFlightId("123e4567-e89b-12d3-a456-426614174000");
    }

    @Test
    void testGetFlightDetailsByFlightId_NotFound() {
        when(flightDetailsRepository.findByFlightId("INVALID")).thenReturn(Optional.empty());

        Optional<FlightDetailsDTO> result = flightDetailsService.getFlightDetailsByFlightId("INVALID");

        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateFlightDetails_Success() throws FlightDetailsNotCreateException {
        when(flightDetailsRepository.existsByFlightId("123e4567-e89b-12d3-a456-426614174000"))
                .thenReturn(false);
        when(flightDetailsRepository.save(any(FlightDetailsEntity.class))).thenReturn(flightDetailsEntity);
        when(flightDetailsMapper.toDTO(flightDetailsEntity)).thenReturn(flightDetailsDTO);

        FlightDetailsDTO result = flightDetailsService.createFlightDetails(flightDetailsDTO);

        assertNotNull(result);
        assertEquals("Boeing 737", result.getAircraftModel());
        verify(flightDetailsRepository, times(1)).save(any(FlightDetailsEntity.class));
    }

    @Test
    void testCreateFlightDetails_AlreadyExists_ThrowsException() {
        when(flightDetailsRepository.existsByFlightId("123e4567-e89b-12d3-a456-426614174000"))
                .thenReturn(true);

        assertThrows(FlightDetailsAlreadyExistsException.class,
                () -> flightDetailsService.createFlightDetails(flightDetailsDTO));
        verify(flightDetailsRepository, never()).save(any());
    }

    @Test
    void testUpdateFlightDetails_Success() throws FlightDetailsNotFoundException {
        FlightDetailsDTO updateDTO = FlightDetailsDTO.builder()
                .aircraftModel("Boeing 747")
                .mealService("Premium meal included")
                .extraBaggagePrice(new BigDecimal("75.00"))
                .build();

        when(flightDetailsRepository.findById("507f1f77bcf86cd799439011"))
                .thenReturn(Optional.of(flightDetailsEntity));
        when(flightDetailsRepository.save(any(FlightDetailsEntity.class))).thenReturn(flightDetailsEntity);
        when(flightDetailsMapper.toDTO(flightDetailsEntity)).thenReturn(flightDetailsDTO);

        FlightDetailsDTO result = flightDetailsService.updateFlightDetails("507f1f77bcf86cd799439011", updateDTO);

        assertNotNull(result);
        verify(flightDetailsRepository, times(1)).save(any(FlightDetailsEntity.class));
    }

    @Test
    void testUpdateFlightDetails_NotFound_ThrowsException() {
        when(flightDetailsRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(FlightDetailsNotFoundException.class,
                () -> flightDetailsService.updateFlightDetails("INVALID", flightDetailsDTO));
    }

    @Test
    void testDeleteFlightDetails_Success() {
        when(flightDetailsRepository.existsById("507f1f77bcf86cd799439011")).thenReturn(true);

        boolean result = flightDetailsService.deleteFlightDetails("507f1f77bcf86cd799439011");

        assertTrue(result);
        verify(flightDetailsRepository, times(1)).deleteById("507f1f77bcf86cd799439011");
    }

    @Test
    void testDeleteFlightDetails_NotFound() {
        when(flightDetailsRepository.existsById("INVALID")).thenReturn(false);

        boolean result = flightDetailsService.deleteFlightDetails("INVALID");

        assertFalse(result);
        verify(flightDetailsRepository, never()).deleteById(any());
    }

    @Test
    void testDeleteFlightDetailsByFlightId_Success() {
        when(flightDetailsRepository.existsByFlightId("123e4567-e89b-12d3-a456-426614174000"))
                .thenReturn(true);

        boolean result = flightDetailsService.deleteFlightDetailsByFlightId("123e4567-e89b-12d3-a456-426614174000");

        assertTrue(result);
        verify(flightDetailsRepository, times(1)).deleteByFlightId("123e4567-e89b-12d3-a456-426614174000");
    }

    @Test
    void testDeleteFlightDetailsByFlightId_NotFound() {
        when(flightDetailsRepository.existsByFlightId("INVALID")).thenReturn(false);

        boolean result = flightDetailsService.deleteFlightDetailsByFlightId("INVALID");

        assertFalse(result);
        verify(flightDetailsRepository, never()).deleteByFlightId(any());
    }

    @Test
    void testGetFlightDetailsByAircraftModel_Success() throws FlightDetailsNotFoundException {
        when(flightDetailsRepository.findByAircraftModel("Boeing 737"))
                .thenReturn(List.of(flightDetailsEntity));
        when(flightDetailsMapper.toDTO(flightDetailsEntity)).thenReturn(flightDetailsDTO);

        List<FlightDetailsDTO> result = flightDetailsService.getFlightDetailsByAircraftModel("Boeing 737");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Boeing 737", result.get(0).getAircraftModel());
        verify(flightDetailsRepository, times(1)).findByAircraftModel("Boeing 737");
    }

    @Test
    void testGetFlightDetailsByMealService_Success() throws FlightDetailsNotFoundException {
        when(flightDetailsRepository.findByMealService("Vegetarian meal included"))
                .thenReturn(List.of(flightDetailsEntity));
        when(flightDetailsMapper.toDTO(flightDetailsEntity)).thenReturn(flightDetailsDTO);

        List<FlightDetailsDTO> result = flightDetailsService.getFlightDetailsByMealService("Vegetarian meal included");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Vegetarian meal included", result.get(0).getMealService());
        verify(flightDetailsRepository, times(1)).findByMealService("Vegetarian meal included");
    }

    @Test
    void testGetFlightDetailsByBaggageOptions_Success() throws FlightDetailsNotFoundException {
        when(flightDetailsRepository.findByIncludesCabinBaggageAndIncludesCheckedBaggage(true, true))
                .thenReturn(List.of(flightDetailsEntity));
        when(flightDetailsMapper.toDTO(flightDetailsEntity)).thenReturn(flightDetailsDTO);

        List<FlightDetailsDTO> result = flightDetailsService.getFlightDetailsByBaggageOptions(true, true);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isIncludesCabinBaggage());
        assertTrue(result.get(0).isIncludesCheckedBaggage());
        verify(flightDetailsRepository, times(1)).findByIncludesCabinBaggageAndIncludesCheckedBaggage(true, true);
    }

    @Test
    void testGetFlightDetailsByWifiAndMeal_Success() throws FlightDetailsNotFoundException {
        when(flightDetailsRepository.findByAvailableWifiConnectionAndMealService(true, "Vegetarian meal included"))
                .thenReturn(List.of(flightDetailsEntity));
        when(flightDetailsMapper.toDTO(flightDetailsEntity)).thenReturn(flightDetailsDTO);

        List<FlightDetailsDTO> result = flightDetailsService.getFlightDetailsByWifiAndMeal(true, "Vegetarian meal included");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getAvailableWifiConnection());
        assertEquals("Vegetarian meal included", result.get(0).getMealService());
        verify(flightDetailsRepository, times(1)).findByAvailableWifiConnectionAndMealService(true, "Vegetarian meal included");
    }

    @Test
    void testGetFlightDetailsByPriceRange_Success() throws FlightDetailsNotFoundException {
        when(flightDetailsRepository.findByExtraBaggagePriceBetween(
                new BigDecimal("40.00"), new BigDecimal("60.00")))
                .thenReturn(List.of(flightDetailsEntity));
        when(flightDetailsMapper.toDTO(flightDetailsEntity)).thenReturn(flightDetailsDTO);

        List<FlightDetailsDTO> result = flightDetailsService.getFlightDetailsByPriceRange(
                new BigDecimal("40.00"), new BigDecimal("60.00"));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(new BigDecimal("50.00"), result.get(0).getExtraBaggagePrice());
        verify(flightDetailsRepository, times(1)).findByExtraBaggagePriceBetween(
                new BigDecimal("40.00"), new BigDecimal("60.00"));
    }

    @Test
    void testGetFlightDetailsByAircraftModel_ThrowsException() {
        when(flightDetailsRepository.findByAircraftModel("Boeing 737"))
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(FlightDetailsNotFoundException.class,
                () -> flightDetailsService.getFlightDetailsByAircraftModel("Boeing 737"));
    }

    @Test
    void testGetFlightDetailsByMealService_ThrowsException() {
        when(flightDetailsRepository.findByMealService("Vegetarian"))
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(FlightDetailsNotFoundException.class,
                () -> flightDetailsService.getFlightDetailsByMealService("Vegetarian"));
    }

    @Test
    void testGetFlightDetailsByBaggageOptions_ThrowsException() {
        when(flightDetailsRepository.findByIncludesCabinBaggageAndIncludesCheckedBaggage(true, true))
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(FlightDetailsNotFoundException.class,
                () -> flightDetailsService.getFlightDetailsByBaggageOptions(true, true));
    }

    @Test
    void testGetFlightDetailsByWifiAndMeal_ThrowsException() {
        when(flightDetailsRepository.findByAvailableWifiConnectionAndMealService(true, "Vegetarian"))
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(FlightDetailsNotFoundException.class,
                () -> flightDetailsService.getFlightDetailsByWifiAndMeal(true, "Vegetarian"));
    }

    @Test
    void testGetFlightDetailsByPriceRange_ThrowsException() {
        when(flightDetailsRepository.findByExtraBaggagePriceBetween(
                new BigDecimal("40.00"), new BigDecimal("60.00")))
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(FlightDetailsNotFoundException.class,
                () -> flightDetailsService.getFlightDetailsByPriceRange(
                        new BigDecimal("40.00"), new BigDecimal("60.00")));
    }

    @Test
    void testDeleteFlightDetails_ExceptionReturnsFlase() {
        when(flightDetailsRepository.existsById("507f1f77bcf86cd799439011"))
                .thenThrow(new RuntimeException("DB error"));

        boolean result = flightDetailsService.deleteFlightDetails("507f1f77bcf86cd799439011");

        assertFalse(result);
    }

    @Test
    void testDeleteFlightDetailsByFlightId_ExceptionReturnsFalse() {
        when(flightDetailsRepository.existsByFlightId("123e4567-e89b-12d3-a456-426614174000"))
                .thenThrow(new RuntimeException("DB error"));

        boolean result = flightDetailsService.deleteFlightDetailsByFlightId("123e4567-e89b-12d3-a456-426614174000");

        assertFalse(result);
    }
    @Test
    void testUpdateFlightDetails_OnlyUpdatesNonNullFields() throws FlightDetailsNotFoundException {
        FlightDetailsDTO partialUpdate = FlightDetailsDTO.builder()
                .aircraftModel("Boeing 747")
                .build();

        when(flightDetailsRepository.findById("507f1f77bcf86cd799439011"))
                .thenReturn(Optional.of(flightDetailsEntity));
        when(flightDetailsRepository.save(any())).thenReturn(flightDetailsEntity);
        when(flightDetailsMapper.toDTO(flightDetailsEntity)).thenReturn(flightDetailsDTO);

        flightDetailsService.updateFlightDetails("507f1f77bcf86cd799439011", partialUpdate);

        verify(flightDetailsRepository, times(1)).save(any());
    }
}

