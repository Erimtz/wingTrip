package com.wingtrip.booking.controller;

import com.wingtrip.booking.controller.doc.BookingControllerDoc;
import com.wingtrip.booking.controller.mapper.BookingMapper;
import com.wingtrip.booking.controller.request.CreateBookingRequest;
import com.wingtrip.booking.controller.request.UpdateBookingRequest;
import com.wingtrip.booking.controller.response.BookingResponse;
import com.wingtrip.booking.dto.BookingDTO;
import com.wingtrip.booking.exception.*;
import com.wingtrip.booking.model.BookingStatus;
import com.wingtrip.booking.service.impl.BookingServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/booking")
public class BookingController implements BookingControllerDoc {

    private final BookingMapper bookingMapper;
    private final BookingServiceImpl bookingService;

    @PostMapping("/create")
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody CreateBookingRequest createRequest) throws BookingNotCreateException {
        log.info("Create new booking request received: {}", createRequest);
        BookingDTO bookingDTO = bookingMapper.toDTO(createRequest);
        BookingDTO createdBooking = bookingService.createBooking(bookingDTO);
        BookingResponse bookingResponse = bookingMapper.toResponse(createdBooking);
        log.info("Booking created successfully: {}", bookingResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingResponse);
    }

    @Override
    @PutMapping("/update/{bookingId}")
    public ResponseEntity<BookingResponse> updateBooking(@PathVariable Long bookingId, @Valid @RequestBody UpdateBookingRequest updateRequest) throws BookingNotUpdateException, BookingAlreadyCancelledException {
        log.info("Updating booking with ID {}:", bookingId);
        BookingDTO updatedBooking = bookingService.updateBooking(bookingId, updateRequest);
        BookingResponse bookingResponse = bookingMapper.toResponse(updatedBooking);
        log.info("Booking updated successfully with ID: {}", bookingId);
        return ResponseEntity.ok(bookingResponse);
    }

    @Override
    @GetMapping("/find/{bookingId}")
    public ResponseEntity<BookingResponse> findById(@PathVariable Long bookingId) throws BookingNotFoundByIdException {
        log.info("Finding booking with ID {}:", bookingId);
        BookingDTO bookingDTO = bookingService.findById(bookingId);
        BookingResponse bookingResponse = bookingMapper.toResponse(bookingDTO);
        log.info("Booking found successfully with ID: {}", bookingId);
        return ResponseEntity.ok(bookingResponse);
    }

    @Override
    @GetMapping("/find-by-reference/{bookingReference}")
    public ResponseEntity<BookingResponse> findByReference(@PathVariable String bookingReference) throws BookingNotFoundByReferenceException {
        log.info("Finding booking with reference {}:", bookingReference);
        BookingDTO bookingDTO = bookingService.findByReference(bookingReference);
        BookingResponse bookingResponse = bookingMapper.toResponse(bookingDTO);
        log.info("Booking found successfully with reference: {}", bookingReference);
        return ResponseEntity.ok(bookingResponse);
    }

    @Override
    @PutMapping("/confirm/{bookingId}")
    public ResponseEntity<BookingResponse> confirmBooking(@PathVariable Long bookingId, @RequestParam Long paymentId) throws BookingNotUpdateException, BookingNotFoundByIdException {
        log.info("Confirming booking with ID: {} and payment ID: {}", bookingId, paymentId);
        bookingService.confirmBooking(bookingId, paymentId);
        BookingDTO bookingDTO = bookingService.findById(bookingId);
        BookingResponse bookingResponse = bookingMapper.toResponse(bookingDTO);
        log.info("Booking confirmed successfully with ID: {}", bookingId);
        return ResponseEntity.ok(bookingResponse);
    }

    @Override
    @PutMapping("/cancel/{bookingId}")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable Long bookingId) throws BookingCannotBeCancelledException, BookingAlreadyCancelledException, BookingNotFoundByIdException {
        log.info("Cancelling booking with ID: {}", bookingId);
        bookingService.cancelBooking(bookingId);
        BookingDTO bookingDTO = bookingService.findById(bookingId);
        BookingResponse bookingResponse = bookingMapper.toResponse(bookingDTO);
        log.info("Booking cancelled successfully with ID: {}", bookingId);
        return ResponseEntity.ok(bookingResponse);
    }

    @Override
    @GetMapping("/is-expired/{bookingId}")
    public ResponseEntity<BookingResponse> isBookingExpired(@PathVariable Long bookingId) {
        log.info("Checking if booking with ID: {} is expired", bookingId);
        boolean isExpired = bookingService.isBookingExpired(bookingId);
        try {
            BookingDTO bookingDTO = bookingService.findById(bookingId);
            BookingResponse bookingResponse = bookingMapper.toResponse(bookingDTO);
            log.info("Booking expiration status: {}", isExpired);
            return ResponseEntity.ok(bookingResponse);
        } catch (BookingNotFoundByIdException ex) {
            log.error("Booking not found with ID: {}", bookingId);
            throw new RuntimeException(ex);
        }
    }

    @Override
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingResponse>> findBookingsByUserId(@PathVariable Long userId) throws UserBookingsNotFoundException {
        log.info("Finding bookings for user ID: {}", userId);
        List<BookingDTO> bookings = bookingService.findBookingsByUserId(userId);
        List<BookingResponse> responses = bookings.stream()
                .map(bookingMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Found {} bookings for user ID: {}", responses.size(), userId);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/user/{userId}/status")
    public ResponseEntity<List<BookingResponse>> findBookingsByUserIdAndStatus(@PathVariable Long userId, @RequestParam BookingStatus status) throws UserBookingsNotFoundException {
        log.info("Finding bookings for user ID: {} with status: {}", userId, status);
        List<BookingDTO> bookings = bookingService.findBookingsByUserIdAndStatus(userId, status);
        List<BookingResponse> responses = bookings.stream()
                .map(bookingMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Found {} bookings for user ID: {} with status: {}", responses.size(), userId, status);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/user/{userId}/cancelled")
    public ResponseEntity<List<BookingResponse>> findCancelledBookingsByUserId(@PathVariable Long userId) throws UserBookingsNotFoundException {
        log.info("Finding cancelled bookings for user ID: {}", userId);
        List<BookingDTO> bookings = bookingService.findCancelledBookingsByUserId(userId);
        List<BookingResponse> responses = bookings.stream()
                .map(bookingMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Found {} cancelled bookings for user ID: {}", responses.size(), userId);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/user/{userId}/pending")
    public ResponseEntity<List<BookingResponse>> findPendingBookingsByUserId(@PathVariable Long userId) throws UserBookingsNotFoundException {
        log.info("Finding pending bookings for user ID: {}", userId);
        List<BookingDTO> bookings = bookingService.findPendingBookingsByUserId(userId);
        List<BookingResponse> responses = bookings.stream()
                .map(bookingMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Found {} pending bookings for user ID: {}", responses.size(), userId);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/user/{userId}/expired")
    public ResponseEntity<List<BookingResponse>> findExpiredBookingsByUserId(@PathVariable Long userId) throws UserBookingsNotFoundException {
        log.info("Finding expired bookings for user ID: {}", userId);
        List<BookingDTO> bookings = bookingService.findExpiredBookingsByUserId(userId);
        List<BookingResponse> responses = bookings.stream()
                .map(bookingMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Found {} expired bookings for user ID: {}", responses.size(), userId);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/expired")
    public ResponseEntity<List<BookingResponse>> findAllExpiredBookings() {
        log.info("Finding all expired bookings");
        List<BookingDTO> bookings = bookingService.findAllExpiredBookings();
        List<BookingResponse> responsesList = bookings.stream()
                .map(bookingMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Found {} expired bookings in total", responsesList.size());
        return ResponseEntity.ok(responsesList);
    }

    @Override
    @PutMapping("/mark-expired/{bookingId}")
    public ResponseEntity<BookingResponse> markAsExpired(@PathVariable Long bookingId) throws BookingExpiredException, BookingNotFoundByIdException {
        log.info("Marking booking with ID: {} as expired", bookingId);
        bookingService.markAsExpired(bookingId);
        BookingDTO bookingDTO = bookingService.findById(bookingId);
        BookingResponse bookingResponse = bookingMapper.toResponse(bookingDTO);
        log.info("Booking marked as expired successfully with ID: {}", bookingId);
        return ResponseEntity.ok(bookingResponse);
    }

    @Override
    @PutMapping("/mark-paid/{bookingId}")
    public ResponseEntity<BookingResponse> markAsPaid(@PathVariable Long bookingId, @RequestParam Long paymentId) throws BookingNotUpdateException, BookingNotFoundByIdException {
        log.info("Marking booking with ID: {} as paid with payment ID: {}", bookingId, paymentId);
        bookingService.markAsPaid(bookingId, paymentId);
        BookingDTO bookingDTO = bookingService.findById(bookingId);
        BookingResponse bookingResponse = bookingMapper.toResponse(bookingDTO);
        log.info("Booking marked as paid successfully with ID: {}", bookingId);
        return ResponseEntity.ok(bookingResponse);
    }
}
