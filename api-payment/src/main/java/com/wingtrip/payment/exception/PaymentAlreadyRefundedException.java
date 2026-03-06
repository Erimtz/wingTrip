package com.wingtrip.payment.exception;

public class PaymentAlreadyRefundedException extends Exception {

    public PaymentAlreadyRefundedException(MessageCode exp) {
        super(exp.getMsg());
    }
}
