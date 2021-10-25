package com.davidasare.FoodOrderingSystem.controllers;

import com.davidasare.FoodOrderingSystem.model.User;
import com.davidasare.FoodOrderingSystem.payload.request.RegistrationRequest;
import com.davidasare.FoodOrderingSystem.services.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:5000")
@RestController
@RequestMapping(path = "api/auth")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request){
        return new ResponseEntity(registrationService.register(request), HttpStatus.CREATED);
    }
    @GetMapping(path = "/confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token) {
        User user =  registrationService.confirmToken(token);
        return new ResponseEntity(user, HttpStatus.OK);
    }
}
