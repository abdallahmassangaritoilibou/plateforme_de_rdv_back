package com.mon_rdv.service;

import com.mon_rdv.model.Availability;
import com.mon_rdv.model.User;
import com.mon_rdv.repository.AvailabilityRepository;
import com.mon_rdv.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;
    private final AvailabilityRepository availRepo;

    public UserService(UserRepository repo, AvailabilityRepository availRepo) {

        this.repo = repo;
        this.availRepo = availRepo;
    }

    public List<User> getAll() {
        return repo.findAll();
    }

    public Optional<User> getById(Long id) {
        return repo.findById(id);
    }
    

    @Transactional
    public User create(User user) {
        User savedUser = repo.save(user);
        LocalDateTime now = LocalDateTime.now();
        Availability slotToday = new Availability(
                savedUser,
                now,
                now.plusMinutes(30));
        Availability slotIn3Days = new Availability(
                savedUser,
                now.plusDays(3),
                now.plusDays(3).plusMinutes(30));
        availRepo.save(slotToday);
        availRepo.save(slotIn3Days);

        return savedUser;
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