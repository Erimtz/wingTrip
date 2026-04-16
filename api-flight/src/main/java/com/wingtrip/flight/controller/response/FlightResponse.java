package com.wingtrip.flight.controller.response;

import com.wingtrip.flight.model.ServiceClass;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Response con los datos del vuelo")
public class FlightResponse {

    @Schema(description = "ID único del vuelo", example = "507f1f77bcf86cd799439011")
    private String id;

    @Schema(description = "Número único del vuelo", example = "AA1234")
    private String flightNumber;

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

    @Schema(description = "Precio del vuelo", example = "250.50")
    private BigDecimal price;

    @Schema(description = "Código de moneda", example = "USD")
    private String currency;

    @Schema(description = "Asientos disponibles actualmente", example = "150")
    private int availableSeats;

    @Schema(description = "Total de asientos", example = "200")
    private int totalSeats;

    @Schema(description = "Estado del vuelo", example = "ACTIVE")
    private String status;

    @Schema(description = "Indica si el vuelo es directo", example = "true")
    private boolean isDirect;

    @Schema(description = "Número de paradas", example = "0")
    private int stops;

    @Schema(description = "Clase de servicio", example = "ECONOMY")
    private ServiceClass serviceClass;
}

