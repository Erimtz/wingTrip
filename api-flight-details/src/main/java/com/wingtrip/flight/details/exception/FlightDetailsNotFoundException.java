package com.wingtrip.flight.details.exception;

public class FlightDetailsNotFoundException extends  RuntimeException {
    public FlightDetailsNotFoundException(String message) {
        super(message);
    }
}
