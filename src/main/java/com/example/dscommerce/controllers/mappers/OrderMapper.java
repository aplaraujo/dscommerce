package com.example.dscommerce.controllers.mappers;

import com.example.dscommerce.dto.OrderDTO;
import com.example.dscommerce.dto.ProductDTO;
import com.example.dscommerce.entities.Order;
import com.example.dscommerce.entities.Product;

import java.util.stream.Collectors;

public class OrderMapper {
    public static Order toEntity(OrderDTO dto) {
        Order order = new Order();
        order.setId(dto.id());
        order.setMoment(dto.moment());
        order.setStatus(dto.status());
        order.setClient(dto.client().getName());
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
}
