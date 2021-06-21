package com.davidasare.FoodOrderingSystem.registration;

import com.davidasare.FoodOrderingSystem.email.EmailSender;
import com.davidasare.FoodOrderingSystem.email.EmailService;
import com.davidasare.FoodOrderingSystem.registration.token.ConfirmationToken;
import com.davidasare.FoodOrderingSystem.registration.token.ConfirmationTokenService;
import com.davidasare.FoodOrderingSystem.user.User;
import com.davidasare.FoodOrderingSystem.user.UserRole;
import com.davidasare.FoodOrderingSystem.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailValidation emailValidation;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private EmailService emailService;

    public RegistrationService(){}

    public String register(RegistrationRequest request) {

        boolean isValidEmail = emailValidation.test(request.getEmail());
        if(!isValidEmail){
            throw new IllegalStateException("Email not valid");
        }
        String token = userService.signUpUser(
                new User(
                        request.getName(), request.getEmail(), request.getPassword(), UserRole.USER));

        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;
        emailSender.send(request.getEmail(), emailService.buildEmail(request.getName(), link));
        return token;
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
