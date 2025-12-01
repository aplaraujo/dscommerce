package com.example.dscommerce.controllers;

import com.example.dscommerce.controllers.mappers.CategoryMapper;
import com.example.dscommerce.dto.CategoryDTO;
import com.example.dscommerce.entities.Category;
import com.example.dscommerce.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Configurar a resposta da API
@RequestMapping(value="/categories") // Configurar a rota
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable("id") String id) {
        var categoryId = Long.parseLong(id);

        return categoryService.findById(categoryId).map(cat -> {
            CategoryDTO dto = CategoryMapper.toDTO(cat);
            return ResponseEntity.ok(dto);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> search(
       @RequestParam(value = "name", required = false) String name
    ) {
        List<Category> list = categoryService.search(name);
        List<CategoryDTO> dto = list.stream().map(CategoryMapper::toDTO).toList();
        return ResponseEntity.ok(dto);
    }

}
