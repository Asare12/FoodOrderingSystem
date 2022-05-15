package com.davidasare.FoodOrderingSystem.payload.request.user;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateUserRequest {

    private Long id;
    private String name;
    private String email;

}