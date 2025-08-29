package com.example.dscommerce.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dscommerce.entities.Product;
import com.example.dscommerce.repositories.ProductRepository;

@RestController // Configurar a resposta da API
@RequestMapping(value="/products") // Configurar a rota
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String teste() {
        Optional<Product> result = productRepository.findById(1L);
        Product product = result.get();
        return product.getName();
    }
}
