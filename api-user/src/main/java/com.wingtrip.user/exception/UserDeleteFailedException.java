package com.wingtrip.user.exception;

public class UserDeleteFailedException extends Exception {

    public UserDeleteFailedException(MessageCode exp) {
        super(exp.getMsg());
    }
}
