package org.example.userauthservice_june2025morning.repos;

import org.example.userauthservice_june2025morning.models.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepo extends JpaRepository<UserSession,Long> {
    Optional<UserSession> findByTokenAndUser_Id(String token, Long userId);
}
