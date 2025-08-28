package com.example.dscommerce.entities;

import java.util.Objects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

// Classe auxiliar que representa uma chave primária composta
@Embeddable
public class OrderItemPK {

    // Relacionamento muitos-para-um
    @ManyToOne
    @JoinColumn(name="order_id") // Chave estrangeira
    private Order order;

    @ManyToOne
    @JoinColumn(name="product_id") // Chave estrangeira
    private Product product;

    public OrderItemPK() {}

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.order);
        hash = 47 * hash + Objects.hashCode(this.product);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OrderItemPK other = (OrderItemPK) obj;
        if (!Objects.equals(this.order, other.order)) {
            return false;
        }
        return Objects.equals(this.product, other.product);
    }

    

    
}
