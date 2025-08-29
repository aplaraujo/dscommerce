package com.example.dscommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dscommerce.dto.ProductDTO;
import com.example.dscommerce.services.ProductService;

@RestController // Configurar a resposta da API
@RequestMapping(value="/products") // Configurar a rota
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(value="/{id}")
    public ProductDTO findById(@PathVariable Long id) {
        ProductDTO dto = productService.findById(id);
        return dto;
    }

    @GetMapping
    public Page<ProductDTO> findAll(Pageable pageable) {
        return productService.findAll(pageable);
    }
}
