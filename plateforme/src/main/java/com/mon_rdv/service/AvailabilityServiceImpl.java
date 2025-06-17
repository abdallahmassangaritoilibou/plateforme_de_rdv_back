package com.mon_rdv.service;

import com.mon_rdv.model.Availability;
import com.mon_rdv.repository.AvailabilityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {

    
    private final AvailabilityRepository availabilityRepository;

    public AvailabilityServiceImpl(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }
    public List<Availability> getAllAvailabilities() {
        return availabilityRepository.findAll();
    }

    
    public Availability getAvailabilityById(Long id) {
        return availabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Availability not found with id: " + id));
    }

    
    public Availability createAvailability(Availability availability) {
        return availabilityRepository.save(availability);
    }

   
    public Availability updateAvailability(Long id, Availability newAvailability) {
        Availability existing = getAvailabilityById(id);
        existing.setStartTime(newAvailability.getStartTime());
        existing.setEndTime(newAvailability.getEndTime());
        existing.setBooked(newAvailability.isBooked());
        return availabilityRepository.save(existing);
    }

    
    public void deleteAvailability(Long id) {
        availabilityRepository.deleteById(id);
    }

    
    public List<Availability> getAvailabilitiesByUserId(Long userId) {
        return availabilityRepository.findByUserId(userId);
    }

    
    public List<Availability> getAvailableSlots() {
        return availabilityRepository.findByIsBookedFalse();
    }
}