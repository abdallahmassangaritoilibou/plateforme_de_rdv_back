package com.mon_rdv.service;

import com.mon_rdv.model.User;
import com.mon_rdv.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;
    public UserService(UserRepository repo) { this.repo = repo; }

    public List<User> getAll() {
        return repo.findAll();
    }

    public Optional<User> getById(Long id) {
        return repo.findById(id);
    }

    public User create(User user) {
        return repo.save(user);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<User> findByRegistrationDateBetween(LocalDate start, LocalDate end) {
        return repo.findByRegistrationDateBetween(start, end);
    }
    
    /**
     * Vérifie si un email existe déjà en base
     */
    public boolean existsByEmail(String email) {
        return repo.existsByEmail(email);
    }

    /**
     * Sauvegarde un utilisateur (alias pour create)
     */
    public User save(User user) {
        return repo.save(user);
    }

    /**
     * Trouve un utilisateur par son email
     */
    public Optional<User> findByEmail(String email) {
        return repo.findByEmail(email);
    }

}