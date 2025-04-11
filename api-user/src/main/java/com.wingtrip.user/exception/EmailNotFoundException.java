package com.wingtrip.user.exception;

public class EmailNotFoundException extends Exception {

    public EmailNotFoundException (MessageCode exp) {
        super(exp.getMsg());
    }
}
