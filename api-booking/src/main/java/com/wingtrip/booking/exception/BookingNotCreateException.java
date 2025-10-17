package com.wingtrip.booking.exception;

public class BookingNotCreateException extends Exception {

    public BookingNotCreateException(MessageCode exp) {
        super(exp.getMsg());
    }
}
