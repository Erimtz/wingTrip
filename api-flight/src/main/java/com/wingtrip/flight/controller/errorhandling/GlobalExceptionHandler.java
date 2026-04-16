package com.wingtrip.flight.controller.errorhandling;

import com.wingtrip.flight.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UrlPathHelper;

import java.util.HashMap;
import java.util.Map;

import static com.wingtrip.flight.constant.Constant.*;

@ControllerAdvice
@RestController
@Log4j2
public class GlobalExceptionHandler {


    @ExceptionHandler({
            FlightNotFoundException.class,
            FlightNotCreatedException.class,
            FlightNotUpdatedException.class,
            FlightNotDeletedException.class,
            FlightAlreadyCancelledException.class
    })
    public ResponseEntity<Map<String, Object>> handleFlightException(HttpServletRequest req, Exception ex) {
        Map<String, Object> result = new HashMap<>();
        result.put(TIMESTAMP, System.currentTimeMillis());
        result.put(STATUS, HttpStatus.BAD_REQUEST.value());
        result.put(ERROR, ex.getMessage());
        result.put(PATH, new UrlPathHelper().getPathWithinApplication(req));
        log.error("Flight exception occurred: {}", ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones genéricas
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(HttpServletRequest req, Exception ex) {

        // Si es una excepción de Flight, no procesarla aquí
        if (ex instanceof FlightNotFoundException ||
                ex instanceof FlightNotCreatedException ||
                ex instanceof FlightNotUpdatedException ||
                ex instanceof FlightNotDeletedException ||
                ex instanceof FlightAlreadyCancelledException) {
            return null;
        }

        Map<String, Object> result = new HashMap<>();
        result.put(TIMESTAMP, System.currentTimeMillis());
        result.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        result.put(ERROR, ex.getMessage());
        result.put(PATH, new UrlPathHelper().getPathWithinApplication(req));
        log.error("Generic exception occurred: {}", ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


