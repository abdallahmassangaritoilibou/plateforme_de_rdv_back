package com.mon_rdv.controller;

import com.mon_rdv.dto.CreateRdvRequest;
import com.mon_rdv.dto.RDVResponseDTO;
import com.mon_rdv.model.Availability;
import com.mon_rdv.model.RendezVous;
import com.mon_rdv.model.User;
import com.mon_rdv.repository.AvailabilityRepository;
import com.mon_rdv.repository.RendezVousRepository;
import com.mon_rdv.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rendezvous")
@CrossOrigin(origins = "*")
public class RendezVousController {

    private final RendezVousRepository rendezVousRepository;
    private final UserRepository userRepository;
    private final AvailabilityRepository availabilityRepository;

    public RendezVousController(RendezVousRepository rendezVousRepository, UserRepository userRepository,
            AvailabilityRepository availabilityRepository) {
        this.rendezVousRepository = rendezVousRepository;
        this.userRepository = userRepository;
        this.availabilityRepository = availabilityRepository;
    }

    // GET - tous les RDV
    @GetMapping
    public ResponseEntity<List<RendezVous>> getAll() {
        return new ResponseEntity<>(rendezVousRepository.findAll(), HttpStatus.OK);
    }

    // GET - RDV par id
    @GetMapping("/{id}")
    public ResponseEntity<RendezVous> getById(@PathVariable Long id) {
        Optional<RendezVous> rdv = rendezVousRepository.findById(id);
        return rdv.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // GET - RDV par user id
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RDVResponseDTO>> getByUser(@PathVariable Long userId) {
        List<RendezVous> rdvs = rendezVousRepository.findByUserId(userId);
        List<RDVResponseDTO> dtos = rdvs.stream()
                .map(r -> new RDVResponseDTO(
                        r.getId(),
                        r.getAvailability().getId(),
                        r.getAvailability().getStartTime().toString(),
                        r.getAvailability().getEndTime().toString(),
                        r.getStatus()))
                .toList();
        return ResponseEntity.ok(dtos);

    }

    // POST - créer un RDV
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateRdvRequest request) {
        try {
            System.out.println(">> Création RDV reçue : " + request);

            // Vérifier que l'utilisateur existe
            Optional<User> userOpt = userRepository.findById(request.getUserId());
            if (userOpt.isEmpty()) {
                return new ResponseEntity<>("Utilisateur introuvable", HttpStatus.BAD_REQUEST);
            }

            // Vérifier que le créneau existe
            Optional<Availability> availOpt = availabilityRepository.findById(request.getAvailabilityId());
            if (availOpt.isEmpty()) {
                return new ResponseEntity<>("Créneau introuvable", HttpStatus.BAD_REQUEST);
            }

            // Vérifier si le créneau est déjà réservé
            if (availOpt.get().isBooked()) {
                return new ResponseEntity<>("Ce créneau est déjà réservé", HttpStatus.CONFLICT);
            }

            Availability avail = availOpt.get();
            avail.setBooked(true); // marquer le créneau comme réservé

            RendezVous rdv = new RendezVous();
            rdv.setUser(userOpt.get());
            rdv.setAvailability(avail);
            rdv.setStatus(request.getStatus());
            rdv.setDateCreation(LocalDateTime.now());
            rdv.setModificationDate(LocalDateTime.now());

            availabilityRepository.save(avail); // mettre à jour le créneau
            RendezVous saved = rendezVousRepository.save(rdv);

            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace(); // pour voir le message d'erreur dans la console
            return new ResponseEntity<>("Erreur serveur", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // POST - Injecter des rendez-vous de test
    @PostMapping("/test")
    public ResponseEntity<String> seedRendezVous() {
        try {
            // Vérifie qu'il y a au moins un utilisateur
            List<com.mon_rdv.model.User> users = userRepository.findAll();
            if (users.isEmpty()) {
                return new ResponseEntity<>("Aucun utilisateur trouvé. Créez d'abord un utilisateur.",
                        HttpStatus.BAD_REQUEST);
            }

            com.mon_rdv.model.User user = users.get(0); // prend le premier utilisateur trouvé pour les tests

            Availability slot1 = availabilityRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Availability 1 not found"));
            Availability slot2 = availabilityRepository.findById(2L)
                    .orElseThrow(() -> new RuntimeException("Availability 2 not found"));

            RendezVous rdv1 = new RendezVous();
            rdv1.setUser(user);
            rdv1.setAvailability(slot1);
            rdv1.setStatus("EN_ATTENTE");
            rdv1.setDateCreation(LocalDateTime.now());

            RendezVous rdv2 = new RendezVous();
            rdv2.setUser(user);
            rdv2.setAvailability(slot2);
            rdv2.setStatus("CONFIRME");
            rdv2.setDateCreation(LocalDateTime.now());

            rendezVousRepository.save(rdv1);
            rendezVousRepository.save(rdv2);

            return new ResponseEntity<>("Rendez-vous de test injectés avec succès !", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erreur lors de l'injection : " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // PUT - mettre à jour un RDV
    @PutMapping("/{id}")
    public ResponseEntity<RendezVous> update(@PathVariable Long id, @RequestBody RendezVous rendezVous) {
        Optional<RendezVous> existing = rendezVousRepository.findById(id);
        if (existing.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        RendezVous rdv = existing.get();

        // met à jour les champs modifiables
        rdv.setStatus(rendezVous.getStatus());

        // Si le RDV est annulé, libérer le créneau
        if ("ANNULE".equalsIgnoreCase(rendezVous.getStatus()) && rdv.getAvailability() != null) {
            Availability avail = rdv.getAvailability();
            avail.setBooked(false);
            availabilityRepository.save(avail);
        }

        rdv.setModificationDate(LocalDateTime.now());
        // On ne change pas l'utilisateur ni la date de création ici
        RendezVous saved = rendezVousRepository.save(rdv);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    // DELETE - supprimer un RDV
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        if (!rendezVousRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        rendezVousRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // GET - Test simple Hello World
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("Hello World - API rendez-vous fonctionne !", HttpStatus.OK);
    }

}
