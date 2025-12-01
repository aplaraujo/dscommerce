package com.example.dscommerce.dto;

public record ProductSearchDTO(
        Long id,
        String name,
        String description,
        Double price,
        String imgUrl,
        CategoryDTO categoryDTO
) {
}
