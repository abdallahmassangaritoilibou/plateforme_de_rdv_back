package com.mon_rdv.repository;

import com.mon_rdv.model.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {
    List<RendezVous> findByUserId(Long userId);
}
