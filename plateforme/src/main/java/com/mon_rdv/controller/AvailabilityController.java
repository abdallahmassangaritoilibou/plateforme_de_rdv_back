package com.mon_rdv.controller;

import com.mon_rdv.model.Availability;
import com.mon_rdv.model.User;
import com.mon_rdv.service.AvailabilityService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/availabilities")
@CrossOrigin(origins = "*")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    // GET /api/availabilities - Récupérer toutes les disponibilités
    @GetMapping
    public ResponseEntity<List<Availability>> getAllAvailabilities() {
        List<Availability> availabilities = availabilityService.getAllAvailabilities();
        return ResponseEntity.ok(availabilities);
    }

    // GET /api/availabilities/{id} - Récupérer une disponibilité par ID
    @GetMapping("/{id}")
    public ResponseEntity<Availability> getAvailabilityById(@PathVariable Long id) {
        try {
            Availability availability = availabilityService.getAvailabilityById(id);
            return ResponseEntity.ok(availability);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /api/availabilities - Créer une nouvelle disponibilité
    @PostMapping
    public ResponseEntity<Availability> createAvailability(@RequestBody Availability availability) {
        try {
            Availability createdAvailability = availabilityService.createAvailability(availability);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAvailability);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT /api/availabilities/{id} - Mettre à jour une disponibilité
    @PutMapping("/{id}")
    public ResponseEntity<Availability> updateAvailability(@PathVariable Long id,
            @RequestBody Availability availability) {
        try {
            Availability updatedAvailability = availabilityService.updateAvailability(id, availability);
            return ResponseEntity.ok(updatedAvailability);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/availabilities/{id} - Supprimer une disponibilité
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvailability(@PathVariable Long id) {
        try {
            availabilityService.deleteAvailability(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET /api/availabilities/user/{userId} - Récupérer les disponibilités d'un
    // utilisateur
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Availability>> getAvailabilitiesByUserId(@PathVariable Long userId) {
        List<Availability> availabilities = availabilityService.getAvailabilitiesByUserId(userId);
        return ResponseEntity.ok(availabilities);
    }

    // GET /api/availabilities/available - Récupérer les créneaux disponibles (non
    // réservés)
    @GetMapping("/available")
    public ResponseEntity<List<Availability>> getAvailableSlots() {
        List<Availability> availableSlots = availabilityService.getAvailableSlots();
        return ResponseEntity.ok(availableSlots);
    }

    // GET /api/availabilities/test - Injecter des disponibilités de test
    @PostMapping("/test")
    public ResponseEntity<String> seedAvailabilities() {
        try {
            // Injecter des disponibilités de test et les sauvegarde dans la base de données
            User user = new User();
            user.setId(1L); // Associer un utilisateur existant (id 1) pour les tests
            Availability availability1 = new Availability();
            availability1.setStartTime(LocalDateTime.parse("2025-10-01T09:00:00"));
            availability1.setEndTime(LocalDateTime.parse("2025-10-01T10:00:00"));
            availability1.setBooked(false);
            availability1.setUser(user); // Associer l'utilisateur à la disponibilité
            availabilityService.createAvailability(availability1);

            Availability availability2 = new Availability();
            availability2.setStartTime(LocalDateTime.parse("2025-10-01T11:00:00"));
            availability2.setEndTime(LocalDateTime.parse("2025-10-01T12:00:00"));
            availability2.setBooked(false);
            availability2.setUser(user); // Associer l'utilisateur à la disponibilité
            availabilityService.createAvailability(availability2);

            return ResponseEntity.status(HttpStatus.CREATED).body("Disponibilités de test injectées avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'injection : " + e.getMessage());
        }
    }

    // Cette méthode va gérer la réservation d'un créneau de disponibilité via une
    // requête HTTP PUT
    // L'URL attend un identifiant 'id' du créneau à réserver
    @PutMapping("/{id}/book")
    public ResponseEntity<String> bookAvailability(@PathVariable Long id) {
        Availability availability = availabilityService.getAvailabilityById(id);

        if (availability.isBooked()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ce créneau est déjà réservé.");
        }

        availability.setBooked(true);
        availabilityService.createAvailability(availability); // ou save()

        return ResponseEntity.ok("Créneau réservé avec succès !");
    }

}
