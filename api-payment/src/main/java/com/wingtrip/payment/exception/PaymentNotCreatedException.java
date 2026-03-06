package com.wingtrip.payment.exception;

public class PaymentNotCreatedException extends Exception {

    public PaymentNotCreatedException(MessageCode exp) {
        super(exp.getMsg());
    }
}
