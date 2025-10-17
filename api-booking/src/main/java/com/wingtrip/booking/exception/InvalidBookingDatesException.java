package com.wingtrip.booking.exception;

public class InvalidBookingDatesException extends Exception {

    public InvalidBookingDatesException(MessageCode exp) {
        super(exp.getMsg());
    }
}
