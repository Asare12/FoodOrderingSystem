package com.davidasare.FoodOrderingSystem.controllers;


import com.davidasare.FoodOrderingSystem.model.User;
import com.davidasare.FoodOrderingSystem.payload.request.user.UpdateUserRequest;
import com.davidasare.FoodOrderingSystem.payload.response.ApiResponse;
import com.davidasare.FoodOrderingSystem.services.ConfirmationTokenService;
import com.davidasare.FoodOrderingSystem.services.UserService;
import com.davidasare.FoodOrderingSystem.utils.Helper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "api/")
@AllArgsConstructor
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    ConfirmationTokenService confirmationTokenService;

    @GetMapping("/users")
    public List<User> getUsers(@RequestParam boolean req) {

      return (req) ? userService.listOfUsers().stream().sorted(Comparator.comparing(User::getId)).limit(5).collect(Collectors.toList()) : userService.listOfUsers();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Optional<User>> getUser(@PathVariable Long id){
        Optional<User> user = userService.readUser(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/user/update/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity updateUser(@PathVariable Long id,@Valid @RequestBody UpdateUserRequest updateUserRequest) {

        // Check to see if the user exists.
        if (Helper.notNull(userService.readUser(id))) {
            // If the category exists then update it.
            User updatedUser = userService.updateUser(id, updateUserRequest);
            return ResponseEntity.ok(updatedUser);
        }
        return new ResponseEntity<ApiResponse>(new ApiResponse(false, "user does not exist"), HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/user/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteUser(@PathVariable Long id){

        confirmationTokenService.deleteConfirmationToken(id);
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }


}