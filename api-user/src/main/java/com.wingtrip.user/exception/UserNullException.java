package com.wingtrip.user.exception;

public class UserNullException extends Exception {

    public UserNullException(MessageCode exp) {
        super(exp.getMsg());
    }
}
