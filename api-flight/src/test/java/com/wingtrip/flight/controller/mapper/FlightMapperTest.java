package com.wingtrip.flight.controller.mapper;

import com.wingtrip.flight.controller.request.CreateFlightRequest;
import com.wingtrip.flight.controller.request.UpdateFlightRequest;
import com.wingtrip.flight.controller.response.FlightResponse;
import com.wingtrip.flight.dto.FlightDTO;
import com.wingtrip.flight.model.FlightEntity;
import com.wingtrip.flight.model.FlightStatus;
import com.wingtrip.flight.model.ServiceClass;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
class FlightMapperTest {

    private final FlightMapper flightMapper = new FlightMapperImpl();

    private final LocalDateTime departureTime = LocalDateTime.of(2024, 12, 20, 10, 0, 0);
    private final LocalDateTime arrivalTime = LocalDateTime.of(2024, 12, 20, 12, 30, 0);

    @Test
    void testToDTOFromEntity() {
        FlightEntity entity = FlightEntity.builder()
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

        FlightDTO dto = flightMapper.toDTO(entity);

        assertNotNull(dto);
        assertEquals("1", dto.getId());
        assertEquals("LA123", dto.getFlightNumber());
        assertEquals("LATAM", dto.getAirline());
        assertEquals("MDE", dto.getOriginAirport());
        assertEquals("BOG", dto.getDestinationAirport());
        assertEquals(new BigDecimal("250000.0"), dto.getPrice());
        assertEquals("COP", dto.getCurrency());
        assertEquals(FlightStatus.SCHEDULED, dto.getStatus());
        assertEquals(180, dto.getAvailableSeats());
        assertEquals(180, dto.getTotalSeats());
        assertTrue(dto.isDirectFlight());
        assertEquals(0, dto.getStops());
        assertEquals(ServiceClass.ECONOMY, dto.getServiceClass());
    }

    @Test
    void testToResponseFromDTO() {
        FlightDTO dto = FlightDTO.builder()
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

        FlightResponse response = flightMapper.toResponse(dto);

        assertNotNull(response);
        assertEquals("1", response.getId());
        assertEquals("LA123", response.getFlightNumber());
        assertEquals("LATAM", response.getAirline());
        assertEquals("MDE", response.getOriginAirport());
        assertEquals("BOG", response.getDestinationAirport());
        assertEquals(new BigDecimal("250000.0"), response.getPrice());
        assertEquals("COP", response.getCurrency());
        assertEquals("SCHEDULED", response.getStatus());
        assertEquals(180, response.getAvailableSeats());
        assertTrue(response.isDirectFlight());
        assertEquals(ServiceClass.ECONOMY, response.getServiceClass());
    }

    @Test
    void testToDTOFromCreateRequest() {
        CreateFlightRequest request = CreateFlightRequest.builder()
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

        FlightDTO dto = flightMapper.toDTO(request);

        assertNotNull(dto);
        assertEquals("LA123", dto.getFlightNumber());
        assertEquals("LATAM", dto.getAirline());
        assertEquals("MDE", dto.getOriginAirport());
        assertEquals("BOG", dto.getDestinationAirport());
        assertEquals(new BigDecimal("250000.0"), dto.getPrice());
        assertEquals("COP", dto.getCurrency());
        assertEquals(180, dto.getAvailableSeats());
        assertTrue(dto.isDirectFlight());
        assertEquals(ServiceClass.ECONOMY, dto.getServiceClass());
    }

    @Test
    void testToDTOFromUpdateRequest() {
        UpdateFlightRequest request = UpdateFlightRequest.builder()
                .airline("LATAM Updated")
                .originAirport("BOG")
                .destinationAirport("MDE")
                .price(new BigDecimal("300000.0"))
                .currency("COP")
                .availableSeats(150)
                .totalSeats(180)
                .serviceClass(ServiceClass.BUSINESS)
                .build();

        FlightDTO dto = flightMapper.toDTO(request);

        assertNotNull(dto);
        assertEquals("LATAM Updated", dto.getAirline());
        assertEquals("BOG", dto.getOriginAirport());
        assertEquals(new BigDecimal("300000.0"), dto.getPrice());
        assertEquals(150, dto.getAvailableSeats());
        assertEquals(ServiceClass.BUSINESS, dto.getServiceClass());
    }

