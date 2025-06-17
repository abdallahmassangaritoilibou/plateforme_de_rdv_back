package com.mon_rdv.repository;

import com.mon_rdv.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    // Trouve les disponibilités par userId qui ne sont pas réservées
    List<Availability> findByUserIdAndIsBookedFalse(Long userId);
    
    // Trouve les disponibilités par userId
    List<Availability> findByUserId(Long userId);
    
    // Trouve toutes les disponibilités non réservées
    List<Availability> findByIsBookedFalse();
}