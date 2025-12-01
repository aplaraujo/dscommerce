package com.example.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dscommerce.entities.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>{
    List<Category> findByName(String name);
    Optional<Category> searchByName(String name);
}
