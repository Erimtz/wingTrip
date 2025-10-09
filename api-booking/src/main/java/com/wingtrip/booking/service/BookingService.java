package com.wingtrip.booking.service;

import com.wingtrip.booking.controller.request.CreateBookingRequest;
import com.wingtrip.booking.controller.request.UpdateBookingRequest;
import com.wingtrip.booking.dto.BookingDTO;

import java.util.List;

public interface BookingService {

    BookingDTO createBooking(CreateBookingRequest request);
    BookingDTO updateBooking(Long bookingId, UpdateBookingRequest request);
    BookingDTO findById(Long bookingId);
    BookingDTO findByReference(String bookingReference);
    List<BookingDTO> findBookingsByUserId(Long userId);
    void cancelBooking(Long bookingId);
    boolean isBookingExpired(Long bookingId);

}
