package org.example.userauthservice_june2025morning.oauth;

import org.example.userauthservice_june2025morning.models.User;
import org.example.userauthservice_june2025morning.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StorageUserDetailsService implements UserDetailsService  {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepo.findByEmail(email);

        if(userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return new StorageUserDetails(userOptional.get());
    }
}
