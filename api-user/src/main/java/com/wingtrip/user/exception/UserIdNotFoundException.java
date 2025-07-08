package com.wingtrip.user.exception;

public class UserIdNotFoundException extends Exception {

    public UserIdNotFoundException (MessageCode exp) {
        super(exp.getMsg());
    }
}
