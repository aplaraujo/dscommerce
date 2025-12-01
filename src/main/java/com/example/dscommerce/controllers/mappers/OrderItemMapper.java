package com.example.dscommerce.controllers.mappers;

import com.example.dscommerce.dto.OrderItemDTO;
import com.example.dscommerce.entities.Order;
import com.example.dscommerce.entities.OrderItem;
import com.example.dscommerce.entities.Product;
import com.example.dscommerce.repositories.ProductRepository;
import com.example.dscommerce.services.exceptions.ResourceNotFoundException;

import java.util.List;

public class OrderItemMapper {

    private final ProductRepository productRepository;

    public OrderItemMapper(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public OrderItemDTO toDTO(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        Product product = orderItem.getProduct();

        return new OrderItemDTO(
                product.getId(),
                product.getName(),
                orderItem.getPrice(),
                orderItem.getQuantity(),
                product.getImgUrl()
        );
    }

    public OrderItem toEntity(OrderItemDTO dto, Order order, Product product) {
        if (dto == null) {
            return null;
        }

        return new OrderItem(
                order,
                product,
                dto.quantity(),
                dto.price()
        );
    }

    public OrderItem toEntity(OrderItemDTO dto, Order order) {
        if (dto == null) return null;

        Product product = productRepository.findById(dto.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return toEntity(dto, order, product);
    }

    public List<OrderItemDTO> toDTOList(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::toDTO)
                .toList();
    }
}
