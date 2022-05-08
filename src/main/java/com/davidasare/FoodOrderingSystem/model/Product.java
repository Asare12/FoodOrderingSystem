package com.davidasare.FoodOrderingSystem.model;

import com.davidasare.FoodOrderingSystem.payload.request.ProductRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String price;
    private String description;
    private String imageUrl;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    Category category;
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn
//    private Category category;

    public Product(ProductRequest productRequest , Category category) {
        this.name = productRequest.getName();
        this.imageUrl = productRequest.getImageUrl();
        this.description = productRequest.getDescription();
        this.price = productRequest.getPrice();
        this.category = category;
    }

}
