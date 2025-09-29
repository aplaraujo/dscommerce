package com.example.dscommerce.dto;

import java.util.ArrayList;
import java.util.List;

import com.example.dscommerce.entities.Category;
import com.example.dscommerce.entities.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProductDTO {
    private Long id;

    // @NotNull // Valida se o campo não é nulo
    // @NotEmpty // valida se o campo não está vazio
    @NotBlank(message="Campo requerido") // Não aceita inclusive espaços em branco
    @Size(min=3, max=80, message="Nome precisa ter de 3 a 80 caracteres") // Valida o tamanho do texto
    private String name;

    @NotBlank(message="Campo requerido")
    @Size(min=10, message="Descrição precisa ter no mínimo 10 caracteres")
    private String description;

    @Positive(message="O preço deve ser positivo")
    private Double price;
    private String imgUrl;

    @NotEmpty(message="Deve haver pelo menos uma categoria")
    private final List<CategoryDTO> categories = new ArrayList<>();

    public ProductDTO() {}

    public ProductDTO(Long id, String name, String description, Double price, String imgUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public ProductDTO(Product entity) {
        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        price = entity.getPrice();
        imgUrl = entity.getImgUrl();

        for(Category category: entity.getCategories()) {
            categories.add(new CategoryDTO(category));
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

}
