package com.example.dscommerce.entities;

import java.util.Objects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Classe auxiliar que representa uma chave primária composta
@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class OrderItemPK {

    // Relacionamento muitos-para-um
    @ManyToOne
    @JoinColumn(name="order_id") // Chave estrangeira
    private Order order;

    @ManyToOne
    @JoinColumn(name="product_id") // Chave estrangeira
    private Product product;

}
