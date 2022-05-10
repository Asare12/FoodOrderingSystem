package com.davidasare.FoodOrderingSystem.services;

import com.davidasare.FoodOrderingSystem.model.ConfirmationToken;
import com.davidasare.FoodOrderingSystem.model.User;
import com.davidasare.FoodOrderingSystem.repository.ConfirmationTokenRepository;
import com.davidasare.FoodOrderingSystem.utils.Helper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConformationToken(ConfirmationToken token){
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token){
        return confirmationTokenRepository.findByToken(token);
    }
    public User getUser(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findUserByToken(token);
        if (Helper.notNull(confirmationToken)) {
            if (Helper.notNull(confirmationToken.getUser())) {
                return confirmationToken.getUser();
            }
        }
        return null;
    }

//    public Optional<ConfirmationToken> getToken(User user) {
//        return confirmationTokenRepository.findTokenByUser(user);
//    }

    public int setConfirmedAt(String token) {
    return confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }

    public void deleteConfirmationToken(Long userID) {
        confirmationTokenRepository.deleteById(userID);
    }

}
