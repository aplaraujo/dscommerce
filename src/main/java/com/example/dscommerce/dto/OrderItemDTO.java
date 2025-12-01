package com.example.dscommerce.dto;

import com.example.dscommerce.entities.OrderItem;

public record OrderItemDTO(
        Long productId,
        String name,
        Double price,
        Integer quantity,
        String imgUrl
) {}
