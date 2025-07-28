package org.example.userauthservice_june2025morning.repos;

import org.example.userauthservice_june2025morning.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
}
