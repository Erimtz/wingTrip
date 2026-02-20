package com.wingtrip.booking.controller.doc;

import com.wingtrip.booking.controller.request.CreateBookingRequest;
import com.wingtrip.booking.controller.request.UpdateBookingRequest;
import com.wingtrip.booking.controller.response.BookingResponse;
import com.wingtrip.booking.exception.*;
import com.wingtrip.booking.model.BookingStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@Tag(name = "Booking Controller", description = "Endpoints for managing bookings")
public interface BookingControllerDoc {


    @Operation(summary = "Created new booking",
            description = "Creates a new booking in the system.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Booking created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookingResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error creating booking",
                    content = @Content
            )
    })
    ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody CreateBookingRequest bookingRequest) throws BookingNotCreateException;

    @Operation(summary = "Update booking by user",
            description = "Updates an existing booking's information using their user.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookingResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Booking not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid update data",
                    content = @Content
            )
    })
    ResponseEntity<BookingResponse> updateBooking(@PathVariable Long bookingId, @RequestBody UpdateBookingRequest updateRequest) throws BookingNotUpdateException, BookingAlreadyCancelledException;

    @Operation(summary = "Search booking by ID",
            description = "Search and return the information for a specific booking using their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Booking found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookingResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Booking not found with the provided ID",
                    content = @Content
            )
    })
    ResponseEntity<BookingResponse> findById(@PathVariable Long bookingId) throws BookingNotFoundByIdException;

    @Operation(summary = "Search user by reference",
            description = "Search and return the information for a booking using their reference.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Booking found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookingResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Booking not found with the provided reference",
                    content = @Content
            )
    })
    ResponseEntity<BookingResponse> findByReference(@PathVariable String bookingReference) throws BookingNotFoundByReferenceException;

    @Operation(summary = "Confirm booking by ID",
            description = "Confirm a booking using their unique ID and Payment ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Confirmation completed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookingResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Confirmation failed, booking not found",
                    content = @Content
            )
    })
    ResponseEntity<BookingResponse> confirmBooking(@PathVariable Long bookingId, @RequestParam Long paymentId) throws BookingNotUpdateException, BookingNotFoundByIdException;

    @Operation(summary = "Cancel booking by ID",
            description = "Cancel a booking using their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cancellation completed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookingResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cancellation failed, booking not found",
                    content = @Content
            )
    })
    ResponseEntity<BookingResponse> cancelBooking(@PathVariable Long bookingId) throws BookingCannotBeCancelledException, BookingAlreadyCancelledException, BookingNotFoundByIdException;


    @Operation(summary = "Expired booking by ID",
            description = "Expire a booking using their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Expiration completed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookingResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Expiration failed, booking not found",
                    content = @Content
            )
    })
    ResponseEntity<BookingResponse> isBookingExpired(@PathVariable Long bookingId);

    @Operation(summary = "Find bookings by user ID",
            description = "Search and return all bookings associated with a specific user ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Bookings found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookingResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Bookings not found for the provided user ID",
                    content = @Content
            )
    })
    ResponseEntity<List<BookingResponse>> findBookingsByUserId(@PathVariable Long userId) throws UserBookingsNotFoundException;

    @Operation(summary = "Find bookings by user ID and status",
            description = "Search and return all bookings associated with a specific user ID and booking status.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Bookings found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookingResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Bookings not found for the provided user ID and status",
                    content = @Content
            )
    })
    ResponseEntity<List<BookingResponse>> findBookingsByUserIdAndStatus(@PathVariable Long userId, @RequestParam BookingStatus status) throws UserBookingsNotFoundException;

    @Operation(summary = "Cancel booking by ID",
            description = "Cancel a booking using their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cancellation completed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookingResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cancellation failed, booking not found",
                    content = @Content
            )
    })
    ResponseEntity<List<BookingResponse>> findCancelledBookingsByUserId(@PathVariable Long userId) throws UserBookingsNotFoundException;

    @Operation(summary = "Pending booking by ID",
            description = "Pending a booking using their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pending completed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookingResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pending failed, booking not found",
                    content = @Content
            )
    })
    ResponseEntity<List<BookingResponse>> findPendingBookingsByUserId(@PathVariable Long userId) throws UserBookingsNotFoundException;

    @Operation(summary = "Expired booking by ID",
            description = "Expire a booking using their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Expiration completed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookingResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Expiration failed, booking not found",
                    content = @Content
            )
    })
    ResponseEntity<List<BookingResponse>> findExpiredBookingsByUserId(@PathVariable Long userId) throws UserBookingsNotFoundException;


    @Operation(summary = "Find all expired bookings",
            description = "Find and return all bookings that have expired in the system.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Find completed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookingResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Find failed, no expired bookings found",
                    content = @Content
            )
    })
    ResponseEntity<List<BookingResponse>> findAllExpiredBookings();


    @Operation(summary = "Mark booking as expired by ID",
            description = "Mark a booking as expired using their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Find completed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookingResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Marking failed, booking not found",
                    content = @Content
            )
    })
    ResponseEntity<BookingResponse> markAsExpired(@PathVariable Long bookingId) throws BookingExpiredException, BookingNotFoundByIdException;

    @Operation(summary = "Mark booking as paid by ID",
            description = "Mark a booking as paid using their unique ID and Payment ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Find completed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookingResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Marking failed, booking not found",
                    content = @Content
            )
    })
    ResponseEntity<BookingResponse> markAsPaid(@PathVariable Long bookingId, @RequestParam Long paymentId) throws BookingNotUpdateException, BookingNotFoundByIdException;
}


