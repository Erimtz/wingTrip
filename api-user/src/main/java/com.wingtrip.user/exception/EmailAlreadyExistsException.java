package com.wingtrip.user.exception;

public class EmailAlreadyExistsException extends Exception {

    public EmailAlreadyExistsException(MessageCode exp) {
        super(exp.getMsg());
    }
}
