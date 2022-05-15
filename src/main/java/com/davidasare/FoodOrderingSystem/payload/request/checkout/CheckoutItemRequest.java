package com.davidasare.FoodOrderingSystem.payload.request.checkout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutItemRequest {

    private String productName;
    private int  quantity;
    private double price;
    private Long productId;
    private Long userId;
}
