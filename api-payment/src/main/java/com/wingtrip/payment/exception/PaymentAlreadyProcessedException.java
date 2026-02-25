package com.wingtrip.payment.exception;

public class PaymentAlreadyProcessedException extends Exception {

    public PaymentAlreadyProcessedException(MessageCode exp) {
        super(exp.getMsg());
    }
}
