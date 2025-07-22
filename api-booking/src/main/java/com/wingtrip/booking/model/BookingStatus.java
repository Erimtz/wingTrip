package com.wingtrip.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
