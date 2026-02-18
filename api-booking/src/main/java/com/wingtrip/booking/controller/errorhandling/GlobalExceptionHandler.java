package com.wingtrip.booking.controller.errorhandling;

import com.wingtrip.booking.exception.*;
import com.wingtrip.booking.util.DateTimeUtil;
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

import static com.wingtrip.booking.constant.Constant.*;


@ControllerAdvice
@RestController
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler({
            BookingNotFoundException.class,
            BookingNotFoundByIdException.class,
            BookingNotFoundByReferenceException.class,
            UserBookingsNotFoundException.class,
            BookingNotCreateException.class,
            BookingNotUpdateException.class,
            BookingAlreadyCancelledException.class,
            BookingExpiredException.class,
            BookingCannotBeCancelledException.class,
            InvalidBookingDatesException.class,
            InvalidPassengerCountException.class,
            FlightNotAvailableException.class
    })

    public ResponseEntity<Map<String, Object>> handleCustomException(HttpServletRequest req, Exception ex) {
        Map<String, Object> result = new HashMap<>();
        result.put(TIMESTAMP, System.currentTimeMillis());
        result.put(STATUS, HttpStatus.BAD_REQUEST.value());
        result.put(ERROR, ex.getMessage());
        result.put(PATH, new UrlPathHelper().getPathWithinApplication(req));

        log.error("Custom business exception ocurred: {} at path: {}", ex.getMessage(), result.get(PATH));

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(HttpServletRequest req, Exception ex) {

        Map<String, Object> result = new HashMap<>();
        result.put(TIMESTAMP, System.currentTimeMillis());
        result.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        result.put(ERROR, ex.getMessage());
        result.put(PATH, new UrlPathHelper().getPathWithinApplication(req));

        log.error("Unexpected error occurred: {} at path: {}", ex.getMessage(), result.get(PATH), ex);

        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
