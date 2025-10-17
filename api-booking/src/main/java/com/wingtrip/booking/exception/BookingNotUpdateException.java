package com.wingtrip.booking.exception;

public class BookingNotUpdateException extends Exception {

    public BookingNotUpdateException(MessageCode exp) {
        super(exp.getMsg());
    }
}
