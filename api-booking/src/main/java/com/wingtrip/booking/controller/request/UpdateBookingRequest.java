package com.wingtrip.booking.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBookingRequest {

    private LocalDate travelDate;
    private LocalDate returnDate;
    private int adultPassengers;
    private int childPassengers;
    private int infantPassengers;
    private String specialRequests;
    private String bookingNotes;

}
