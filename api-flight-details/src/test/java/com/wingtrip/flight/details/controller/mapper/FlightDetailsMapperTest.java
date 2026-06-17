package com.wingtrip.flight.details.controller.mapper;

import com.wingtrip.flight.details.controller.request.CreateFlightDetailsRequest;
import com.wingtrip.flight.details.controller.request.UpdateFlightDetailsRequest;
import com.wingtrip.flight.details.controller.response.FlightDetailsResponse;
import com.wingtrip.flight.details.dto.FlightDetailsDTO;
import com.wingtrip.flight.details.model.FlightDetailsEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FlightDetailsMapperTest {

        private final FlightDetailsMapper flightDetailsMapper = Mappers.getMapper(FlightDetailsMapper.class);

        private final LocalDateTime now = LocalDateTime.now();

        // ==================== toDTO(CreateFlightDetailsRequest) ====================

        @Test
        void testToDTOFromCreateRequest_Success() {
            CreateFlightDetailsRequest request = CreateFlightDetailsRequest.builder()
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

            FlightDetailsDTO dto = flightDetailsMapper.toDTO(request);

            assertNotNull(dto);
            assertEquals("Boeing 737", dto.getAircraftModel());
            assertEquals("A12", dto.getGateNumber());
            assertEquals("Terminal 1", dto.getTerminalOrigin());
            assertEquals("Terminal 2", dto.getTerminalDestination());
            assertTrue(dto.isIncludesCabinBaggage());
            assertTrue(dto.isIncludesCheckedBaggage());
            assertEquals(new BigDecimal("50.00"), dto.getExtraBaggagePrice());
            assertEquals("Free seat selection", dto.getSeatSelectionPolicy());
            assertEquals("Vegetarian meal included", dto.getMealService());
            assertTrue(dto.getAvailableWifiConnection());
            assertEquals("Movies, TV shows, music", dto.getEntertainmentOptions());
            assertEquals("123e4567-e89b-12d3-a456-426614174000", dto.getFlightId());
        }

        @Test
        void testToDTOFromCreateRequest_Null() {
            FlightDetailsDTO dto = flightDetailsMapper.toDTO((CreateFlightDetailsRequest) null);
            assertNull(dto);
        }

        @Test
        void testToDTOFromCreateRequest_WithNullOptionalFields() {
            CreateFlightDetailsRequest request = CreateFlightDetailsRequest.builder()
                    .aircraftModel("Boeing 737")
                    .gateNumber("A12")
                    .flightId("123e4567-e89b-12d3-a456-426614174000")
                    .build();

            FlightDetailsDTO dto = flightDetailsMapper.toDTO(request);

            assertNotNull(dto);
            assertEquals("Boeing 737", dto.getAircraftModel());
            assertNull(dto.getMealService());
            assertNull(dto.getSeatSelectionPolicy());
            assertNull(dto.getEntertainmentOptions());
        }

        // ==================== toDTO(UpdateFlightDetailsRequest) ====================

        @Test
        void testToDTOFromUpdateRequest_Success() {
            UpdateFlightDetailsRequest request = UpdateFlightDetailsRequest.builder()
                    .aircraftModel("Boeing 747")
                    .gateNumber("B15")
                    .terminalOrigin("Terminal 3")
                    .terminalDestination("Terminal 4")
                    .includesCabinBaggage(true)
                    .includesCheckedBaggage(false)
                    .extraBaggagePrice(new BigDecimal("75.00"))
                    .mealService("Premium meal")
                    .availableWifiConnection(false)
                    .build();

            FlightDetailsDTO dto = flightDetailsMapper.toDTO(request);

            assertNotNull(dto);
            assertEquals("Boeing 747", dto.getAircraftModel());
            assertEquals("B15", dto.getGateNumber());
            assertEquals(new BigDecimal("75.00"), dto.getExtraBaggagePrice());
            assertEquals("Premium meal", dto.getMealService());
        }

        @Test
        void testToDTOFromUpdateRequest_Null() {
            FlightDetailsDTO dto = flightDetailsMapper.toDTO((UpdateFlightDetailsRequest) null);
            assertNull(dto);
        }

        // ==================== toDTO(FlightDetailsEntity) ====================

        @Test
        void testToDTOFromEntity_Success() {
            FlightDetailsEntity entity = FlightDetailsEntity.builder()
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

            FlightDetailsDTO dto = flightDetailsMapper.toDTO(entity);

            assertNotNull(dto);
            assertEquals("507f1f77bcf86cd799439011", dto.getId());
            assertEquals("Boeing 737", dto.getAircraftModel());
            assertEquals("A12", dto.getGateNumber());
            assertEquals("Terminal 1", dto.getTerminalOrigin());
            assertEquals("Terminal 2", dto.getTerminalDestination());
            assertTrue(dto.isIncludesCabinBaggage());
            assertTrue(dto.isIncludesCheckedBaggage());
            assertEquals(new BigDecimal("50.00"), dto.getExtraBaggagePrice());
            assertEquals(now, dto.getCreatedAt());
            assertEquals(now, dto.getUpdatedAt());
        }

        @Test
        void testToDTOFromEntity_Null() {
            FlightDetailsDTO dto = flightDetailsMapper.toDTO((FlightDetailsEntity) null);
            assertNull(dto);
        }

        @Test
        void testToDTOFromEntity_WithFalseBoolean() {
            FlightDetailsEntity entity = FlightDetailsEntity.builder()
                    .id("507f1f77bcf86cd799439011")
                    .aircraftModel("Boeing 737")
                    .includesCabinBaggage(false)
                    .includesCheckedBaggage(false)
                    .availableWifiConnection(false)
                    .flightId("123e4567-e89b-12d3-a456-426614174000")
                    .build();

            FlightDetailsDTO dto = flightDetailsMapper.toDTO(entity);

            assertNotNull(dto);
            assertFalse(dto.isIncludesCabinBaggage());
            assertFalse(dto.isIncludesCheckedBaggage());
            assertFalse(dto.getAvailableWifiConnection());
        }

        // ==================== toResponse(FlightDetailsDTO) ====================

        @Test
        void testToResponseFromDTO_Success() {
            FlightDetailsDTO dto = FlightDetailsDTO.builder()
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

            FlightDetailsResponse response = flightDetailsMapper.toResponse(dto);

            assertNotNull(response);
            assertEquals("507f1f77bcf86cd799439011", response.getId());
            assertEquals("Boeing 737", response.getAircraftModel());
            assertEquals("A12", response.getGateNumber());
            assertEquals("Terminal 1", response.getTerminalOrigin());
            assertEquals("Terminal 2", response.getTerminalDestination());
            assertTrue(response.isIncludesCabinBaggage());
            assertTrue(response.isIncludesCheckedBaggage());
            assertEquals("50.00", response.getExtraBaggagePrice());
            assertEquals("Free seat selection", response.getSeatSelectionPolicy());
            assertEquals("Vegetarian meal included", response.getMealService());
            assertTrue(response.isAvailableWifiConnection());
            assertEquals("Movies, TV shows, music", response.getEntertainmentOptions());
            assertEquals("123e4567-e89b-12d3-a456-426614174000", response.getFlightId());
            assertEquals(now, response.getCreatedAt());
            assertEquals(now, response.getUpdatedAt());
        }

        @Test
        void testToResponseFromDTO_Null() {
            FlightDetailsResponse response = flightDetailsMapper.toResponse(null);
            assertNull(response);
        }

        @Test
        void testToResponseFromDTO_WithMinimalData() {
            FlightDetailsDTO dto = FlightDetailsDTO.builder()
                    .id("507f1f77bcf86cd799439011")
                    .aircraftModel("Boeing 737")
                    .flightId("123e4567-e89b-12d3-a456-426614174000")
                    .build();

            FlightDetailsResponse response = flightDetailsMapper.toResponse(dto);

            assertNotNull(response);
            assertEquals("507f1f77bcf86cd799439011", response.getId());
            assertEquals("Boeing 737", response.getAircraftModel());
            assertEquals("123e4567-e89b-12d3-a456-426614174000", response.getFlightId());
            assertNull(response.getGateNumber());
            assertNull(response.getTerminalOrigin());
        }

        // ==================== toEntity(CreateFlightDetailsRequest) ====================

        @Test
        void testToEntityFromCreateRequest_Success() {
            CreateFlightDetailsRequest request = CreateFlightDetailsRequest.builder()
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

            FlightDetailsEntity entity = flightDetailsMapper.toEntity(request);

            assertNotNull(entity);
            assertNull(entity.getId());
            assertEquals("Boeing 737", entity.getAircraftModel());
            assertEquals("A12", entity.getGateNumber());
            assertEquals("Terminal 1", entity.getTerminalOrigin());
            assertEquals("Terminal 2", entity.getTerminalDestination());
            assertTrue(entity.isIncludesCabinBaggage());
            assertTrue(entity.isIncludesCheckedBaggage());
            assertEquals(new BigDecimal("50.00"), entity.getExtraBaggagePrice());
            assertEquals("123e4567-e89b-12d3-a456-426614174000", entity.getFlightId());
            assertNotNull(entity.getCreatedAt());
            assertNotNull(entity.getUpdatedAt());
        }

        @Test
        void testToEntityFromCreateRequest_Null() {
            FlightDetailsEntity entity = flightDetailsMapper.toEntity(null);
            assertNull(entity);
        }

        // ==================== entityToResponse(FlightDetailsEntity) ====================

        @Test
        void testEntityToResponse_Success() {
            FlightDetailsEntity entity = FlightDetailsEntity.builder()
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

            FlightDetailsResponse response = flightDetailsMapper.entityToResponse(entity);

            assertNotNull(response);
            assertEquals("507f1f77bcf86cd799439011", response.getId());
            assertEquals("Boeing 737", response.getAircraftModel());
            assertEquals("Terminal 1", response.getTerminalOrigin());
            assertEquals("Terminal 2", response.getTerminalDestination());
        }

        @Test
        void testEntityToResponse_Null() {
            FlightDetailsResponse response = flightDetailsMapper.entityToResponse(null);
            assertNull(response);
        }

        // ==================== updateEntityFromRequest ====================

        @Test
        void testUpdateEntityFromRequest_Success() {
            FlightDetailsEntity entity = FlightDetailsEntity.builder()
                    .id("507f1f77bcf86cd799439011")
                    .aircraftModel("Boeing 737")
                    .gateNumber("A12")
                    .flightId("123e4567-e89b-12d3-a456-426614174000")
                    .createdAt(now)
                    .build();

            UpdateFlightDetailsRequest request = UpdateFlightDetailsRequest.builder()
                    .aircraftModel("Boeing 747")
                    .gateNumber("B15")
                    .terminalOrigin("Terminal 3")
                    .mealService("Premium meal")
                    .availableWifiConnection(false)
                    .build();

            flightDetailsMapper.updateEntityFromRequest(request, entity);

            assertEquals("Boeing 747", entity.getAircraftModel());
            assertEquals("B15", entity.getGateNumber());
            assertEquals("Terminal 3", entity.getTerminalOrigin());
            assertEquals("Premium meal", entity.getMealService());
            assertEquals("507f1f77bcf86cd799439011", entity.getId());
            assertEquals("123e4567-e89b-12d3-a456-426614174000", entity.getFlightId());
            assertNotNull(entity.getUpdatedAt());
        }

        @Test
        void testUpdateEntityFromRequest_Null() {
            FlightDetailsEntity entity = FlightDetailsEntity.builder()
                    .aircraftModel("Boeing 737")
                    .flightId("123e4567-e89b-12d3-a456-426614174000")
                    .build();

            flightDetailsMapper.updateEntityFromRequest(null, entity);

            assertEquals("Boeing 737", entity.getAircraftModel());
        }

        // ==================== updateToEntity ====================

        @Test
        void testUpdateToEntity_Success() {
            UpdateFlightDetailsRequest request = UpdateFlightDetailsRequest.builder()
                    .aircraftModel("Boeing 747")
                    .gateNumber("B15")
                    .extraBaggagePrice(new BigDecimal("75.00"))
                    .mealService("Premium meal")
                    .availableWifiConnection(true)
                    .build();

            FlightDetailsEntity entity = flightDetailsMapper.updateToEntity(request);

            assertNotNull(entity);
            assertEquals("Boeing 747", entity.getAircraftModel());
            assertEquals("B15", entity.getGateNumber());
            assertEquals(new BigDecimal("75.00"), entity.getExtraBaggagePrice());
            assertEquals("Premium meal", entity.getMealService());
            assertNull(entity.getId());
            assertNull(entity.getFlightId());
            assertNotNull(entity.getUpdatedAt());
        }

        @Test
        void testUpdateToEntity_Null() {
            FlightDetailsEntity entity = flightDetailsMapper.updateToEntity(null);
            assertNull(entity);
        }

        @Test
        void testUpdateToEntity_WithNullOptionalFields() {
            UpdateFlightDetailsRequest request = UpdateFlightDetailsRequest.builder()
                    .aircraftModel("Boeing 747")
                    .build();

            FlightDetailsEntity entity = flightDetailsMapper.updateToEntity(request);

            assertNotNull(entity);
            assertEquals("Boeing 747", entity.getAircraftModel());
            assertNull(entity.getGateNumber());
            assertNull(entity.getMealService());
        }
    @Test
    void testUpdateEntityFromRequest_WithNullBooleanFields() {
        FlightDetailsEntity entity = FlightDetailsEntity.builder()
                .aircraftModel("Boeing 737")
                .includesCabinBaggage(true)
                .includesCheckedBaggage(true)
                .availableWifiConnection(true)
                .flightId("123e4567-e89b-12d3-a456-426614174000")
                .build();

        UpdateFlightDetailsRequest request = UpdateFlightDetailsRequest.builder()
                .aircraftModel("Boeing 747")
                // includesCabinBaggage, includesCheckedBaggage, availableWifiConnection → null
                .build();

        flightDetailsMapper.updateEntityFromRequest(request, entity);

        assertEquals("Boeing 747", entity.getAircraftModel());
        assertTrue(entity.isIncludesCabinBaggage());
        assertTrue(entity.isIncludesCheckedBaggage());
        assertTrue(entity.getAvailableWifiConnection());
    }

    @Test
    void testUpdateToEntity_WithAllNullFields() {
        UpdateFlightDetailsRequest request = UpdateFlightDetailsRequest.builder()
                .build();

        FlightDetailsEntity entity = flightDetailsMapper.updateToEntity(request);

        assertNotNull(entity);
        assertNull(entity.getAircraftModel());
        assertNull(entity.getGateNumber());
        assertNull(entity.getMealService());
        assertFalse(entity.isIncludesCabinBaggage()); // default boolean
    }
}

