package com.example.dscommerce.controllers;
import java.net.URI;

import com.example.dscommerce.controllers.mappers.ProductMapper;
import com.example.dscommerce.dto.ProductSearchDTO;
import com.example.dscommerce.entities.Category;
import com.example.dscommerce.entities.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.dscommerce.dto.ProductDTO;
import com.example.dscommerce.dto.ProductMinDTO;
import com.example.dscommerce.services.ProductService;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;

import jakarta.validation.Valid;

@RestController // Configurar a resposta da API
@RequestMapping(value = "/products") // Configurar a rota
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable String id) {
        var productId = Long.parseLong(id);
        ProductDTO dto = productService.findById(productId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "name") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<ProductDTO> products = productService.findAll(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductDTO>> searchByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "name") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<ProductDTO> products = productService.searchByName(name, pageable);
        return ResponseEntity.ok(products);
    }

//    @Autowired
//    private ProductService productService;
//
//    @GetMapping(value="/{id}")
//    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
//        ProductDTO dto = productService.findById(id);
//        return ResponseEntity.ok(dto);
//    }
//
//    @GetMapping
//    public ResponseEntity<Page<ProductMinDTO>> findAll(@RequestParam(name = "name", defaultValue = "") String name, Pageable pageable) {
//        Page<ProductMinDTO> dto = productService.findAll(name, pageable);
//        return ResponseEntity.ok(dto);
//    }
//
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
//    @PostMapping
//    public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO dto) {
//        dto = productService.insert(dto);
//        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.id()).toUri();
//        return ResponseEntity.created(uri).body(dto);
//    }
//
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
//    @PutMapping(value="/{id}")
//    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) throws InvalidDefinitionException {
//        dto = productService.update(id, dto);
//        return ResponseEntity.ok(dto);
//    }
//
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
//    @DeleteMapping(value="/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        productService.delete(id);
//        return ResponseEntity.noContent().build();
//    }
}
