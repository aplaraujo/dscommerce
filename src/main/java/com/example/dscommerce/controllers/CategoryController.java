package com.example.dscommerce.controllers;
import java.util.List;
import java.util.stream.Collectors;

import com.example.dscommerce.controllers.mappers.CategoryMapper;
import com.example.dscommerce.entities.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> search(
       @RequestParam(value = "name", required = false) String name
    ) {
        List<Category> list = categoryService.search(name);
        List<CategoryDTO> dto = list.stream().map(categoryMapper::toDTO).toList();
        return ResponseEntity.ok(dto);
    }

}
