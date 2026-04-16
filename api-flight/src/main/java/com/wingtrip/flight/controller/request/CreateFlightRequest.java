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
@Schema(description = "Request para crear un nuevo vuelo")
public class CreateFlightRequest {

    @NotBlank(message = "El número de vuelo es requerido")
    @Schema(description = "Número único del vuelo", example = "AA1234")
    private String flightNumber;

    @NotBlank(message = "La aerolínea es requerida")
    @Schema(description = "Nombre de la aerolínea", example = "American Airlines")
    private String airline;

    @NotBlank(message = "El aeropuerto de origen es requerido")
    @Schema(description = "Código del aeropuerto de origen", example = "JFK")
    private String originAirport;

    @NotBlank(message = "El aeropuerto de destino es requerido")
    @Schema(description = "Código del aeropuerto de destino", example = "LAX")
    private String destinationAirport;

    @NotNull(message = "La fecha de salida es requerida")
    @Schema(description = "Fecha y hora de salida", example = "2026-08-15T10:30:00")
    private LocalDateTime departureTime;

    @NotNull(message = "La fecha de llegada es requerida")
    @Schema(description = "Fecha y hora de llegada", example = "2026-08-15T13:30:00")
    private LocalDateTime arrivalTime;

    @NotNull(message = "El precio es requerido")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Schema(description = "Precio del vuelo", example = "250.50")
    private BigDecimal price;

    @NotBlank(message = "La moneda es requerida")
    @Schema(description = "Código de moneda", example = "USD")
    private String currency;

    @Min(value = 1, message = "El total de asientos debe ser al menos 1")
    @Schema(description = "Total de asientos disponibles", example = "200")
    private int totalSeats;

    @Min(value = 1, message = "Los asientos disponibles deben ser al menos 1")
    @Schema(description = "Asientos disponibles actualmente", example = "150")
    private int availableSeats;

    @Schema(description = "Indica si el vuelo es directo", example = "true")
    private boolean isDirect;

    @Min(value = 0, message = "El número de paradas no puede ser negativo")
    @Schema(description = "Número de paradas", example = "0")
    private int stops;

    @NotNull(message = "La clase de servicio es requerida")
    @Schema(description = "Clase de servicio", example = "ECONOMY")
    private ServiceClass serviceClass;
}

