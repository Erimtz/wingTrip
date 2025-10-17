package com.wingtrip.booking.exception;

public class BookingCannotBeCancelledException extends Exception {

    public BookingCannotBeCancelledException(MessageCode exp) {
        super(exp.getMsg());
    }
}
