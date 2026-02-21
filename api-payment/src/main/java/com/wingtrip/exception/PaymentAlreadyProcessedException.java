package com.wingtrip.exception;

public class PaymentAlreadyProcessedException extends Exception {

    public PaymentAlreadyProcessedException(MessageCode exp) {
        super(exp.getMsg());
    }
}
