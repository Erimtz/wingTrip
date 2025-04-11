package com.wingtrip.user.exception;

public class UserNotExistException extends Exception {

    public UserNotExistException (MessageCode exp) {
        super(exp.getMsg());
    }
}
