package com.wingtrip.payment.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageCode {

    PAYMENT_NOT_FOUND("Payment not found."),
    PAYMENT_NOT_FOUND_BY_ID("Payment not found by ID: %s"),
    PAYMENT_NOT_FOUND_BY_BOOKING("Payment not found for booking ID: %s"),
    PAYMENT_NOT_CREATE("Failed to create payment"),
    PAYMENT_NOT_UPDATE("Failed to update payment with ID: %s"),
    PAYMENT_ALREADY_PROCESSED("Payment has already been processed"),
    PAYMENT_ALREADY_REFUNDED("Payment has already been refunded"),
    PAYMENT_CANNOT_BE_REFUNDED("Payment cannot be refunded at this time"),
    PAYMENT_FAILED("Payment processing failed"),
    INVALID_PAYMENT_AMOUNT("Payment amount must be greater than zero"),
    INVALID_PAYMENT_TYPE("Invalid payment type provided");

    private final String msg;
}
