package com.wingtrip.flight.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wingtrip.flight.controller.mapper.FlightMapper;
import com.wingtrip.flight.controller.request.CreateFlightRequest;
import com.wingtrip.flight.controller.request.UpdateFlightRequest;
import com.wingtrip.flight.controller.response.FlightResponse;
import com.wingtrip.flight.dto.FlightDTO;
import com.wingtrip.flight.controller.errorhandling.GlobalExceptionHandler;
import com.wingtrip.flight.model.ServiceClass;
import com.wingtrip.flight.service.FlightService;
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
class FlightControllerTest {

    @Mock
    private FlightService flightService;

    @Mock
    private FlightMapper flightMapper;

    @InjectMocks
    private FlightController flightController;

    private MockMvc mockMvc;
    private FlightDTO flightDTO;
    private FlightResponse flightResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(flightController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        LocalDateTime departureTime = LocalDateTime.of(2024, 12, 20, 10, 0, 0);
        LocalDateTime arrivalTime = LocalDateTime.of(2024, 12, 20, 12, 30, 0);

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

        flightResponse = FlightResponse.builder()
                .id("1")
                .flightNumber("LA123")
                .airline("LATAM")
                .originAirport("MDE")
                .destinationAirport("BOG")
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
    void testGetAllFlights() throws Exception {
        when(flightService.getAllFlights()).thenReturn(List.of(flightDTO));
        when(flightMapper.toResponse(flightDTO)).thenReturn(flightResponse);

        mockMvc.perform(get("/api/v1/flights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"));

        verify(flightService, times(1)).getAllFlights();
    }

    @Test
    void testGetFlightById() throws Exception {
        when(flightService.getFlightById("LA123")).thenReturn(Optional.of(flightDTO));
        when(flightMapper.toResponse(flightDTO)).thenReturn(flightResponse);

        mockMvc.perform(get("/api/v1/flights/LA123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));

        verify(flightService, times(1)).getFlightById("LA123");
    }

    @Test
    void testGetFlightById_NotFound() throws Exception {
        when(flightService.getFlightById("INVALID")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/flights/INVALID"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSearchFlights() throws Exception {
        when(flightService.searchFlights("MDE", "BOG", "2024-12-20"))
                .thenReturn(List.of(flightDTO));
        when(flightMapper.toResponse(flightDTO)).thenReturn(flightResponse);

        mockMvc.perform(get("/api/v1/flights/search")
                        .param("origin", "MDE")
                        .param("destination", "BOG")
                        .param("departureDate", "2024-12-20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].flightNumber").value("LA123"));
    }

    @Test
    void testSearchFlights_Empty() throws Exception {
        when(flightService.searchFlights("MDE", "BOG", "2024-12-20"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/flights/search")
                        .param("origin", "MDE")
                        .param("destination", "BOG")
                        .param("departureDate", "2024-12-20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testCreateFlight() throws Exception {
        CreateFlightRequest createRequest = CreateFlightRequest.builder()
                .flightNumber("LA123")
                .airline("LATAM")
                .originAirport("MDE")
                .destinationAirport("BOG")
                .departureTime(LocalDateTime.of(2024, 12, 20, 10, 0))
                .arrivalTime(LocalDateTime.of(2024, 12, 20, 12, 30))
                .price(new BigDecimal("250000.0"))
                .currency("COP")
                .availableSeats(180)
                .totalSeats(180)
                .directFlight(true)
                .stops(0)
                .serviceClass(ServiceClass.ECONOMY)
                .build();

        when(flightMapper.toDTO(any(CreateFlightRequest.class))).thenReturn(flightDTO);
        when(flightService.createFlight(flightDTO)).thenReturn(flightDTO);
        when(flightMapper.toResponse(flightDTO)).thenReturn(flightResponse);

        mockMvc.perform(post("/api/v1/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule())
                                .writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.flightNumber").value("LA123"));
    }

    @Test
    void testUpdateFlight() throws Exception {
        when(flightMapper.toDTO(any(UpdateFlightRequest.class))).thenReturn(flightDTO);
        when(flightService.updateFlight(eq("LA123"), any(FlightDTO.class))).thenReturn(flightDTO);
        when(flightMapper.toResponse(flightDTO)).thenReturn(flightResponse);

        mockMvc.perform(put("/api/v1/flights/LA123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"airline\":\"LATAM Updated\",\"price\":300000.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightNumber").value("LA123"));
    }

    @Test
    void testDeleteFlight_Success() throws Exception {
        when(flightService.deleteFlight("LA123")).thenReturn(true);

        mockMvc.perform(delete("/api/v1/flights/LA123"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteFlight_NotFound() throws Exception {
        when(flightService.deleteFlight("INVALID")).thenReturn(false);

        mockMvc.perform(delete("/api/v1/flights/INVALID"))
                .andExpect(status().isNotFound());
    }
}
