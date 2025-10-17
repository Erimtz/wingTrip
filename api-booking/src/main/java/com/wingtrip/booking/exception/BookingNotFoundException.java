package com.wingtrip.booking.exception;

public class BookingNotFoundException extends Exception {

    public BookingNotFoundException(MessageCode exp) {
        super(exp.getMsg());
    }
}
