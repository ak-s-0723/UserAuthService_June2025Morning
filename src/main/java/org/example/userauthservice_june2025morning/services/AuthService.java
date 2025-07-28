package org.example.userauthservice_june2025morning.services;

import org.example.userauthservice_june2025morning.exceptions.PasswordMismatchException;
import org.example.userauthservice_june2025morning.exceptions.UserAlreadySignedUpException;
import org.example.userauthservice_june2025morning.exceptions.UserNotRegisteredException;
import org.example.userauthservice_june2025morning.models.User;
import org.example.userauthservice_june2025morning.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

//ToDo : Define Exception Handlers and Controller Advisor for Exceptions
@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public User signup(String name, String email, String password, String phoneNumber) {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if(userOptional.isPresent()) {
           throw new UserAlreadySignedUpException("Please login directly...");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        return userRepo.save(user);
     }

    @Override
    public User login(String email, String password) {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if(userOptional.isEmpty()) {
           throw new UserNotRegisteredException("Please signup first...");
        }

        User user = userOptional.get();

        if(!user.getPassword().equals(password)) {
            throw new PasswordMismatchException("Please add correct password...");
        }

        return user;
    }

    @Override
    public Boolean validateToken(String token, Long userId) {
        return null;
    }
}
