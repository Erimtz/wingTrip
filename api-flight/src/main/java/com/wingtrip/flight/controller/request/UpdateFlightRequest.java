package com.wingtrip.flight.controller.request;

import com.wingtrip.flight.model.ServiceClass;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request para actualizar un vuelo existente")
public class UpdateFlightRequest {

    @Schema(description = "Nombre de la aerolínea", example = "American Airlines")
    private String airline;

    @Schema(description = "Código del aeropuerto de origen", example = "JFK")
    private String originAirport;

    @Schema(description = "Código del aeropuerto de destino", example = "LAX")
    private String destinationAirport;

    @Schema(description = "Fecha y hora de salida", example = "2026-08-15T10:30:00")
    private LocalDateTime departureTime;

    @Schema(description = "Fecha y hora de llegada", example = "2026-08-15T13:30:00")
    private LocalDateTime arrivalTime;

    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Schema(description = "Precio del vuelo", example = "250.50")
    private BigDecimal price;

    @Schema(description = "Código de moneda", example = "USD")
    private String currency;

    @Min(value = 0, message = "Los asientos disponibles no pueden ser negativos")
    @Schema(description = "Asientos disponibles actualmente", example = "150")
    private Integer availableSeats;

    @Min(value = 1, message = "El total de asientos debe ser al menos 1")
    @Schema(description = "Total de asientos disponibles", example = "200")
    private Integer totalSeats;

    @Schema(description = "Indica si el vuelo es directo", example = "true")
    private Boolean isDirect;

    @Min(value = 0, message = "El número de paradas no puede ser negativo")
    @Schema(description = "Número de paradas", example = "0")
    private Integer stops;

    @Schema(description = "Clase de servicio", example = "ECONOMY")
    private ServiceClass serviceClass;
}

