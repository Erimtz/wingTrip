package com.wingtrip.payment.exception;

public class PaymentCannotBeRefundedException extends Exception {

    public PaymentCannotBeRefundedException(MessageCode exp) {
        super(exp.getMsg());
    }
}
