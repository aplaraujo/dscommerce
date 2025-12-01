package com.example.dscommerce.dto;

public record OrderItemDTO(
        Long productId,
        String name,
        Double price,
        Integer quantity,
        String imgUrl
) {}
