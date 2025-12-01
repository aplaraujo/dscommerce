package com.example.dscommerce.controllers.mappers;

import com.example.dscommerce.dto.ProductDTO;
import com.example.dscommerce.dto.ProductSearchDTO;
import com.example.dscommerce.entities.Category;
import com.example.dscommerce.entities.Product;

import java.util.List;
import java.util.stream.Collectors;

public abstract class ProductMapper {

    public static Product toEntity(ProductDTO dto) {
        Product product = new Product();
        product.setId(dto.id());
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setImgUrl(dto.imgUrl());

        // Converte as categorias do DTO para entidades Category
        if (dto.categories() != null && !dto.categories().isEmpty()) {
            product.setCategories(
                    dto.categories().stream()
                            .map(CategoryMapper::toEntity)
                            .collect(Collectors.toSet())
            );
        }

        return product;
    }

    public static ProductDTO toDTO(Product entity) {
        return new ProductDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getImgUrl(),
                entity.getCategories().stream()
                        .map(CategoryMapper::toDTO)
                        .collect(Collectors.toList())
        );
    }

    public static List<ProductSearchDTO> toSearchDTOList(Product entity) {
        return entity.getCategories().stream()
                .map(category -> new ProductSearchDTO(
                        entity.getId(),
                        entity.getName(),
                        entity.getDescription(),
                        entity.getPrice(),
                        entity.getImgUrl(),
                        CategoryMapper.toDTO(category)
                ))
                .collect(Collectors.toList());
    }

    public static ProductSearchDTO toSearchDTO(Product entity, Category category) {
        return new ProductSearchDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getImgUrl(),
                CategoryMapper.toDTO(category)
        );
    }

    public static void updateEntityFromDTO(Product entity, ProductDTO dto) {
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setPrice(dto.price());
        entity.setImgUrl(dto.imgUrl());

        // Atualiza as categorias
        if (dto.categories() != null) {
            entity.getCategories().clear();
            entity.getCategories().addAll(
                    dto.categories().stream()
                            .map(CategoryMapper::toEntity)
                            .collect(Collectors.toSet())
            );
        }
    }
}
