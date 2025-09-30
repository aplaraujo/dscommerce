package com.example.dscommerce.dto;

import com.example.dscommerce.entities.OrderItem;

public class OrderItemDTO {
    private Long productId;
    private String name;
    private Double price;
    private Integer quantity;

    public OrderItemDTO() {}

    public OrderItemDTO(String name, Double price, Long productId, Integer quantity) {
        this.name = name;
        this.price = price;
        this.productId = productId;
        this.quantity = quantity;
    }

    public OrderItemDTO(OrderItem entity) {
        this.name = entity.getProduct().getName();
        this.price = entity.getPrice();
        this.productId = entity.getProduct().getId();
        this.quantity = entity.getQuantity();
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getSubTotal() {
        return price * quantity;
    }
}
