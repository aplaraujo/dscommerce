package com.example.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dscommerce.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
