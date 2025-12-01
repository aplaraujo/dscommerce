package com.example.dscommerce.repositories.specs;

import com.example.dscommerce.entities.Category;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecs {
    public static Specification<Category> nameLike(String name) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), "%" + name.toUpperCase() + "%")
        );
    }
}
