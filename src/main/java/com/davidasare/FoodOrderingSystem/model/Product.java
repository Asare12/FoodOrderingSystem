package com.davidasare.FoodOrderingSystem.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private String price;
    private String description;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Category category;

}
