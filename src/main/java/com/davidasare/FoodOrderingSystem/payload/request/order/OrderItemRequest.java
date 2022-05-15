package com.davidasare.FoodOrderingSystem.payload.request.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {

    private @NotNull double price;
    private @NotNull int quantity;
    private @NotNull Long orderId;
    private @NotNull Long productId;
}
