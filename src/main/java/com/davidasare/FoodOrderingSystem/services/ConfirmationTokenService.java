package com.davidasare.FoodOrderingSystem.services;

import com.davidasare.FoodOrderingSystem.model.ConfirmationToken;
import com.davidasare.FoodOrderingSystem.repository.ConfirmationTokenRepository;
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
     public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
     }

    public void deleteConfirmationToken(Long userID) {
        confirmationTokenRepository.deleteById(userID);
    }

}
