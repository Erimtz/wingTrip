package com.wingtrip.payment.controller.mapper;

import com.wingtrip.payment.controller.request.CreatePaymentRequest;
import com.wingtrip.payment.controller.response.PaymentResponse;
import com.wingtrip.payment.dto.PaymentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {

    PaymentDTO toDTO(CreatePaymentRequest request);

    PaymentResponse toResponse(PaymentDTO paymentDTO);
}
