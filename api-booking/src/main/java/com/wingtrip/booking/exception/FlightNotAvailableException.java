package com.wingtrip.booking.exception;

public class FlightNotAvailableException extends Exception {

    public FlightNotAvailableException(MessageCode exp) {
        super(exp.getMsg());
    }
}
