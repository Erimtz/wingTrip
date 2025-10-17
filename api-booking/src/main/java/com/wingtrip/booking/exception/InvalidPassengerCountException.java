package com.wingtrip.booking.exception;

public class InvalidPassengerCountException extends Exception {

    public InvalidPassengerCountException(MessageCode exp) {
        super(exp.getMsg());
    }
}
