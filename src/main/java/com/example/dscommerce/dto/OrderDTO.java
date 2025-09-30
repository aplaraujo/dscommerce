package com.example.dscommerce.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.example.dscommerce.entities.Order;
import com.example.dscommerce.entities.OrderItem;
import com.example.dscommerce.entities.enums.OrderStatus;

public class OrderDTO {
    private Long id;
    private Instant moment;
    private OrderStatus status;
    private ClientDTO client;
    private PaymentDTO payment;
    private final List<OrderItemDTO> items = new ArrayList<>();

    public OrderDTO() {}

    public OrderDTO(ClientDTO client, Long id, Instant moment, PaymentDTO payment, OrderStatus status) {
        this.client = client;
        this.id = id;
        this.moment = moment;
        this.payment = payment;
        this.status = status;
    }

    public OrderDTO(Order entity) {
        this.client = new ClientDTO(entity.getClient());
        this.id = entity.getId();
        this.moment = entity.getMoment();
        this.payment = (entity.getPayment() == null) ? null : new PaymentDTO(entity.getPayment());
        this.status = entity.getStatus();

        for(OrderItem item: entity.getItems()) {
            OrderItemDTO itemDTO = new OrderItemDTO(item);
            items.add(itemDTO);
        }
    }

    public Long getId() {
        return id;
    }

    public Instant getMoment() {
        return moment;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public ClientDTO getClient() {
        return client;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public Double getTotal() {
        double sum = 0.0;
        for(OrderItemDTO item: items) {
            sum = sum + item.getSubTotal();
        }
        return sum;
    }
}
