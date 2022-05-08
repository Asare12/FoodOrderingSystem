package com.davidasare.FoodOrderingSystem.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "category_name")
    private String name;

    private String description;

    private String imageUrl;
    @OneToMany(mappedBy = "category", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    Set<Product> products;

    public Category(Long id, String name, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        //this.products = products;
    }

//    public Category(Long id, String name, String description, String imageUrl) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.imageUrl = imageUrl;
//    }
//    public Category() {
//        super();
//        products = new ArrayList<>();
//    }
//
//    public List<Product> getProduct() {
//        return products;
//    }
//
//    public void addProduct(Product product) {
//        products.add(product);
//    }
}
