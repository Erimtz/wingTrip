package com.wingtrip.booking.service;

import com.wingtrip.booking.controller.request.UpdateBookingRequest;
import com.wingtrip.booking.dto.BookingDTO;
import com.wingtrip.booking.exception.*;

import java.util.List;

public interface BookingService {

    BookingDTO createBooking(BookingDTO bookingDTO) throws BookingNotCreateException;
    BookingDTO updateBooking(Long bookingId, UpdateBookingRequest request) throws BookingNotUpdateException, BookingAlreadyCancelledException;
    BookingDTO findById(Long bookingId) throws BookingNotFoundByIdException;
    BookingDTO findByReference(String bookingReference) throws BookingNotFoundByReferenceException;
    List<BookingDTO> findBookingsByUserId(Long userId) throws UserBookingsNotFoundException;
    void cancelBooking(Long bookingId) throws BookingCannotBeCancelledException, BookingAlreadyCancelledException;
    boolean isBookingExpired(Long bookingId);

}
