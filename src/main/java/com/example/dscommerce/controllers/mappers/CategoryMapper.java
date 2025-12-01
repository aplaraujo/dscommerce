package com.example.dscommerce.controllers.mappers;

import com.example.dscommerce.dto.CategoryDTO;
import com.example.dscommerce.entities.Category;

public class CategoryMapper {
    public static Category toEntity(CategoryDTO dto) {
        Category category = new Category();
        category.setId(dto.id());
        category.setName(dto.name());
        return category;
    }

    public static CategoryDTO toDTO(Category entity) {
        return new CategoryDTO(
                entity.getId(),
                entity.getName()
        );
    }
}
