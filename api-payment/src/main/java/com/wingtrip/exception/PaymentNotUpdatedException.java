package com.wingtrip.exception;

public class PaymentNotUpdatedException extends Exception {

    public PaymentNotUpdatedException(MessageCode exp) {
        super(exp.getMsg());
    }
}
