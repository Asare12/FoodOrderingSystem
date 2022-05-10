package com.davidasare.FoodOrderingSystem.payload.request.cart;

import com.davidasare.FoodOrderingSystem.model.Cart;
import com.davidasare.FoodOrderingSystem.model.Product;
import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class CartItemRequest {

    private Long id;
    private @NotNull Integer quantity;
    private @NotNull Product product;

    public CartItemRequest(Cart cart) {
        this.setId(cart.getId());
        this.setQuantity(cart.getQuantity());
        this.setProduct(cart.getProduct());
    }

}
