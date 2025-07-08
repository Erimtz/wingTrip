package com.wingtrip.user.exception;

public class UserTotalAmountException extends Exception {

    public UserTotalAmountException(MessageCode exp) {
        super(exp.getMsg());
    }
}
