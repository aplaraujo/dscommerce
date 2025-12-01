package com.example.dscommerce.services;

import com.example.dscommerce.entities.Category;
import com.example.dscommerce.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public List<Category> search(String name) {
        if(name != null) {
            return categoryRepository.findByName(name);
        }

        return categoryRepository.findAll();
    }

}
