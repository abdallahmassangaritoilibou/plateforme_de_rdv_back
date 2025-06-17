package com.mon_rdv.service;

import com.mon_rdv.model.Availability;
import java.util.List;

public interface AvailabilityService {
    List<Availability> getAllAvailabilities();
    Availability getAvailabilityById(Long id);
    Availability createAvailability(Availability availability);
    Availability updateAvailability(Long id, Availability availability);
    void deleteAvailability(Long id);
    
    List<Availability> getAvailabilitiesByUserId(Long userId);
    List<Availability> getAvailableSlots();
   
}
