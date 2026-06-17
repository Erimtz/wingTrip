package com.wingtrip.flight.details.controller.mapper;

import com.wingtrip.flight.details.controller.request.CreateFlightDetailsRequest;
import com.wingtrip.flight.details.controller.request.UpdateFlightDetailsRequest;
import com.wingtrip.flight.details.controller.response.FlightDetailsResponse;
import com.wingtrip.flight.details.dto.FlightDetailsDTO;
import com.wingtrip.flight.details.model.FlightDetailsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface FlightDetailsMapper {

    /**
     * Convierte CreateFlightDetailsRequest a FlightDetailsDTO
     */
    FlightDetailsDTO toDTO(CreateFlightDetailsRequest request);

    /**
     * Convierte UpdateFlightDetailsRequest a FlightDetailsDTO
     */
    FlightDetailsDTO toDTO(UpdateFlightDetailsRequest request);

    /**
     * Convierte CreateFlightDetailsRequest a FlightDetailsEntity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    FlightDetailsEntity toEntity(CreateFlightDetailsRequest request);

    /**
     * Convierte FlightDetailsEntity a FlightDetailsDTO
     */
    FlightDetailsDTO toDTO(FlightDetailsEntity entity);

    /**
     * Convierte FlightDetailsDTO a FlightDetailsResponse
     */
    FlightDetailsResponse toResponse(FlightDetailsDTO dto);

    /**
     * Convierte FlightDetailsEntity a FlightDetailsResponse
     */
    FlightDetailsResponse entityToResponse(FlightDetailsEntity entity);

    /**
     * Actualiza FlightDetailsEntity con datos de UpdateFlightDetailsRequest
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flightId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    void updateEntityFromRequest(UpdateFlightDetailsRequest request, @MappingTarget FlightDetailsEntity entity);

    /**
     * Convierte UpdateFlightDetailsRequest a FlightDetailsEntity
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flightId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    FlightDetailsEntity updateToEntity(UpdateFlightDetailsRequest request);
}
