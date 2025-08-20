package org.example.userauthservice_june2025morning.services;

import org.example.userauthservice_june2025morning.models.User;
import org.example.userauthservice_june2025morning.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public User getUserDetailsBasedOnId(Long id) {
        User user = userRepo.findById(id).get();
        System.out.println(user.getName());
        return user;
    }
}
