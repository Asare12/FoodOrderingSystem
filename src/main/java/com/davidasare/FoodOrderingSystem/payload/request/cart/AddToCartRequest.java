package com.davidasare.FoodOrderingSystem.payload.request.cart;

import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class AddToCartRequest {

    private Long id;
    private @NotNull Long productId;
    private @NotNull Integer quantity;
}
