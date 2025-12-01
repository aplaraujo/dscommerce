package com.example.dscommerce.dto;

import com.example.dscommerce.entities.enums.OrderStatus;
import jakarta.validation.constraints.NotEmpty;

import java.time.Instant;
import java.util.List;

public record OrderDTO(
        Long id,
        Instant moment,
        OrderStatus status,
        ClientDTO client,
        PaymentDTO payment,

        @NotEmpty(message="Deve haver pelo menos um item")
        List<OrderItemDTO> items
) {
}
