package com.example.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dscommerce.entities.OrderItem;
import com.example.dscommerce.entities.OrderItemPK;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK>{

}
