package com.wingtrip.user.exception;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(MessageCode exp) {
        super(exp.getMsg());
    }
}
