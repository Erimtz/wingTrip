package com.wingtrip.booking.service;

import com.wingtrip.booking.controller.request.UpdateBookingRequest;
import com.wingtrip.booking.dto.BookingDTO;
import com.wingtrip.booking.exception.*;
import com.wingtrip.booking.model.BookingStatus;

import java.util.List;

public interface BookingService {

    // ==================== CRUD Operations ====================
    BookingDTO createBooking(BookingDTO bookingDTO) throws BookingNotCreateException;
    BookingDTO updateBooking(Long bookingId, UpdateBookingRequest request) throws BookingNotUpdateException, BookingAlreadyCancelledException;
    BookingDTO findById(Long bookingId) throws BookingNotFoundByIdException;
    BookingDTO findByReference(String bookingReference) throws BookingNotFoundByReferenceException;
    void confirmBooking(Long bookingId, Long paymentId) throws BookingNotUpdateException;
    void cancelBooking(Long bookingId) throws BookingCannotBeCancelledException, BookingAlreadyCancelledException;
    boolean isBookingExpired(Long bookingId);

    // ==================== User Filters ====================
    List<BookingDTO> findBookingsByUserId(Long userId) throws UserBookingsNotFoundException;
    List<BookingDTO> findBookingsByUserIdAndStatus(Long userId, BookingStatus status) throws UserBookingsNotFoundException;
    List<BookingDTO> findCancelledBookingsByUserId(Long userId) throws UserBookingsNotFoundException;
    List<BookingDTO> findPendingBookingsByUserId(Long userId) throws UserBookingsNotFoundException;
    List<BookingDTO> findExpiredBookingsByUserId(Long userId) throws UserBookingsNotFoundException;

    // ==================== Admin Operations ====================
    List<BookingDTO> findAllExpiredBookings();

    // ==================== Future Use Cases ====================

    /**
     * Marca la reserva como expirada si su fecha de expiración ya pasó.
     * (A implementar en un próximo feature: gestión automática de expiración)
     */
    void markAsExpired(Long bookingId) throws BookingExpiredException;

    /**
     * Marca la reserva como pagada y confirmada, tras recibir confirmación de pago.
     * (Se implementará cuando esté integrado api-payment y RabbitMQ)
     */
    void markAsPaid(Long bookingId, Long paymentId) throws BookingNotUpdateException;
}
