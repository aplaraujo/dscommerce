package com.example.dscommerce.controllers.mappers;

import com.example.dscommerce.dto.OrderDTO;
import com.example.dscommerce.entities.Order;
import com.example.dscommerce.entities.OrderItem;

import java.util.Set;
import java.util.stream.Collectors;

public class OrderMapper {
    private final OrderItemMapper orderItemMapper;

    public OrderMapper(OrderItemMapper orderItemMapper) {
        this.orderItemMapper = orderItemMapper;
    }


    public OrderDTO toDTO(Order order) {
        if (order == null) return null;

        return new OrderDTO(
                order.getId(),
                order.getMoment(),
                order.getStatus(),
                ClientMapper.toDTO(order.getClient().getId()),
                PaymentMapper.toDTO(order.getPayment()),
                order.getItems().stream()
                        .map(orderItemMapper::toDTO)
                        .toList()
        );
    }

    public Order toEntity(OrderDTO dto) {
        if (dto == null) return null;

        Order order = new Order();
        order.setId(dto.id());
        order.setMoment(dto.moment());
        order.setStatus(dto.status());
        order.setClient(ClientMapper.toEntity(dto.client()));
        order.setPayment(PaymentMapper.toEntity(dto.payment()));

        // Agora funciona!
        Set<OrderItem> items = dto.items().stream()
                .map(itemDTO -> orderItemMapper.toEntity(itemDTO, order))
                .collect(Collectors.toSet());;
        order.setItems(items);

        return order;
    }
}
