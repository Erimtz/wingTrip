package com.wingtrip.flight.exception;

public class MessageCode {
    public static final String FLIGHT_NOT_FOUND = "Flight not found";
    public static final String FLIGHT_NOT_FOUND_BY_ID = "Flight not found by ID: %s";
    public static final String FLIGHT_NOT_FOUND_BY_NUMBER = "Flight not found by number: %s";
    public static final String FLIGHT_NOT_CREATE = "Failed to create flight";
    public static final String FLIGHT_NOT_UPDATE = "Failed to update flight";
    public static final String FLIGHT_NOT_DELETE = "Failed to delete flight";
    public static final String FLIGHT_ALREADY_CANCELLED = "Flight has already been cancelled";
    public static final String INVALID_FLIGHT_DATES = "Arrival time must be after departure time";
}
