package com.davidasare.FoodOrderingSystem.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany
    private List<Product> products;

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public Category() {
        super();
        products = new ArrayList<>();
    }

    public List<Product> getProduct() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }
}
