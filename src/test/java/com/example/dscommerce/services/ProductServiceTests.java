package com.example.dscommerce.services;

import com.example.dscommerce.dto.ProductDTO;
import com.example.dscommerce.dto.ProductMinDTO;
import com.example.dscommerce.entities.Category;
import com.example.dscommerce.entities.Product;
import com.example.dscommerce.repositories.CategoryRepository;
import com.example.dscommerce.repositories.ProductRepository;
import com.example.dscommerce.services.exceptions.DatabaseException;
import com.example.dscommerce.services.exceptions.ResourceNotFoundException;
import com.example.dscommerce.tests.CategoryFactory;
import com.example.dscommerce.tests.ProductFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private Product product;
    private Long existingId;
    private Long notExistingId;
    private Long dependentId;
    private String productName;
    private PageImpl<Product> page;
    private Category category;
    private ProductDTO dto;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        notExistingId = 100L;
        dependentId = 3L;
        productName = "PlayStation 5";
        product = ProductFactory.createProduct(productName);
        dto = ProductFactory.createProductDTO();
        category = CategoryFactory.createCategory();
        page = new PageImpl<Product>(List.of(product));

        when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        when(productRepository.findById(notExistingId)).thenReturn(Optional.empty());
        when(productRepository.searchByName(any(), (Pageable)any())).thenReturn(page);
        when(categoryRepository.findById(existingId)).thenReturn(Optional.of(category));
        when(productRepository.save(any())).thenReturn(product);
        when(productRepository.getReferenceById(existingId)).thenReturn(product);
        when(productRepository.getReferenceById(notExistingId)).thenThrow(ResourceNotFoundException.class);
        when(productRepository.existsById(existingId)).thenReturn(true);
        when(productRepository.existsById(dependentId)).thenReturn(true);
        when(productRepository.existsById(notExistingId)).thenReturn(false);
        doNothing().when(productRepository).deleteById(existingId);
        doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() {
        ProductDTO result = productService.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
        Assertions.assertEquals(product.getName(), result.getName());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.findById(notExistingId);
        });
    }

    @Test
    public void findAllShouldReturnPagedProductMinDTO() {
        Pageable pageable = PageRequest.of(0, 12);
        Page<ProductMinDTO> result = productService.findAll(productName, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getSize());
        Assertions.assertEquals(productName, result.iterator().next().getName());
    }

    @Test
    public void insertShouldReturnProductDTO() {
        ProductDTO result = productService.insert(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(dto.getName(), result.getName());
        Assertions.assertEquals(dto.getDescription(), result.getDescription());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() {
        ProductDTO result = productService.update(existingId, dto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
        Assertions.assertEquals(dto.getName(), result.getName());
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.update(notExistingId, dto);
        });
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {
            productService.delete(existingId);
        });
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(notExistingId);
        });
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(DatabaseException.class, () -> {
            productService.delete(dependentId);
        });
    }

}
