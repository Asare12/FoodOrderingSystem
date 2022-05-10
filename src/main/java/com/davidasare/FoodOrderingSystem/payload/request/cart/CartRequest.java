package com.davidasare.FoodOrderingSystem.payload.request.cart;

import java.util.List;
import lombok.*;

@Getter
@Setter
public class CartRequest {

    private List<CartItemRequest> cartItems;
    private double totalCost;

    public CartRequest(List<CartItemRequest> cartItemDtoList, double totalCost) {
        this.cartItems = cartItemDtoList;
        this.totalCost = totalCost;
    }
}
