package com.mon_rdv.service;





import org.springframework.stereotype.Service;

import com.mon_rdv.model.Availability;
import com.mon_rdv.model.RendezVous;
import com.mon_rdv.model.User;
import com.mon_rdv.repository.AvailabilityRepository;
import com.mon_rdv.repository.RendezVousRepository;



@Service
public class RendezVousService {
  private final RendezVousRepository rdvRepo;
  private final AvailabilityRepository availRepo;

  public RendezVousService(RendezVousRepository rdvRepo,
                           AvailabilityRepository availRepo) {
    this.rdvRepo = rdvRepo;
    this.availRepo = availRepo;
  }

  public RendezVous createRdv(Long userId, Long availabilityId, String status) {
    // 1. Charger l’availability
    Availability avail = availRepo.findById(availabilityId)
        .orElseThrow(() -> new IllegalArgumentException("Disponibilité introuvable"));
    if (avail.isBooked())
      throw new IllegalStateException("Créneau déjà réservé");

    // 2. Charger l’utilisateur (ou injecter le user dans les paramètres)
    User user = new User();
    user.setId(userId);

    // 3. Marquer la dispo réservée
    avail.setBooked(true);
    availRepo.save(avail);

    // 4. Créer et sauvegarder le RDV
    RendezVous rdv = new RendezVous(user, avail, status);
    return rdvRepo.save(rdv);
  }

  public void cancelRdv(Long id, Long userId) {
	
	throw new UnsupportedOperationException("Unimplemented method 'cancelRdv'");
  }
}


    