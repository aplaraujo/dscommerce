package com.example.dscommerce.services;

import com.example.dscommerce.controllers.mappers.ProductMapper;
import com.example.dscommerce.dto.ProductDTO;
import com.example.dscommerce.entities.Product;
import com.example.dscommerce.repositories.CategoryRepository;
import com.example.dscommerce.repositories.ProductRepository;
import com.example.dscommerce.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = productRepository.findWithCategoriesById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
        return ProductMapper.toDTO(product);
    }

//    public ProductDTO findById(Long id) {
//        Optional<Product> result = productRepository.findById(id);
//        Product product = result.orElseThrow(() -> new ResourceNotFoundException("Resource not found!"));
//        ProductDTO dto = new ProductDTO(product);
//        return dto;
//    }
//
//    public Page<ProductMinDTO> findAll(String name, Pageable pageable) {
//        Page<Product> products = productRepository.searchByName(name, pageable);
//        return products.map(prod -> new ProductMinDTO(prod));
//    }
//
//    public ProductDTO insert(ProductDTO dto) {
//        Product entity = new Product();
//        copyDtoToEntity(dto, entity);
//
//        entity = productRepository.save(entity);
//        return new ProductDTO(entity);
//    }
//
//    public ProductDTO update(Long id, ProductDTO dto) {
//        Product entity = productRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
//        copyDtoToEntity(dto, entity);
//        entity = productRepository.save(entity);
//        return new ProductDTO(entity);
//    }
//
//
//    public void delete(Long id) {
//        if (!productRepository.existsById(id)) {
//            throw new ResourceNotFoundException("Recurso não encontrado");
//        }
//        try {
//            productRepository.deleteById(id);
//        } catch (DataIntegrityViolationException e) {
//            throw new DatabaseException("Falha de integridade referencial");
//        }
//    }

//    private void copyDtoToEntity(ProductDTO dto, Product entity) {
//        entity.setName(dto.getName());
//        entity.setDescription(dto.getDescription());
//        entity.setPrice(dto.getPrice());
//        entity.setImgUrl(dto.getImgUrl());
//
//        entity.getCategories().clear();
//        for (CategoryDTO catDTO : dto.getCategories()) {
//            Category cat = categoryRepository.findById(catDTO.getId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada: " + catDTO.getId()));
//            entity.getCategories().add(cat);
//        }
//    }
}
