package com.wingtrip.booking.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateTimeUtil {

    public static LocalDate now() {
        return LocalDate.now(ZoneId.of("America/Bogota"));
    }

    public static LocalDateTime nowDateTime() {
        return LocalDateTime.now(ZoneId.of("America/Bogota"));
    }
}
