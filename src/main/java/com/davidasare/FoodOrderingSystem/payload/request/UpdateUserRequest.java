package com.davidasare.FoodOrderingSystem.payload.request;

import lombok.*;

@Getter
@Setter
public class UpdateUserRequest {

    private Long id;
    private String name;
    private String email;
}