package com.davidasare.FoodOrderingSystem.services;

import com.davidasare.FoodOrderingSystem.model.User;
import com.davidasare.FoodOrderingSystem.payload.request.user.UpdateUserRequest;
import com.davidasare.FoodOrderingSystem.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<User> listOfUsers() { return userRepository.findAll();}

    public void createUser(User user) {
        userRepository.save(user);
    }

    public Optional<User> readUser(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User>  readUser(String userName) {
        return userRepository.findByEmail(userName);
    }

    public User updateUser(Long userID, UpdateUserRequest newUser) {
        User user = userRepository.findById(userID).get();
        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());
        userRepository.save(user);
        return user;
    }

    public void deleteUser(Long userID) {
        userRepository.deleteById(userID);
    }

}
