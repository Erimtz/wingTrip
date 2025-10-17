package com.wingtrip.booking.exception;

public class BookingExpiredException extends Exception {

    public BookingExpiredException(MessageCode exp) {
        super(exp.getMsg());
    }
}
