package com.wingtrip.flight.exception;

public class FlightAlreadyCancelledException  extends RuntimeException {
    public FlightAlreadyCancelledException(String message) {
        super(message);
    }
}
