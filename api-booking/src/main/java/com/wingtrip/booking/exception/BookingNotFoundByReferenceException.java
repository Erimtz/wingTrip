package com.wingtrip.booking.exception;

public class BookingNotFoundByReferenceException extends Exception {

    public BookingNotFoundByReferenceException(MessageCode exp) {
        super(exp.getMsg());
    }
}
