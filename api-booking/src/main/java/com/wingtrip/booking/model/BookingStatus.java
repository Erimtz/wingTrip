package com.wingtrip.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BookingStatus {
    PENDING("Pendiente"),
    CONFIRMED("Confirmada"),
    PAID("Pagada"),
    CANCELLED("Cancelada"),
    EXPIRED("Expirada");

    private final String description;
}
