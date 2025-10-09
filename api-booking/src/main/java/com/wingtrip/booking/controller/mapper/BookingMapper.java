package com.wingtrip.booking.controller.mapper;

import com.wingtrip.booking.controller.request.CreateBookingRequest;
import com.wingtrip.booking.controller.request.UpdateBookingRequest;
import com.wingtrip.booking.controller.response.BookingResponse;
import com.wingtrip.booking.dto.BookingDTO;

import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingDTO toDTO(CreateBookingRequest createRequest);

    BookingDTO toDTO(UpdateBookingRequest updateRequest);

    BookingResponse toResponse(BookingDTO bookingDTO);

}
