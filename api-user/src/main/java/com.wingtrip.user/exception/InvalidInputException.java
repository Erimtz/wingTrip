package com.wingtrip.user.exception;

public class InvalidInputException extends Exception {

    public InvalidInputException(MessageCode exp) {
        super(exp.getMsg());
    }
}
