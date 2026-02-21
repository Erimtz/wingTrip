package com.wingtrip.exception;

public class PaymentAlreadyRefundedException extends Exception {

    public PaymentAlreadyRefundedException(MessageCode exp) {
        super(exp.getMsg());
    }
}
