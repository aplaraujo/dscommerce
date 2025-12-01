package com.example.dscommerce.controllers;
import java.util.List;

import com.example.dscommerce.controllers.mappers.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dscommerce.dto.CategoryDTO;
import com.example.dscommerce.services.CategoryService;

@RestController // Configurar a resposta da API
@RequestMapping(value="/categories") // Configurar a rota
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable("id") String id) {
        var categoryId = Long.parseLong(id);

        return categoryService.findById(categoryId).map(cat -> {
            CategoryDTO dto = categoryMapper.toDTO(cat);
            return ResponseEntity.ok(dto);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
