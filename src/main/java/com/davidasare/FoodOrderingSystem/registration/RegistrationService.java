package com.davidasare.FoodOrderingSystem.registration;

import com.davidasare.FoodOrderingSystem.registration.token.ConfirmationToken;
import com.davidasare.FoodOrderingSystem.registration.token.ConfirmationTokenService;
import com.davidasare.FoodOrderingSystem.user.User;
import com.davidasare.FoodOrderingSystem.user.UserRole;
import com.davidasare.FoodOrderingSystem.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private UserService userService;
    private EmailValidation emailValidation;
    private ConfirmationTokenService confirmationTokenService;

    public String register(RegistrationRequest request) {

        boolean isValidEmail = emailValidation.test(request.getEmail());
        if(!isValidEmail){
            throw new IllegalStateException("Email not valid");
        }
        return userService.signUpUser(
                new User(
                        request.getName(), request.getEmail(),request.getPassword(), UserRole.USER));
    }

    @Transactional
    public String confirmToken(String token){
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(
                () -> new IllegalStateException("token not found"));

        if(confirmationToken.getConfirmedAt() != null){
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if(expiredAt.isBefore(LocalDateTime.now())){
            throw new IllegalStateException("token expired");
        }
        confirmationTokenService.setConfirmedAt(token);
        userService.enableUser(confirmationToken.getUser().getEmail());

        return "confirmed";
    }

}
