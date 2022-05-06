package com.davidasare.FoodOrderingSystem.payload.request;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

@Getter
@AllArgsConstructor
@ToString
public class RegistrationRequest {

    private final String name;
    private final String email;
    private final String password;
    private Set<String> role;

}
