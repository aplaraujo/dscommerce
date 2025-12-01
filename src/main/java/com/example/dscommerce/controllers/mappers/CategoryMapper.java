package com.example.dscommerce.controllers.mappers;

import com.example.dscommerce.dto.CategoryDTO;
import com.example.dscommerce.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(source = "name", target = "name")
    Category toEntity(CategoryDTO dto);
    CategoryDTO toDTO(Category category);
}
