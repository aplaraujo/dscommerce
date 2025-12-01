package com.example.dscommerce.repositories.specs;

import com.example.dscommerce.entities.Category;
import com.example.dscommerce.entities.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecs {
    public static Specification<Product> nameLike(String name) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), "%" + name.toUpperCase() + "%")
        );
    }

    public static Specification<Product> categoryEqual(Category category) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category"), category)
        );
    }
}
