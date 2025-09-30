package com.example.dscommerce.services;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dscommerce.dto.OrderDTO;
import com.example.dscommerce.dto.OrderItemDTO;
import com.example.dscommerce.entities.Order;
import com.example.dscommerce.entities.OrderItem;
import com.example.dscommerce.entities.Product;
import com.example.dscommerce.entities.User;
import com.example.dscommerce.entities.enums.OrderStatus;
import com.example.dscommerce.repositories.OrderItemRepository;
import com.example.dscommerce.repositories.OrderRepository;
import com.example.dscommerce.repositories.ProductRepository;
import com.example.dscommerce.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Optional<Order> result = orderRepository.findById(id);
        Order order = result.orElseThrow(() -> new ResourceNotFoundException("Resource not found!"));
        OrderDTO dto = new OrderDTO(order);
        return dto;
    }

    @Transactional
    public OrderDTO insert(OrderDTO dto) {
        Order order = new Order();
        order.setMoment(Instant.now());
        order.setStatus(OrderStatus.WAITING_PAYMENT);
        User user = userService.authenticated();
        order.setClient(user);

        for(OrderItemDTO itemDTO: dto.getItems()) {
            Product product = productRepository.getReferenceById(itemDTO.getProductId());
            OrderItem item = new OrderItem(order, product, itemDTO.getQuantity(), product.getPrice());
            order.getItems().add(item);
        }

        orderRepository.save(order);
        orderItemRepository.saveAll(order.getItems());
        return new OrderDTO(order);
    }
}
