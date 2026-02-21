package com.wingtrip.exception;

public class PaymentFailedException extends Exception {

    public PaymentFailedException(MessageCode exp) {
        super(exp.getMsg());
    }
}
