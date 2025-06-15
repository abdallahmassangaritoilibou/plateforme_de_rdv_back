package com.mon_rdv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.mon_rdv.model.User;
import java.util.Optional;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    
    // Vérifier si un email existe déjà
    boolean existsByEmail(String email);
    // Rechercher un utilisateur par email
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);
    
    Optional<User> findByForenameAndSurname(String forename, String surname);

    List<User> findByRegistrationDateBetween(LocalDate start, LocalDate end);
}
