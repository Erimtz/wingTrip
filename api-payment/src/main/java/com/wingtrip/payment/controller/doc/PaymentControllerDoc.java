package com.wingtrip.payment.controller.doc;

import com.wingtrip.payment.controller.request.CreatePaymentRequest;
import com.wingtrip.payment.controller.response.PaymentResponse;
import com.wingtrip.payment.exception.*;
import com.wingtrip.payment.model.PaymentStatus;
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

@Tag(name = "Payment Controller", description = "Endpoints for managing payments")
public interface PaymentControllerDoc {

    @Operation(summary = "Create new payment",
            description = "Creates a new payment in the system.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Payment created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PaymentResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error creating payment", content = @Content)
    })
    ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody CreatePaymentRequest request) throws PaymentNotCreatedException;

    @Operation(summary = "Find payment by ID",
            description = "Search and return a specific payment using their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Payment found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PaymentResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Payment not found with the provided ID", content = @Content)
    })
    ResponseEntity<PaymentResponse> findById(@PathVariable Long paymentId) throws PaymentNotFoundException;

    @Operation(summary = "Find payment by Booking ID",
            description = "Search and return a payment associated with a specific booking ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Payment found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PaymentResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Payment not found for the provided booking ID", content = @Content)
    })
    ResponseEntity<List<PaymentResponse>> findByBookingId(@PathVariable Long bookingId) throws PaymentNotFoundException;

    @Operation(summary = "Find payments by status",
            description = "Search and return all payments with a specific status.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Payments found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PaymentResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "No payments found with the provided status", content = @Content)
    })
    ResponseEntity<List<PaymentResponse>> findByStatus(@RequestParam PaymentStatus status) throws PaymentNotFoundException;

    @Operation(summary = "Process payment by ID",
            description = "Process a pending payment using their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Payment processed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PaymentResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Payment already processed", content = @Content),
            @ApiResponse(responseCode = "404", description = "Payment not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Payment processing failed", content = @Content)
    })
    ResponseEntity<PaymentResponse> processPayment(@PathVariable Long paymentId) throws PaymentNotFoundException, PaymentAlreadyProcessedException, PaymentFailedException;

    @Operation(summary = "Refund payment by ID",
            description = "Refund a successful payment using their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Payment refunded successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PaymentResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Payment already refunded or cannot be refunded", content = @Content),
            @ApiResponse(responseCode = "404", description = "Payment not found", content = @Content)
    })
    ResponseEntity<PaymentResponse> refundPayment(@PathVariable Long paymentId) throws PaymentNotFoundException, PaymentAlreadyRefundedException, PaymentCannotBeRefundedException;

    @Operation(summary = "Update payment status by ID",
            description = "Update the status of a payment using their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Payment status updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PaymentResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Payment not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid status update", content = @Content)
    })
    ResponseEntity<PaymentResponse> updatePaymentStatus(@PathVariable Long paymentId, @RequestParam PaymentStatus status) throws PaymentNotFoundException, PaymentNotUpdatedException;

}
