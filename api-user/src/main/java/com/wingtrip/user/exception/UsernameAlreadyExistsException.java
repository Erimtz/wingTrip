package com.wingtrip.user.exception;

public class UsernameAlreadyExistsException extends Exception {

    public UsernameAlreadyExistsException(MessageCode exp) {
        super(exp.getMsg());
    }
}
