package com.davidasare.FoodOrderingSystem.payload.request.cart;

import java.util.List;
import lombok.*;

@Getter
@Setter
public class CartRequest {

    private List<CartItemRequest> cartItems;
    private double totalCost;

    public CartRequest(List<CartItemRequest> cartItemRequestList, double totalCost) {
        this.cartItems = cartItemRequestList;
        this.totalCost = totalCost;
    }
}
