package com.davidasare.FoodOrderingSystem.services;

import com.davidasare.FoodOrderingSystem.email.EmailSender;
import com.davidasare.FoodOrderingSystem.email.EmailService;
import com.davidasare.FoodOrderingSystem.model.User;
import com.davidasare.FoodOrderingSystem.model.ConfirmationToken;
import com.davidasare.FoodOrderingSystem.model.UserDetailsImpl;
import com.davidasare.FoodOrderingSystem.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private static String USER_NOT_FOUND_MSG = "user with email %s not found";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private ConfirmationTokenService confirmationTokenService;
    private EmailService emailService;
    private final EmailSender emailSender;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));

        return UserDetailsImpl.build(user);
    }

    public String signUpUser(User user) {
       boolean userExists =  userRepository.findByEmail(user.getEmail()).isPresent();

        String token = UUID.randomUUID().toString();

        if(userExists) {
            // TODO check of attributes are the same and
            // TODO if email not confirmed send confirmation email.

            throw new IllegalStateException("Email has been taken");
        }



       String encodePassword = bCryptPasswordEncoder.encode(user.getPassword());

       user.setPassword(encodePassword);

       userRepository.save(user);

       confirmationTokenService.saveConformationToken(createToken(user, token));

       return token;
    }

    public ConfirmationToken createToken(User user, String token){
        //String token = UUID.randomUUID().toString();
        return new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );
    }

//    public void resendConfirmationEmail(User user){
//        String newToken = UUID.randomUUID().toString();
//         ConfirmationToken token = createToken(user, newToken);
//        if(!user.isEnabled() && token.getExpiresAt().isAfter(LocalDateTime.now().plusMinutes(15))){
//            String link = "http://localhost:8080/api/auth/confirm?token=" + token;
//            emailSender.send(user.getEmail(), emailService.buildEmail(user.getName(), link));
//        }else{
//            throw new IllegalStateException("Could not send confirmation");
//        }
//    }

    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }
}
