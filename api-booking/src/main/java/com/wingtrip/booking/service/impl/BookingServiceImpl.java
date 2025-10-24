package com.wingtrip.booking.service.impl;

import com.wingtrip.booking.controller.request.UpdateBookingRequest;
import com.wingtrip.booking.dto.BookingDTO;
import com.wingtrip.booking.exception.*;
import com.wingtrip.booking.model.BookingEntity;
import com.wingtrip.booking.model.BookingStatus;
import com.wingtrip.booking.repository.BookingRepository;
import com.wingtrip.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;


    @Override
    public BookingDTO createBooking(BookingDTO bookingDTO) throws BookingNotCreateException {
        try {
            BookingEntity bookingEntity = BookingEntity.builder()
                    .bookingReference(generateBookingReference())
                    .bookingDate(LocalDateTime.now())
                    .travelDate(bookingDTO.getTravelDate())
                    .returnDate(bookingDTO.getReturnDate())
                    .adultPassengers(bookingDTO.getAdultPassengers())
                    .childPassengers(bookingDTO.getChildPassengers())
                    .infantPassengers(bookingDTO.getInfantPassengers())
                    .totalAmount(bookingDTO.getTotalAmount())
                    .currency(bookingDTO.getCurrency())
                    .bookingStatus(BookingStatus.PENDING)
                    .specialRequests(bookingDTO.getSpecialRequests())
                    .bookingNotes(bookingDTO.getBookingNotes())
                    .expiresAt(LocalDateTime.now().plusMinutes(15))
                    .userId(bookingDTO.getUserId())
                    .flightId(bookingDTO.getFlightId())
                    .build();
            BookingEntity saveBooking = bookingRepository.save(bookingEntity);
            return new BookingDTO(saveBooking);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BookingNotCreateException(MessageCode.BOOKING_NOT_CREATE);
        }
    }

    @Override
    public BookingDTO updateBooking(Long bookingId, UpdateBookingRequest request) throws BookingNotUpdateException, BookingAlreadyCancelledException {
        if (bookingId == null) {
            throw new BookingNotUpdateException(MessageCode.BOOKING_NOT_UPDATE);
        }

        Optional<BookingEntity> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            throw new BookingNotUpdateException(MessageCode.BOOKING_NOT_UPDATE);
        }

        BookingEntity bookingEntity = optionalBooking.get();

        // Validar estado antes de actualizar
        if (bookingEntity.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new BookingAlreadyCancelledException(MessageCode.BOOKING_ALREADY_CANCELLED);
        }

        // Actualizar campos
        if (request.getTravelDate() != null) {
            bookingEntity.setTravelDate(request.getTravelDate());
        }
        if (request.getReturnDate() != null) {
            bookingEntity.setReturnDate(request.getReturnDate());
        }

        bookingEntity.setAdultPassengers(request.getAdultPassengers());
        bookingEntity.setChildPassengers(request.getChildPassengers());
        bookingEntity.setInfantPassengers(request.getInfantPassengers());
        if (request.getSpecialRequests() != null) {
            bookingEntity.setSpecialRequests(request.getSpecialRequests());
        }
        if (request.getBookingNotes() != null) {
            bookingEntity.setBookingNotes(request.getBookingNotes());
        }
        BookingEntity updated = bookingRepository.save(bookingEntity);
        return new BookingDTO(updated);
    }

    @Override
    public BookingDTO findById(Long bookingId) throws BookingNotFoundByIdException {
        if (bookingId == null) {
            throw new BookingNotFoundByIdException(MessageCode.BOOKING_NOT_FOUND_BY_ID);
        }

        Optional<BookingEntity> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            throw new BookingNotFoundByIdException(MessageCode.BOOKING_NOT_FOUND_BY_ID);
        }

        BookingEntity bookingEntity = optionalBooking.get();
        return new BookingDTO(bookingEntity);
    }

    @Override
    public BookingDTO findByReference(String bookingReference) throws BookingNotFoundByReferenceException {
        if (bookingReference == null) {
            throw new BookingNotFoundByReferenceException(MessageCode.BOOKING_NOT_FOUND_BY_REFERENCE);
        }

        Optional<BookingEntity> optionalBooking = bookingRepository.findByBookingReference(bookingReference);
        if (optionalBooking.isEmpty()) {
            throw new BookingNotFoundByReferenceException(MessageCode.BOOKING_NOT_FOUND_BY_REFERENCE);
        }

        BookingEntity bookingEntity = optionalBooking.get();
        return new BookingDTO(bookingEntity);
    }

    @Override
    public List<BookingDTO> findBookingsByUserId(Long userId) throws UserBookingsNotFoundException {
        if (userId == null) {
            throw new UserBookingsNotFoundException(MessageCode.USER_BOOKINGS_NOT_FOUND);
        }

        List<BookingEntity> bookings = bookingRepository.findByUserId(userId);

        if (bookings == null) {
            throw new UserBookingsNotFoundException(MessageCode.USER_BOOKINGS_NOT_FOUND);
        }

        return bookings.stream()
                .map(BookingDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public void cancelBooking(Long bookingId) throws BookingCannotBeCancelledException, BookingAlreadyCancelledException {
        if (bookingId == null) {
            throw new BookingCannotBeCancelledException(MessageCode.BOOKING_CANNOT_BE_CANCELLED);
        }

        Optional<BookingEntity> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            throw new BookingCannotBeCancelledException(MessageCode.BOOKING_CANNOT_BE_CANCELLED);
        }

        BookingEntity entity = optionalBooking.get();

        //Valida si ya está cancelada
        if (entity.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new BookingAlreadyCancelledException(MessageCode.BOOKING_ALREADY_CANCELLED);
        }

        entity.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(entity);
    }

    @Override
    public boolean isBookingExpired(Long bookingId){
        if (bookingId == null) {
            return false;
        }

        Optional<BookingEntity> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            return false;
        }

        BookingEntity entity = optionalBooking.get();

        //Si no tiene fecha de expiración, no puede estar expirada
        if (entity.getExpiresAt() == null) {
            return false;
        }

        //Verificar si la fecha actual es despues de expiresAt
        return LocalDateTime.now().isAfter(entity.getExpiresAt());
    }

    private String generateBookingReference() {
        return "BK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
