package com.wingtrip.flight.exception;

public class FlightNotDeletedException extends RuntimeException {
    public FlightNotDeletedException(String message) {
        super(message);
    }
}
