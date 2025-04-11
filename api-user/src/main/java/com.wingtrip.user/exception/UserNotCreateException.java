package com.wingtrip.user.exception;

public class UserNotCreateException extends Exception {

    public UserNotCreateException (MessageCode exp) {
        super(exp.getMsg());
    }
}
