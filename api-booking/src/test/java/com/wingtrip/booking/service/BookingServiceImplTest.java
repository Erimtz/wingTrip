package com.wingtrip.booking.service;

import com.wingtrip.booking.dto.BookingDTO;
import com.wingtrip.booking.exception.*;
import com.wingtrip.booking.model.BookingEntity;
import com.wingtrip.booking.model.BookingStatus;
import com.wingtrip.booking.repository.BookingRepository;
import com.wingtrip.booking.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookingServiceImpl Tests")
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private BookingDTO bookingDTO;
    private BookingEntity bookingEntity;

    @BeforeEach
    void setUp() {
        bookingDTO = BookingDTO.builder()
                .userId(1L)
                .flightId(101L)
                .travelDate(LocalDate.of(2025, 8, 15))
                .returnDate(LocalDate.of(2025, 8, 22))
                .adultPassengers(2)
                .childPassengers(0)
                .infantPassengers(0)
                .totalAmount(new BigDecimal("450.00"))
                .currency("USD")
                .specialRequests("Window seats preferred")
                .bookingNotes("Test booking")
                .build();

        bookingEntity = BookingEntity.builder()
                .bookingId(1L)
                .bookingReference("WT123456")
                .bookingDate(LocalDateTime.now())
                .travelDate(LocalDate.of(2025, 8, 15))
                .returnDate(LocalDate.of(2025, 8, 22))
                .adultPassengers(2)
                .childPassengers(0)
                .infantPassengers(0)
                .totalAmount(new BigDecimal("450.00"))
                .currency("USD")
                .bookingStatus(BookingStatus.PENDING)
                .specialRequests("Window seats preferred")
                .bookingNotes("Test booking")
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .userId(1L)
                .flightId(101L)
                .build();
    }

    @Test
    @DisplayName("Should create booking successfully")
    void testCreateBooking_Success() throws BookingNotCreateException {
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(bookingEntity);

        BookingDTO result = bookingService.createBooking(bookingDTO);

        assertNotNull(result);
        assertEquals(bookingEntity.getBookingId(), result.getBookingId());
        assertEquals(BookingStatus.PENDING, result.getBookingStatus());
        verify(bookingRepository, times(1)).save(any(BookingEntity.class));
    }

    @Test
    @DisplayName("Should throw exception when return date is before travel date")
    void testCreateBooking_InvalidReturnDate() {
        bookingDTO.setTravelDate(LocalDate.of(2025, 8, 22));
        bookingDTO.setReturnDate(LocalDate.of(2025, 8, 15));

        assertThrows(BookingNotCreateException.class, () -> {
            bookingService.createBooking(bookingDTO);
        });
    }

    @Test
    @DisplayName("Should find booking by ID successfully")
    void testFindById_Success() throws BookingNotFoundByIdException {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(bookingEntity));

        BookingDTO result = bookingService.findById(1L);

        assertNotNull(result);
        assertEquals(bookingEntity.getBookingId(), result.getBookingId());
        verify(bookingRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when booking not found by ID")
    void testFindById_NotFound() {

        when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundByIdException.class, () -> {
            bookingService.findById(999L);
        });
    }

    @Test
    @DisplayName("Should find booking by reference successfully")
    void testFindByReference_Success() throws BookingNotFoundByReferenceException {

        when(bookingRepository.findByBookingReference("WT123456")).thenReturn(Optional.of(bookingEntity));

        BookingDTO result = bookingService.findByReference("WT123456");

        assertNotNull(result);
        assertEquals(bookingEntity.getBookingReference(), result.getBookingReference());
        verify(bookingRepository, times(1)).findByBookingReference("WT123456");
    }

    @Test
    @DisplayName("Should throw exception when booking not found by reference")
    void testFindByReference_NotFound() {

        when(bookingRepository.findByBookingReference("INVALID")).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundByReferenceException.class, () -> {
            bookingService.findByReference("INVALID");
        });
    }

    @Test
    @DisplayName("Should confirm booking successfully")
    void testConfirmBooking_Success() throws BookingNotUpdateException, BookingNotFoundByIdException {

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(bookingEntity));
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(bookingEntity);

        bookingService.confirmBooking(1L, 1001L);

        verify(bookingRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).save(any(BookingEntity.class));
    }

    @Test
    @DisplayName("Should cancel booking successfully")
    void testCancelBooking_Success() throws BookingCannotBeCancelledException, BookingAlreadyCancelledException {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(bookingEntity));
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(bookingEntity);

        bookingService.cancelBooking(1L);

        verify(bookingRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).save(any(BookingEntity.class));
    }

    @Test
    @DisplayName("Should throw exception when cancelling already cancelled booking")
    void testCancelBooking_AlreadyCancelled() {
        bookingEntity.setBookingStatus(BookingStatus.CANCELLED);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(bookingEntity));

        assertThrows(BookingAlreadyCancelledException.class, () -> {
            bookingService.cancelBooking(1L);
        });
    }

    @Test
    @DisplayName("Should check if booking is expired correctly")
    void testIsBookingExpired_True() {
        bookingEntity.setExpiresAt(LocalDateTime.now().minusMinutes(5));
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(bookingEntity));

        boolean result = bookingService.isBookingExpired(1L);

        assertTrue(result);
    }

    @Test
    @DisplayName("Should check if booking is not expired correctly")
    void testIsBookingExpired_False() {
        bookingEntity.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(bookingEntity));

        boolean result = bookingService.isBookingExpired(1L);

        assertFalse(result);
    }

    @Test
    @DisplayName("Should find bookings by user ID successfully")
    void testFindBookingsByUserId_Success() throws UserBookingsNotFoundException {
        List<BookingEntity> bookings = List.of(bookingEntity);
        when(bookingRepository.findByUserId(1L)).thenReturn(bookings);

        List<BookingDTO> result = bookingService.findBookingsByUserId(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(bookingRepository, times(1)).findByUserId(1L);
    }

    @Test
    @DisplayName("Should throw exception when no bookings found for user")
    void testFindBookingsByUserId_NotFound() {
        when(bookingRepository.findByUserId(999L)).thenReturn(List.of());

        assertThrows(UserBookingsNotFoundException.class, () -> {
            bookingService.findBookingsByUserId(999L);
        });
    }

    @Test
    @DisplayName("Should find bookings by user ID and status successfully")
    void testFindBookingsByUserIdAndStatus_Success() throws UserBookingsNotFoundException {
        List<BookingEntity> bookings = List.of(bookingEntity);
        when(bookingRepository.findByUserIdAndBookingStatus(1L, BookingStatus.PENDING))
                .thenReturn(bookings);

        List<BookingDTO> result = bookingService.findBookingsByUserIdAndStatus(1L, BookingStatus.PENDING);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(bookingRepository, times(1)).findByUserIdAndBookingStatus(1L, BookingStatus.PENDING);
    }

    @Test
    @DisplayName("Should mark booking as paid successfully")
    void testMarkAsPaid_Success() throws BookingNotUpdateException, BookingNotFoundByIdException {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(bookingEntity));
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(bookingEntity);

        bookingService.markAsPaid(1L, 1001L);

        verify(bookingRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).save(any(BookingEntity.class));
    }

    @Test
    @DisplayName("Should find all expired bookings")
    void testFindAllExpiredBookings() {
        List<BookingEntity> expiredBookings = List.of(bookingEntity);
        when(bookingRepository.findByExpiresAtBefore(any(LocalDateTime.class)))
                .thenReturn(expiredBookings);

        List<BookingDTO> result = bookingService.findAllExpiredBookings();

        assertNotNull(result);
        verify(bookingRepository, times(1)).findByExpiresAtBefore(any(LocalDateTime.class));
    }
}

