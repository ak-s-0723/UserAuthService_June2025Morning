package org.example.userauthservice_june2025morning.repos;

import org.example.userauthservice_june2025morning.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    User save(User user);

    Optional<User> findByEmail(String email);
}
