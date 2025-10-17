package com.wingtrip.booking.exception;

public class BookingAlreadyCancelledException extends Exception {

    public BookingAlreadyCancelledException(MessageCode exp) {
        super(exp.getMsg());
    }
}
