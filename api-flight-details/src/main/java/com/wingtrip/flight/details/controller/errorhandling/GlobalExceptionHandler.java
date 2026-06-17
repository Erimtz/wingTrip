package com.wingtrip.flight.details.controller.errorhandling;

import com.wingtrip.flight.details.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UrlPathHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.wingtrip.flight.details.constant.Constant.*;

@ControllerAdvice
@RestController
@Log4j2
public class GlobalExceptionHandler {

    /**
     * Maneja FlightDetailsNotFoundException → 404
     */
    @ExceptionHandler(FlightDetailsNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleFlightDetailsNotFoundException(
            HttpServletRequest req, FlightDetailsNotFoundException ex) {
        Map<String, Object> result = new HashMap<>();
        result.put(TIMESTAMP, System.currentTimeMillis());
        result.put(STATUS, HttpStatus.NOT_FOUND.value());
        result.put(ERROR, ex.getMessage());
        result.put(PATH, new UrlPathHelper().getPathWithinApplication(req));
        log.error("Flight details not found exception: {}", ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja FlightDetailsNotFoundByIdException → 404
     */
    @ExceptionHandler(FlightDetailsNotFoundByIdException.class)
    public ResponseEntity<Map<String, Object>> handleFlightDetailsNotFoundByIdException(
            HttpServletRequest req, FlightDetailsNotFoundByIdException ex) {
        Map<String, Object> result = new HashMap<>();
        result.put(TIMESTAMP, System.currentTimeMillis());
        result.put(STATUS, HttpStatus.NOT_FOUND.value());
        result.put(ERROR, ex.getMessage());
        result.put(PATH, new UrlPathHelper().getPathWithinApplication(req));
        log.error("Flight details not found by id exception: {}", ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja FlightDetailsNotFoundByFlightIdException → 404
     */
    @ExceptionHandler(FlightDetailsNotFoundByFlightIdException.class)
    public ResponseEntity<Map<String, Object>> handleFlightDetailsNotFoundByFlightIdException(
            HttpServletRequest req, FlightDetailsNotFoundByFlightIdException ex) {
        Map<String, Object> result = new HashMap<>();
        result.put(TIMESTAMP, System.currentTimeMillis());
        result.put(STATUS, HttpStatus.NOT_FOUND.value());
        result.put(ERROR, ex.getMessage());
        result.put(PATH, new UrlPathHelper().getPathWithinApplication(req));
        log.error("Flight details not found by flightId exception: {}", ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja FlightDetailsAlreadyExistsException → 409
     */
    @ExceptionHandler(FlightDetailsAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleFlightDetailsAlreadyExistsException(
            HttpServletRequest req, FlightDetailsAlreadyExistsException ex) {
        Map<String, Object> result = new HashMap<>();
        result.put(TIMESTAMP, System.currentTimeMillis());
        result.put(STATUS, HttpStatus.CONFLICT.value());
        result.put(ERROR, ex.getMessage());
        result.put(PATH, new UrlPathHelper().getPathWithinApplication(req));
        log.error("Flight details already exists exception: {}", ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.CONFLICT);
    }

    /**
     * Maneja el resto de excepciones de flight details → 400
     */
    @ExceptionHandler({
            FlightDetailsNotCreateException.class,
            FlightDetailsNotUpdateException.class,
            FlightDetailsNotDeleteException.class
    })
    public ResponseEntity<Map<String, Object>> handleFlightDetailsException(
            HttpServletRequest req, Exception ex) {
        Map<String, Object> result = new HashMap<>();
        result.put(TIMESTAMP, System.currentTimeMillis());
        result.put(STATUS, HttpStatus.BAD_REQUEST.value());
        result.put(ERROR, ex.getMessage());
        result.put(PATH, new UrlPathHelper().getPathWithinApplication(req));
        log.error("Flight details exception occurred: {}", ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja errores de validación de @Valid en @RequestBody → 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(HttpServletRequest req,
                                                                         MethodArgumentNotValidException ex) {
        Map<String, Object> result = new HashMap<>();
        result.put(TIMESTAMP, System.currentTimeMillis());
        result.put(STATUS, HttpStatus.BAD_REQUEST.value());

        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));

        result.put(ERROR, errors);
        result.put(PATH, new UrlPathHelper().getPathWithinApplication(req));
        log.error("Validation failed for request {}: {}", req.getRequestURI(), errors);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones genéricas → 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            HttpServletRequest req, Exception ex) {
        Map<String, Object> result = new HashMap<>();
        result.put(TIMESTAMP, System.currentTimeMillis());
        result.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        result.put(ERROR, ex.getMessage());
        result.put(PATH, new UrlPathHelper().getPathWithinApplication(req));
        log.error("Generic exception occurred: {}", ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
