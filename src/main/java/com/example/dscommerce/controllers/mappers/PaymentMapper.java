package com.example.dscommerce.controllers.mappers;

import com.example.dscommerce.dto.PaymentDTO;
import com.example.dscommerce.entities.Payment;

public class PaymentMapper {
    public static Payment toEntity(PaymentDTO dto) {
        Payment payment  = new Payment();
        payment.setId(dto.id());
        payment.setMoment(dto.moment());
        return payment;
    }

    public static PaymentDTO toDTO(Payment entity) {
        return new PaymentDTO(
                entity.getId(),
                entity.getMoment()
        );
    }
}
