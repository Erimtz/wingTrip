package com.wingtrip.booking.exception;

public class InvalidReturnDateException extends Exception {

    public InvalidReturnDateException(MessageCode exp) {
        super(exp.getMsg());
    }
}
