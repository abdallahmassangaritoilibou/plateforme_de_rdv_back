package com.mon_rdv.service;

import java.time.LocalDateTime;
import java.util.List;

import java.util.Optional;
import org.springframework.stereotype.Service;

import com.mon_rdv.model.RendezVous;
import com.mon_rdv.model.User;
import com.mon_rdv.repository.RendezVousRepository;
import com.mon_rdv.repository.UserRepository;


@Service
public class RendezVousService {

    private final RendezVousRepository rendezVousRepository;
    private final UserRepository userRepository;

    public RendezVousService(RendezVousRepository rendezVousRepository, UserRepository userRepository) {
        this.rendezVousRepository = rendezVousRepository;
        this.userRepository = userRepository;
    }

    public List<RendezVous> getUserRdv(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        return rendezVousRepository.findByUserId(userId);
    }

    public RendezVous takeRdv(RendezVous rendezVous) {
        Optional<User> userOpt = userRepository.findById(rendezVous.getUser().getId());
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        rendezVous.setUser(userOpt.get());
        rendezVous.setDateCreation(LocalDateTime.now());
        rendezVous.setStatus("pending");
        return rendezVousRepository.save(rendezVous);
    }

    public RendezVous updateRdv(Long id, RendezVous rendezVous) {
        RendezVous existing = rendezVousRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("RendezVous not found"));

        if (!existing.getUser().getId().equals(rendezVous.getUser().getId())) {
            throw new SecurityException("Forbidden");
        }

        existing.setAvailability(rendezVous.getAvailability());
        existing.setStatus(rendezVous.getStatus());
        existing.setModificationDate(LocalDateTime.now());
        return rendezVousRepository.save(existing);
    }

    public void cancelRdv(Long id, Long userId) {
        RendezVous existing = rendezVousRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("RendezVous not found"));

        if (!existing.getUser().getId().equals(userId)) {
            throw new SecurityException("Forbidden");
        }

        rendezVousRepository.deleteById(id);
    }
}


