package com.wingtrip.payment.exception;

public class PaymentNotFoundException extends Exception {

    public PaymentNotFoundException(MessageCode exp) {
        super(exp.getMsg());
    }
}
