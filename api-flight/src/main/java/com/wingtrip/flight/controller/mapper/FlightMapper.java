package com.wingtrip.flight.controller.mapper;

import com.wingtrip.flight.controller.request.CreateFlightRequest;
import com.wingtrip.flight.dto.FlightDTO;
import com.wingtrip.flight.controller.response.FlightResponse;
import com.wingtrip.flight.controller.request.UpdateFlightRequest;
import com.wingtrip.flight.model.FlightEntity;
import com.wingtrip.flight.model.FlightStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface FlightMapper {

    /**
     * Convierte CreateFlightRequest a FlightDTO
     */
    FlightDTO toDTO(CreateFlightRequest request);

    /**
     * Convierte UpdateFlightRequest a FlightDTO
     */
    FlightDTO toDTO(UpdateFlightRequest request);

    /**
     * Convierte CreateFlightRequest a FlightEntity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(getDefaultFlightStatus())")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    FlightEntity toEntity(CreateFlightRequest request);

    /**
     * Convierte FlightEntity a FlightDTO
     */
    FlightDTO toDTO(FlightEntity entity);

    /**
     * Convierte FlightDTO a FlightResponse
     */
    @Mapping(target = "status", source = "status")
    FlightResponse toResponse(FlightDTO dto);

    /**
     * Convierte FlightEntity a FlightResponse
     */
    @Mapping(target = "status", source = "status")
    FlightResponse entityToResponse(FlightEntity entity);

    /**
     * Actualiza una entidad FlightEntity con datos de UpdateFlightRequest
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flightNumber", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    void updateEntityFromRequest(UpdateFlightRequest request, @MappingTarget FlightEntity entity);

    /**
     * Convierte UpdateFlightRequest a FlightEntity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flightNumber", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    FlightEntity updateToEntity(UpdateFlightRequest request);

    /**
     * Convierte FlightStatus enum a String
     */
    default String map(FlightStatus status) {
        return status != null ? status.name() : null;
    }

    /**
     * Retorna el estado de vuelo por defecto
     */
    default FlightStatus getDefaultFlightStatus() {
        return FlightStatus.SCHEDULED;
    }
}

