package com.example.dscommerce.tests;

import com.example.dscommerce.entities.Category;

public class CategoryFactory {
    public static Category createCategory() {
        return new Category(1L, "Jogos");
    }

    public static Category createCategory(Long id, String name) {
        return new Category(id, name);
    }
}
