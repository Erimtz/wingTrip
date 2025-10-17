package com.wingtrip.booking.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageCode {

    BOOKING_NOT_FOUND("Booking not found."),
    BOOKING_NOT_FOUND_BY_ID("Booking not found by ID: %s"),
    BOOKING_NOT_FOUND_BY_REFERENCE("Booking not found with reference: %s"),
    USER_BOOKINGS_NOT_FOUND("No bookings found for user ID: %s"),
    BOOKING_NOT_CREATE("Failed to create booking"),
    BOOKING_NOT_UPDATE("Failed to update booking with ID: %s"),
    BOOKING_ALREADY_CANCELLED("Booking is already cancelled"),
    BOOKING_EXPIRED("Booking has expired"),
    BOOKING_CANNOT_BE_CANCELLED("Booking cannot be cancelled at this time"),
    INVALID_BOOKING_DATES("Travel date must be in the future"),
    INVALID_PASSENGER_COUNT("At least one passenger is required"),
    FLIGHT_NOT_AVAILABLE("Flight is not available for booking");

    private final String msg;
}
