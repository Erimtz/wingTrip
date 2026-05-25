package com.wingtrip.flight.details.exception;

public class FlightDetailsNotFoundByIdException extends RuntimeException {
    public FlightDetailsNotFoundByIdException(String message) {
        super(message);
    }
}
