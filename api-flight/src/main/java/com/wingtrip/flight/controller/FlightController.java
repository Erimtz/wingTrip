package com.wingtrip.flight.controller;

import com.wingtrip.flight.controller.mapper.FlightMapper;
import com.wingtrip.flight.controller.request.CreateFlightRequest;
import com.wingtrip.flight.controller.response.FlightResponse;
import com.wingtrip.flight.controller.request.UpdateFlightRequest;
import com.wingtrip.flight.dto.FlightDTO;
import com.wingtrip.flight.exception.FlightNotCreatedException;
import com.wingtrip.flight.exception.FlightNotFoundException;
import com.wingtrip.flight.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/flights")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Flight Management", description = "Flight management operations")
public class FlightController {

    private final FlightService flightService;
    private final FlightMapper flightMapper;

    /**
     * Get all available flights
     */
    @GetMapping
    @Operation(summary = "Get all flights", description = "Returns a list of all available flights")
    @ApiResponse(responseCode = "200", description = "Flight list successfully obtained",
            content = @Content(schema = @Schema(implementation = FlightResponse.class)))
    public ResponseEntity<List<FlightResponse>> getAllFlights() throws FlightNotFoundException {
        log.info("Getting all flights");
        List<FlightDTO> flights = flightService.getAllFlights();
        List<FlightResponse> responses = flights.stream()
                .map(flightMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Flights were obtained {}", responses.size());
        return ResponseEntity.ok(responses);
    }

    /**
     * You get a specific flight based on your number.
     */
    @GetMapping("/{flightNumber}")
    @Operation(summary = "Get flight by number", description = "You get a specific flight using your flight number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flight found",
                    content = @Content(schema = @Schema(implementation = FlightResponse.class))),
            @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    public ResponseEntity<FlightResponse> getFlightById(@PathVariable String flightNumber) throws FlightNotFoundException {
        log.info("Obtaining flight number: {}", flightNumber);
        FlightDTO flight = flightService.getFlightById(flightNumber)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found"));
        FlightResponse response = flightMapper.toResponse(flight);
        log.info("Flight found: {}", flightNumber);
        return ResponseEntity.ok(response);
    }

    /**
     * Search for flights by origin, destination, and date
     */
    @GetMapping("/search")
    @Operation(summary = "Search for flights", description = "Search for flights by origin, destination, and departure date")
    @ApiResponse(responseCode = "200", description = "Vuelos encontrados",
            content = @Content(schema = @Schema(implementation = FlightResponse.class)))
    public ResponseEntity<List<FlightResponse>> searchFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam String departureDate) throws FlightNotFoundException {
        log.info("Searching for flights: origin={}, destination={}, date={}", origin, destination, departureDate);
        List<FlightDTO> flights = flightService.searchFlights(origin, destination, departureDate);
        List<FlightResponse> responses = flights.stream()
                .map(flightMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Flights were found {}", responses.size());
        return ResponseEntity.ok(responses);
    }

    /**
     * Create a new flight
     */
    @PostMapping
    @Operation(summary = "Create new flight", description = "Create a new flight in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Flight successfully created",
                    content = @Content(schema = @Schema(implementation = FlightResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    public ResponseEntity<FlightResponse> createFlight(@Valid @RequestBody CreateFlightRequest request) throws FlightNotCreatedException {
        log.info("Creating a new flight: {}", request.getFlightNumber());
        FlightDTO flightDTO = flightMapper.toDTO(request);
        FlightDTO createdFlight = flightService.createFlight(flightDTO);
        FlightResponse response = flightMapper.toResponse(createdFlight);
        log.info("Flight successfully created: {}", response.getFlightNumber());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update an existing flight
     */
    @PutMapping("/{flightNumber}")
    @Operation(summary = "Flight Update", description = "Update an existing flight")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flight updated successfully",
                    content = @Content(schema = @Schema(implementation = FlightResponse.class))),
            @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    public ResponseEntity<FlightResponse> updateFlight(
            @PathVariable String flightNumber,
            @Valid @RequestBody UpdateFlightRequest request) throws FlightNotFoundException {
        log.info("Updating flight: {}", flightNumber);
        FlightDTO flightDTO = flightMapper.toDTO(request);
        FlightDTO updatedFlight = flightService.updateFlight(flightNumber, flightDTO);
        FlightResponse response = flightMapper.toResponse(updatedFlight);
        log.info("Flight successfully updated: {}", flightNumber);
        return ResponseEntity.ok(response);
    }

    /**
     * Cancel a flight
     */
    @DeleteMapping("/{flightNumber}")
    @Operation(summary = "Cancel flight", description = "Remove a flight from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Flight successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    public ResponseEntity<Void> deleteFlight(@PathVariable String flightNumber) {
        log.info("Eliminating flight: {}", flightNumber);
        boolean deleted = flightService.deleteFlight(flightNumber);
        if (deleted) {
            log.info("Flight successfully removed: {}", flightNumber);
            return ResponseEntity.noContent().build();
        }
        log.warn("Flight not found to delete: {}", flightNumber);
        return ResponseEntity.notFound().build();
    }
}

