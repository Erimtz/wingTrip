package com.wingtrip.booking.exception;

public class UserBookingsNotFoundException extends Exception {

    public UserBookingsNotFoundException(MessageCode exp) {
        super(exp.getMsg());
    }
}
