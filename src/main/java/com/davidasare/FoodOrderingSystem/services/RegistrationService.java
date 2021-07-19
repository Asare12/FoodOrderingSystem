package com.davidasare.FoodOrderingSystem.services;

import com.davidasare.FoodOrderingSystem.email.EmailSender;
import com.davidasare.FoodOrderingSystem.email.EmailService;
import com.davidasare.FoodOrderingSystem.model.Role;
import com.davidasare.FoodOrderingSystem.model.ConfirmationToken;
import com.davidasare.FoodOrderingSystem.payload.request.RegistrationRequest;
import com.davidasare.FoodOrderingSystem.services.ConfirmationTokenService;
import com.davidasare.FoodOrderingSystem.repository.RoleRepository;
import com.davidasare.FoodOrderingSystem.model.User;
import com.davidasare.FoodOrderingSystem.model.UserRole;
import com.davidasare.FoodOrderingSystem.security.EmailValidation;
import com.davidasare.FoodOrderingSystem.services.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class RegistrationService {

    @Autowired
    private UserDetailsServiceImpl userService;
    @Autowired
    private EmailValidation emailValidation;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RoleRepository roleRepository;

    public RegistrationService(){}

    public String register(RegistrationRequest request) {

        boolean isValidEmail = emailValidation.test(request.getEmail());
        if(!isValidEmail){
            throw new IllegalStateException("Email not valid");
        }
        User user = new User(request.getName(), request.getEmail(), request.getPassword());

        Set<String> strRoles = request.getRole();
        Set<Role> roles = new HashSet<>();

        if(strRoles == null) {
            Role userRole = roleRepository.findByName(UserRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if ("admin".equals(role)) {
                    Role adminRole = roleRepository.findByName(UserRole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Admin Role is not found."));
                    roles.add(adminRole);
                }else if("user".equals(role)) {
                    Role userRole = roleRepository.findByName(UserRole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: User Role is not found."));
                    roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);

        String token = userService.signUpUser(user);

        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;
        emailSender.send(request.getEmail(), emailService.buildEmail(request.getName(), link));
        return token;
    }

    @Transactional
    public User confirmToken(String token){
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

        return confirmationToken.getUser();
    }

}
