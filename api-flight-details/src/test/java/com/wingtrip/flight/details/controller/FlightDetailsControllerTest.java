package com.wingtrip.flight.details.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wingtrip.flight.details.controller.mapper.FlightDetailsMapper;
import com.wingtrip.flight.details.controller.request.CreateFlightDetailsRequest;
import com.wingtrip.flight.details.controller.response.FlightDetailsResponse;
import com.wingtrip.flight.details.controller.errorhandling.GlobalExceptionHandler;
import com.wingtrip.flight.details.dto.FlightDetailsDTO;
import com.wingtrip.flight.details.exception.FlightDetailsNotFoundException;
import com.wingtrip.flight.details.service.FlightDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FlightDetailsControllerTest {

    @Mock
    private FlightDetailsService flightDetailsService;

    @Mock
    private FlightDetailsMapper flightDetailsMapper;

    @InjectMocks
    private FlightDetailsController flightDetailsController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private FlightDetailsDTO flightDetailsDTO;
    private FlightDetailsResponse flightDetailsResponse;
    private CreateFlightDetailsRequest createRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(flightDetailsController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        LocalDateTime now = LocalDateTime.now();

        createRequest = CreateFlightDetailsRequest.builder()
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

        flightDetailsResponse = FlightDetailsResponse.builder()
                .id("507f1f77bcf86cd799439011")
                .aircraftModel("Boeing 737")
                .gateNumber("A12")
                .terminalOrigin("Terminal 1")
                .terminalDestination("Terminal 2")
                .includesCabinBaggage(true)
                .includesCheckedBaggage(true)
                .extraBaggagePrice("50.00")
                .seatSelectionPolicy("Free seat selection")
                .mealService("Vegetarian meal included")
                .availableWifiConnection(true)
                .entertainmentOptions("Movies, TV shows, music")
                .flightId("123e4567-e89b-12d3-a456-426614174000")
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    // ==================== GET All Tests ====================

    @Test
    void testGetAllFlightDetails_Success() throws Exception {
        when(flightDetailsService.getAllFlightDetails()).thenReturn(List.of(flightDetailsDTO));
        when(flightDetailsMapper.toResponse(flightDetailsDTO)).thenReturn(flightDetailsResponse);

        mockMvc.perform(get("/api/v1/flight-details")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("507f1f77bcf86cd799439011"))
                .andExpect(jsonPath("$[0].aircraftModel").value("Boeing 737"));

        verify(flightDetailsService, times(1)).getAllFlightDetails();
    }

    @Test
    void testGetAllFlightDetails_Empty() throws Exception {
        when(flightDetailsService.getAllFlightDetails()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/flight-details")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(flightDetailsService, times(1)).getAllFlightDetails();
    }

    // ==================== GET By ID Tests ====================

    @Test
    void testGetFlightDetailsById_Success() throws Exception {
        when(flightDetailsService.getFlightDetailsById("507f1f77bcf86cd799439011"))
                .thenReturn(Optional.of(flightDetailsDTO));
        when(flightDetailsMapper.toResponse(flightDetailsDTO)).thenReturn(flightDetailsResponse);

        mockMvc.perform(get("/api/v1/flight-details/507f1f77bcf86cd799439011")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("507f1f77bcf86cd799439011"))
                .andExpect(jsonPath("$.aircraftModel").value("Boeing 737"));

        verify(flightDetailsService, times(1)).getFlightDetailsById("507f1f77bcf86cd799439011");
    }

    @Test
    void testGetFlightDetailsById_NotFound() throws Exception {
        when(flightDetailsService.getFlightDetailsById("INVALID"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/flight-details/INVALID")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(flightDetailsService, times(1)).getFlightDetailsById("INVALID");
    }

    // ==================== GET By Flight ID Tests ====================

    @Test
    void testGetFlightDetailsByFlightId_Success() throws Exception {
        when(flightDetailsService.getFlightDetailsByFlightId("123e4567-e89b-12d3-a456-426614174000"))
                .thenReturn(Optional.of(flightDetailsDTO));
        when(flightDetailsMapper.toResponse(flightDetailsDTO)).thenReturn(flightDetailsResponse);

        mockMvc.perform(get("/api/v1/flight-details/flight/123e4567-e89b-12d3-a456-426614174000")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightId").value("123e4567-e89b-12d3-a456-426614174000"));

        verify(flightDetailsService, times(1)).getFlightDetailsByFlightId("123e4567-e89b-12d3-a456-426614174000");
    }

    @Test
    void testGetFlightDetailsByFlightId_NotFound() throws Exception {
        when(flightDetailsService.getFlightDetailsByFlightId("INVALID"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/flight-details/flight/INVALID")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // ==================== POST Create Tests ====================

    @Test
    void testCreateFlightDetails_Success() throws Exception {
        when(flightDetailsMapper.toDTO(createRequest)).thenReturn(flightDetailsDTO);
        when(flightDetailsService.createFlightDetails(flightDetailsDTO)).thenReturn(flightDetailsDTO);
        when(flightDetailsMapper.toResponse(flightDetailsDTO)).thenReturn(flightDetailsResponse);

        mockMvc.perform(post("/api/v1/flight-details")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.aircraftModel").value("Boeing 737"))
                .andExpect(jsonPath("$.flightId").value("123e4567-e89b-12d3-a456-426614174000"));

        verify(flightDetailsService, times(1)).createFlightDetails(any(FlightDetailsDTO.class));
    }

    @Test
    void testCreateFlightDetails_InvalidData() throws Exception {
        CreateFlightDetailsRequest invalidRequest = CreateFlightDetailsRequest.builder()
                .aircraftModel("") // Empty required field
                .flightId("123e4567-e89b-12d3-a456-426614174000")
                .build();

        mockMvc.perform(post("/api/v1/flight-details")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(flightDetailsService, never()).createFlightDetails(any());
    }

    // ==================== PUT Update Tests ====================

    @Test
    void testUpdateFlightDetails_Success() throws Exception {
        when(flightDetailsMapper.toDTO(createRequest)).thenReturn(flightDetailsDTO);
        when(flightDetailsService.updateFlightDetails("507f1f77bcf86cd799439011", flightDetailsDTO))
                .thenReturn(flightDetailsDTO);
        when(flightDetailsMapper.toResponse(flightDetailsDTO)).thenReturn(flightDetailsResponse);

        mockMvc.perform(put("/api/v1/flight-details/507f1f77bcf86cd799439011")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("507f1f77bcf86cd799439011"));

        verify(flightDetailsService, times(1)).updateFlightDetails(
                eq("507f1f77bcf86cd799439011"), any(FlightDetailsDTO.class));
    }

    @Test
    void testUpdateFlightDetails_NotFound() throws Exception {
        when(flightDetailsMapper.toDTO(createRequest)).thenReturn(flightDetailsDTO);
        when(flightDetailsService.updateFlightDetails("INVALID", flightDetailsDTO))
                .thenThrow(new FlightDetailsNotFoundException("Flight details not found"));

        mockMvc.perform(put("/api/v1/flight-details/INVALID")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isNotFound());

        verify(flightDetailsService, times(1)).updateFlightDetails(
                eq("INVALID"), any(FlightDetailsDTO.class));
    }

    // ==================== DELETE Tests ====================

    @Test
    void testDeleteFlightDetails_Success() throws Exception {
        when(flightDetailsService.deleteFlightDetails("507f1f77bcf86cd799439011")).thenReturn(true);

        mockMvc.perform(delete("/api/v1/flight-details/507f1f77bcf86cd799439011")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(flightDetailsService, times(1)).deleteFlightDetails("507f1f77bcf86cd799439011");
    }

    @Test
    void testDeleteFlightDetails_NotFound() throws Exception {
        when(flightDetailsService.deleteFlightDetails("INVALID")).thenReturn(false);

        mockMvc.perform(delete("/api/v1/flight-details/INVALID")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(flightDetailsService, times(1)).deleteFlightDetails("INVALID");
    }

    @Test
    void testDeleteFlightDetailsByFlightId_Success() throws Exception {
        when(flightDetailsService.deleteFlightDetailsByFlightId("123e4567-e89b-12d3-a456-426614174000"))
                .thenReturn(true);

        mockMvc.perform(delete("/api/v1/flight-details/flight/123e4567-e89b-12d3-a456-426614174000")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(flightDetailsService, times(1)).deleteFlightDetailsByFlightId("123e4567-e89b-12d3-a456-426614174000");
    }

    // ==================== Search by Baggage Options Tests ====================

    @Test
    void testGetFlightDetailsByBaggageOptions_Success() throws Exception {
        when(flightDetailsService.getFlightDetailsByBaggageOptions(true, true))
                .thenReturn(List.of(flightDetailsDTO));
        when(flightDetailsMapper.toResponse(flightDetailsDTO)).thenReturn(flightDetailsResponse);

        mockMvc.perform(get("/api/v1/flight-details/search/baggage")
                .param("cabinBaggage", "true")
                .param("checkedBaggage", "true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].includesCabinBaggage").value(true))
                .andExpect(jsonPath("$[0].includesCheckedBaggage").value(true));

        verify(flightDetailsService, times(1)).getFlightDetailsByBaggageOptions(true, true);
    }

    // ==================== Search by Amenities Tests ====================

    @Test
    void testGetFlightDetailsByWifiAndMeal_Success() throws Exception {
        when(flightDetailsService.getFlightDetailsByWifiAndMeal(true, "Vegetarian meal included"))
                .thenReturn(List.of(flightDetailsDTO));
        when(flightDetailsMapper.toResponse(flightDetailsDTO)).thenReturn(flightDetailsResponse);

        mockMvc.perform(get("/api/v1/flight-details/search/amenities")
                .param("wifi", "true")
                .param("mealService", "Vegetarian meal included")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].availableWifiConnection").value(true))
                .andExpect(jsonPath("$[0].mealService").value("Vegetarian meal included"));

        verify(flightDetailsService, times(1)).getFlightDetailsByWifiAndMeal(true, "Vegetarian meal included");
    }

    // ==================== Search by Price Range Tests ====================

    @Test
    void testGetFlightDetailsByPriceRange_Success() throws Exception {
        when(flightDetailsService.getFlightDetailsByPriceRange(
                new BigDecimal("40.00"), new BigDecimal("60.00")))
                .thenReturn(List.of(flightDetailsDTO));
        when(flightDetailsMapper.toResponse(flightDetailsDTO)).thenReturn(flightDetailsResponse);

        mockMvc.perform(get("/api/v1/flight-details/search/price")
                .param("minPrice", "40.00")
                .param("maxPrice", "60.00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].extraBaggagePrice").value("50.00"));

        verify(flightDetailsService, times(1)).getFlightDetailsByPriceRange(
                new BigDecimal("40.00"), new BigDecimal("60.00"));
    }

    // ==================== Search by Aircraft Model Tests ====================

    @Test
    void testGetFlightDetailsByAircraftModel_Success() throws Exception {
        when(flightDetailsService.getFlightDetailsByAircraftModel("Boeing 737"))
                .thenReturn(List.of(flightDetailsDTO));
        when(flightDetailsMapper.toResponse(flightDetailsDTO)).thenReturn(flightDetailsResponse);

        mockMvc.perform(get("/api/v1/flight-details/search/aircraft")
                .param("aircraftModel", "Boeing 737")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].aircraftModel").value("Boeing 737"));

        verify(flightDetailsService, times(1)).getFlightDetailsByAircraftModel("Boeing 737");
    }

    // ==================== Search by Meal Service Tests ====================

    @Test
    void testGetFlightDetailsByMealService_Success() throws Exception {
        when(flightDetailsService.getFlightDetailsByMealService("Vegetarian meal included"))
                .thenReturn(List.of(flightDetailsDTO));
        when(flightDetailsMapper.toResponse(flightDetailsDTO)).thenReturn(flightDetailsResponse);

        mockMvc.perform(get("/api/v1/flight-details/search/meal")
                .param("mealService", "Vegetarian meal included")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].mealService").value("Vegetarian meal included"));

        verify(flightDetailsService, times(1)).getFlightDetailsByMealService("Vegetarian meal included");
    }
}

