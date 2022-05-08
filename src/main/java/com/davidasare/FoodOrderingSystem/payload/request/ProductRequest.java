package com.davidasare.FoodOrderingSystem.payload.request;

import com.davidasare.FoodOrderingSystem.model.Product;
import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class ProductRequest {
    private Long id;
    private @NotNull String name;
    private @NotNull String imageUrl;
    private @NotNull String price;
    private @NotNull String description;
    private @NotNull Long categoryId;

    public ProductRequest(Product product) {
        this.setId(product.getId());
        this.setName(product.getName());
        this.setImageUrl(product.getImageUrl());
        this.setDescription(product.getDescription());
        this.setPrice(product.getPrice());
        this.setCategoryId(product.getCategory().getId());
    }
    public ProductRequest(@NotNull String name, @NotNull String imageUrl, @NotNull String price, @NotNull String description, @NotNull Long categoryId) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.description = description;
        this.categoryId = categoryId;
    }
}
