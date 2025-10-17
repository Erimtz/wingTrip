package com.wingtrip.booking.exception;

public class BookingNotFoundByIdException extends Exception {

    public BookingNotFoundByIdException(MessageCode exp) {
        super(exp.getMsg());
    }
}
