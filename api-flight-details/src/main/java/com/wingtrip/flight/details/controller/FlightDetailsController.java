package com.wingtrip.flight.details.controller;

import com.wingtrip.flight.details.controller.mapper.FlightDetailsMapper;
import com.wingtrip.flight.details.controller.request.CreateFlightDetailsRequest;
import com.wingtrip.flight.details.controller.response.FlightDetailsResponse;
import com.wingtrip.flight.details.dto.FlightDetailsDTO;
import com.wingtrip.flight.details.exception.FlightDetailsNotCreateException;
import com.wingtrip.flight.details.exception.FlightDetailsNotFoundException;
import com.wingtrip.flight.details.service.FlightDetailsService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/flight-details")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Flight Details API", description = "API for managing flight details")
public class FlightDetailsController {

    private final FlightDetailsService flightDetailsService;
    private final FlightDetailsMapper flightDetailsMapper;

    @GetMapping
    @Operation(summary = "Get all flight details", description = "Retrieve a list of all flight details")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved flight details",
            content = @Content(schema = @Schema(implementation = FlightDetailsResponse.class)))
    public ResponseEntity<List<FlightDetailsResponse>> getAllFlightDetails() throws FlightDetailsNotFoundException {
        log.info("Received request to get all flight details");
        List<FlightDetailsDTO> detailsDTOS = flightDetailsService.getAllFlightDetails();
        List<FlightDetailsResponse> responses = detailsDTOS.stream()
                .map(flightDetailsMapper::toResponse)
                .toList();
        log.info("Successfully retrieved {} flight details", responses.size());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get flight details by ID", description = "Returns flight details by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flight details found",
                    content = @Content(schema = @Schema(implementation = FlightDetailsResponse.class))),
            @ApiResponse(responseCode = "404", description = "Flight details not found")
    })
    public ResponseEntity<FlightDetailsResponse> getFlightDetailsById(@PathVariable String id) throws FlightDetailsNotFoundException {
        log.info("Getting flight details by id: {}", id);
        FlightDetailsDTO details = flightDetailsService.getFlightDetailsById(id)
                .orElseThrow(() -> new FlightDetailsNotFoundException("Flight details not found with id: " + id));
        FlightDetailsResponse response = flightDetailsMapper.toResponse(details);
        log.info("Flight details found for id: {}", id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/flight/{flightId}")
    @Operation(summary = "Get flight details by flight ID", description = "Returns flight details by its associated flight ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flight details found",
                    content = @Content(schema = @Schema(implementation = FlightDetailsResponse.class))),
            @ApiResponse(responseCode = "404", description = "Flight details not found")
    })
    public ResponseEntity<FlightDetailsResponse> getFlightDetailsByFlightId(@PathVariable String flightId) throws FlightDetailsNotFoundException {
        log.info("Getting flight details by flight id: {}", flightId);
        FlightDetailsDTO details = flightDetailsService.getFlightDetailsByFlightId(flightId)
                .orElseThrow(() -> new FlightDetailsNotFoundException("Flight details not found with flight id: " + flightId));
        FlightDetailsResponse response = flightDetailsMapper.toResponse(details);
        log.info("Flight details found for flight id: {}", flightId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/baggage")
    @Operation(summary = "Search by baggage options", description = "Search flight details by cabin and checked baggage options")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flight details found",
                    content = @Content(schema = @Schema(implementation = FlightDetailsResponse.class))),
            @ApiResponse(responseCode = "404", description = "Flight details not found")
    })
    public ResponseEntity<List<FlightDetailsResponse>> getFlightDetailsByBaggageOptions(
            @RequestParam boolean cabinBaggage,
            @RequestParam boolean checkedBaggage) throws FlightDetailsNotFoundException {
        log.info("Getting flight details by baggage options: cabinBaggage={}, checkedBaggage={}", cabinBaggage, checkedBaggage);
        List<FlightDetailsDTO> detailsList = flightDetailsService.getFlightDetailsByBaggageOptions(cabinBaggage, checkedBaggage);
        List<FlightDetailsResponse> responses = detailsList.stream()
                .map(flightDetailsMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Found {} flight details matching baggage options", responses.size());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("search/amenities")
    @Operation(summary = "Search by amenities", description = "Search flight details by wifi availability and meal service type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flight details found",
                    content = @Content(schema = @Schema(implementation = FlightDetailsResponse.class))),
            @ApiResponse(responseCode = "404", description = "Flight details not found")
    })
    public ResponseEntity<List<FlightDetailsResponse>> getFlightDetailsByWifiAndMeal(
            @RequestParam Boolean wifi,
            @RequestParam String mealService) throws FlightDetailsNotFoundException {
        log.info("Searching flight details by wifi={} and mealService={}", wifi, mealService);
        List<FlightDetailsDTO> detailsList = flightDetailsService.getFlightDetailsByWifiAndMeal(wifi, mealService);
        List<FlightDetailsResponse> responses = detailsList.stream()
                .map(flightDetailsMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Found {} flight details matching wifi and meal service criteria", responses.size());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search/price")
    @Operation(summary = "Search by extra baggage price range", description = "Search flight details by extra baggage price range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flight details found",
                    content = @Content(schema = @Schema(implementation = FlightDetailsResponse.class))),
            @ApiResponse(responseCode = "404", description = "Flight details not found")
    })
    public ResponseEntity<List<FlightDetailsResponse>> getFlightDetailsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) throws FlightDetailsNotFoundException {
        log.info("Searching flight details by price range: minPrice={}, maxPrice={}", minPrice, maxPrice);
        List<FlightDetailsDTO> detailsList = flightDetailsService.getFlightDetailsByPriceRange(minPrice, maxPrice);
        List<FlightDetailsResponse> responses = detailsList.stream()
                .map(flightDetailsMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Found {} flight details matching price range criteria", responses.size());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search/aircraft")
    @Operation(summary = "Search by aircraft model", description = "Search flight details by aircraft model")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flight details found",
                    content = @Content(schema = @Schema(implementation = FlightDetailsResponse.class))),
            @ApiResponse(responseCode = "404", description = "Flight details not found")
    })
    public ResponseEntity<List<FlightDetailsResponse>> getFlightDetailsByAircraftModel(
            @RequestParam String aircraftModel) throws FlightDetailsNotFoundException {
        log.info("Searching flight details by aircraft model: {}", aircraftModel);
        List<FlightDetailsDTO> detailsList = flightDetailsService.getFlightDetailsByAircraftModel(aircraftModel);
        List<FlightDetailsResponse> responses = detailsList.stream()
                .map(flightDetailsMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Found {} flight details matching aircraft model criteria", responses.size());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search/meal")
    @Operation(summary = "Search by meal service type", description = "Search flight details by meal service type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flight details found",
                    content = @Content(schema = @Schema(implementation = FlightDetailsResponse.class))),
            @ApiResponse(responseCode = "404", description = "Flight details not found")
    })
    public ResponseEntity<List<FlightDetailsResponse>> getFlightDetailsByMealService(
            @RequestParam String mealService) throws FlightDetailsNotFoundException {
        log.info("Searching flight details by meal service type: {}", mealService);
        List<FlightDetailsDTO> detailsList = flightDetailsService.getFlightDetailsByMealService(mealService);
        List<FlightDetailsResponse> responses = detailsList.stream()
                .map(flightDetailsMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Found {} flight details matching meal service type criteria", responses.size());
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    @Operation(summary = "Create flight details", description = "Create flight details for a specific flight")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Flight details successfully created",
                    content = @Content(schema = @Schema(implementation = FlightDetailsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "409", description = "Flight details already exist for this flight")
    })
    public ResponseEntity<FlightDetailsResponse> createFlightDetails(
            @Valid @RequestBody CreateFlightDetailsRequest request) throws FlightDetailsNotCreateException {
        log.info("Creating flight details for flight id: {}", request.getFlightId());
        FlightDetailsDTO flightDetailsDTO = flightDetailsMapper.toDTO(request);
        FlightDetailsDTO created = flightDetailsService.createFlightDetails(flightDetailsDTO);
        FlightDetailsResponse response = flightDetailsMapper.toResponse(created);
        log.info("Flight details created for flightId: {}", request.getFlightId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update flight details", description = "Update existing flight details by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flight details successfully updated",
                    content = @Content(schema = @Schema(implementation = FlightDetailsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Flight details not found")
    })
    public ResponseEntity<FlightDetailsResponse> updateFlightDetails(
            @PathVariable String id,
            @Valid @RequestBody CreateFlightDetailsRequest request) throws FlightDetailsNotFoundException {
        log.info("Updating flight details with id: {}", id);
        FlightDetailsDTO flightDetailsDTO = flightDetailsMapper.toDTO(request);
        FlightDetailsDTO updated = flightDetailsService.updateFlightDetails(id, flightDetailsDTO);
        FlightDetailsResponse response = flightDetailsMapper.toResponse(updated);
        log.info("Flight details updated for id: {}", id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete flight details", description = "Delete flight details by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Flight details successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Flight details not found")
    })
    public ResponseEntity<Void> deleteFlightDetails(@PathVariable String id) {
        log.info("Deleting flight details with id: {}", id);
        boolean deleted = flightDetailsService.deleteFlightDetails(id);
        if (deleted) {
            log.info("Flight details deleted for id: {}", id);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("Flight details not found for id: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/flight/{flightId}")
    @Operation(summary = "Delete flight details by flight ID", description = "Delete flight details by associated flight ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Flight details successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Flight details not found")
    })
    public ResponseEntity<Void> deleteFlightDetailsByFlightId(@PathVariable String flightId){
            log.info("Deleting flight details with flight id: {}", flightId);
            boolean deleted = flightDetailsService.deleteFlightDetailsByFlightId(flightId);
            if (deleted) {
                log.info("Flight details deleted for flight id: {}", flightId);
                return ResponseEntity.noContent().build();
            } else {
                log.warn("Flight details not found for flight id: {}", flightId);
                return ResponseEntity.notFound().build();
            }
        }

}
