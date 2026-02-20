package com.wingtrip.booking.controller.mapper;

import com.wingtrip.booking.controller.request.CreateBookingRequest;
import com.wingtrip.booking.controller.request.UpdateBookingRequest;
import com.wingtrip.booking.controller.response.BookingResponse;
import com.wingtrip.booking.dto.BookingDTO;
import com.wingtrip.booking.model.BookingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BookingMapper Tests")
class BookingMapperTest {

    private BookingMapper bookingMapper;

    private CreateBookingRequest createRequest;
    private BookingDTO bookingDTO;
    private BookingResponse bookingResponse;

    @BeforeEach
    void setUp() {
        bookingMapper = new BookingMapperImpl();

        createRequest = CreateBookingRequest.builder()
                .userId(1L)
                .flightId(101L)
                .travelDate(LocalDate.of(2025, 8, 15))
                .returnDate(LocalDate.of(2025, 8, 22))
                .adultPassengers(2)
                .childPassengers(0)
                .infantPassengers(0)
                .totalAmount(450.00)
                .currency("USD")
                .specialRequests("Window seats preferred")
                .build();

        bookingDTO = BookingDTO.builder()
                .bookingId(1L)
                .bookingReference("WT123456")
                .userId(1L)
                .flightId(101L)
                .travelDate(LocalDate.of(2025, 8, 15))
                .returnDate(LocalDate.of(2025, 8, 22))
                .adultPassengers(2)
                .childPassengers(0)
                .infantPassengers(0)
                .totalAmount(450.00)
                .currency("USD")
                .bookingStatus(BookingStatus.PENDING)
                .specialRequests("Window seats preferred")
                .build();

        bookingResponse = BookingResponse.builder()
                .bookingId(1L)
                .bookingReference("WT123456")
                .userId(1L)
                .flightId(101L)
                .travelDate(LocalDate.of(2025, 8, 15))
                .returnDate(LocalDate.of(2025, 8, 22))
                .adultPassengers(2)
                .childPassengers(0)
                .infantPassengers(0)
                .totalAmount(450.00)
                .currency("USD")
                .bookingStatus(BookingStatus.PENDING)
                .specialRequests("Window seats preferred")
                .build();
    }

    @Test
    @DisplayName("Should map CreateBookingRequest to BookingDTO successfully")
    void testToDTO_Success() {
        BookingDTO result = bookingMapper.toDTO(createRequest);

        assertNotNull(result);
        assertEquals(createRequest.getUserId(), result.getUserId());
        assertEquals(createRequest.getFlightId(), result.getFlightId());
        assertEquals(createRequest.getTravelDate(), result.getTravelDate());
        assertEquals(createRequest.getReturnDate(), result.getReturnDate());
        assertEquals(createRequest.getAdultPassengers(), result.getAdultPassengers());
        assertEquals(createRequest.getCurrency(), result.getCurrency());
    }

    @Test
    @DisplayName("Should map CreateBookingRequest to BookingDTO with null optional fields")
    void testToDTO_WithNullFields() {
        CreateBookingRequest requestWithNulls = CreateBookingRequest.builder()
                .userId(1L)
                .flightId(101L)
                .travelDate(LocalDate.of(2025, 8, 15))
                .adultPassengers(2)
                .build();

        BookingDTO result = bookingMapper.toDTO(requestWithNulls);

        assertNotNull(result);
        assertEquals(requestWithNulls.getUserId(), result.getUserId());
        assertNull(result.getReturnDate());
        assertNull(result.getSpecialRequests());
    }

    @Test
    @DisplayName("Should map BookingDTO to BookingResponse successfully")
    void testToResponse_Success() {
        BookingResponse result = bookingMapper.toResponse(bookingDTO);

        assertNotNull(result);
        assertEquals(bookingDTO.getBookingId(), result.getBookingId());
        assertEquals(bookingDTO.getBookingReference(), result.getBookingReference());
        assertEquals(bookingDTO.getUserId(), result.getUserId());
        assertEquals(bookingDTO.getFlightId(), result.getFlightId());
        assertEquals(bookingDTO.getBookingStatus(), result.getBookingStatus());
    }

    @Test
    @DisplayName("Should map BookingDTO to BookingResponse preserving all fields")
    void testToResponse_AllFields() {
        BookingResponse result = bookingMapper.toResponse(bookingDTO);

        assertNotNull(result);
        assertEquals(bookingDTO.getBookingId(), result.getBookingId());
        assertEquals(bookingDTO.getBookingReference(), result.getBookingReference());
        assertEquals(bookingDTO.getTravelDate(), result.getTravelDate());
        assertEquals(bookingDTO.getReturnDate(), result.getReturnDate());
        assertEquals(bookingDTO.getAdultPassengers(), result.getAdultPassengers());
        assertEquals(bookingDTO.getChildPassengers(), result.getChildPassengers());
        assertEquals(bookingDTO.getInfantPassengers(), result.getInfantPassengers());
        assertEquals(bookingDTO.getTotalAmount(), result.getTotalAmount());
        assertEquals(bookingDTO.getCurrency(), result.getCurrency());
        assertEquals(bookingDTO.getSpecialRequests(), result.getSpecialRequests());
    }

    @Test
    @DisplayName("Should handle null DTO gracefully")
    void testToResponse_NullDTO() {
        BookingResponse result = bookingMapper.toResponse(null);

        assertNull(result, "MapStruct should return null when input is null instead of throwing exception");
    }

    @Test
    @DisplayName("Should handle null request gracefully")
    void testToDTO_NullRequest() {
        BookingDTO result = bookingMapper.toDTO((CreateBookingRequest) null);

        assertNull(result, "MapStruct should return null when input is null instead of throwing exception");
    }

    @Test
    @DisplayName("Should map UpdateBookingRequest to BookingDTO successfully")
    void testToDTO_Update_Success() {
        UpdateBookingRequest updateRequest = UpdateBookingRequest.builder()
                .travelDate(LocalDate.of(2025, 12, 1))
                .returnDate(LocalDate.of(2025, 12, 15))
                .adultPassengers(3)
                .childPassengers(1)
                .infantPassengers(0)
                .specialRequests("Extra luggage")
                .bookingNotes("Urgent update")
                .build();

        BookingDTO result = bookingMapper.toDTO(updateRequest);

        assertNotNull(result);
        assertEquals(updateRequest.getTravelDate(), result.getTravelDate());
        assertEquals(updateRequest.getReturnDate(), result.getReturnDate());
        assertEquals(updateRequest.getAdultPassengers(), result.getAdultPassengers());
        assertEquals(updateRequest.getChildPassengers(), result.getChildPassengers());
        assertEquals(updateRequest.getInfantPassengers(), result.getInfantPassengers());
        assertEquals(updateRequest.getSpecialRequests(), result.getSpecialRequests());

        assertEquals(updateRequest.getBookingNotes(), result.getBookingNotes());
    }
}