    @Test
    void testToEntity_FromCreateRequest() {
        CreateFlightRequest request = CreateFlightRequest.builder()
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

        FlightEntity entity = flightMapper.toEntity(request);

        assertNotNull(entity);
        assertEquals("LA123", entity.getFlightNumber());
        assertEquals("MDE", entity.getOriginAirport());
        assertEquals(FlightStatus.SCHEDULED, entity.getStatus());
        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
    }

    @Test
    void testEntityToResponse() {
        FlightEntity entity = FlightEntity.builder()
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

        FlightResponse response = flightMapper.entityToResponse(entity);

        assertNotNull(response);
        assertEquals("LA123", response.getFlightNumber());
        assertEquals("SCHEDULED", response.getStatus());
        assertEquals("MDE", response.getOriginAirport());
    }

    @Test
    void testUpdateEntityFromRequest() {
        FlightEntity entity = FlightEntity.builder()
                .flightNumber("LA123")
                .airline("LATAM")
                .availableSeats(180)
                .totalSeats(180)
                .build();

        UpdateFlightRequest request = UpdateFlightRequest.builder()
                .airline("LATAM Updated")
                .price(new BigDecimal("300000.0"))
                .availableSeats(150)
                .totalSeats(180)
                .directFlight(true)
                .stops(1)
                .serviceClass(ServiceClass.BUSINESS)
                .build();

        flightMapper.updateEntityFromRequest(request, entity);

        assertEquals("LATAM Updated", entity.getAirline());
        assertEquals(new BigDecimal("300000.0"), entity.getPrice());
        assertEquals(150, entity.getAvailableSeats());
        assertNotNull(entity.getUpdatedAt());
    }

    @Test
    void testUpdateToEntity_WithNullOptionalFields() {
        UpdateFlightRequest request = UpdateFlightRequest.builder()
                .airline("LATAM Updated")
                .price(new BigDecimal("300000.0"))
                .build();

        FlightEntity entity = flightMapper.updateToEntity(request);

        assertNotNull(entity);
        assertEquals("LATAM Updated", entity.getAirline());
        assertEquals(0, entity.getAvailableSeats());
        assertEquals(0, entity.getTotalSeats());
    }

    @Test
    void testUpdateEntityFromRequest_WithNullOptionalFields() {
        FlightEntity entity = FlightEntity.builder()
                .flightNumber("LA123")
                .airline("LATAM")
                .availableSeats(180)
                .totalSeats(180)
                .directFlight(true)
                .stops(2)
                .build();

        UpdateFlightRequest request = UpdateFlightRequest.builder()
                .airline("LATAM Updated")
                .build();

        flightMapper.updateEntityFromRequest(request, entity);

        assertEquals("LATAM Updated", entity.getAirline());
        assertEquals(180, entity.getAvailableSeats());
        assertTrue(entity.isDirectFlight());
        assertEquals(2, entity.getStops());
    }

    @Test
    void testToDTOFromEntity_Null() {
        FlightDTO dto = flightMapper.toDTO((FlightEntity) null);
        assertNull(dto);
    }

    @Test
    void testToResponseFromDTO_Null() {
        FlightResponse response = flightMapper.toResponse(null);
        assertNull(response);
    }

    @Test
    void testToDTOFromCreateRequest_Null() {
        FlightDTO dto = flightMapper.toDTO((CreateFlightRequest) null);
        assertNull(dto);
    }

    @Test
    void testToDTOFromUpdateRequest_Null() {
        FlightDTO dto = flightMapper.toDTO((UpdateFlightRequest) null);
        assertNull(dto);
    }

    @Test
    void testToEntity_Null() {
        FlightEntity entity = flightMapper.toEntity(null);
        assertNull(entity);
    }

    @Test
    void testEntityToResponse_Null() {
        FlightResponse response = flightMapper.entityToResponse(null);
        assertNull(response);
    }

    @Test
    void testUpdateToEntity_Null() {
        FlightEntity entity = flightMapper.updateToEntity(null);
        assertNull(entity);
    }

    @Test
    void testUpdateEntityFromRequest_Null() {
        FlightEntity entity = FlightEntity.builder()
                .flightNumber("LA123")
                .airline("LATAM")
                .build();

        flightMapper.updateEntityFromRequest(null, entity);

        assertEquals("LATAM", entity.getAirline());
    }
}
