package com.davidasare.FoodOrderingSystem.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class JwtResponse {

    private Long id;
    private String email;
    private List<String> roles;
    private String accessToken;
    private String type = "Bearer";

    public JwtResponse(String accessToken, Long id, String email, List<String> roles) {
        this.accessToken = accessToken;
        this.id = id;
        this.email = email;
        this.roles = roles;
    }

}
