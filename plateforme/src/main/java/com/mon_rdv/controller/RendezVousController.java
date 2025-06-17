package com.mon_rdv.controller;

import com.mon_rdv.model.RendezVous;
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
    

    public RendezVousController(RendezVousRepository rendezVousRepository, UserRepository userRepository) {
        this.rendezVousRepository = rendezVousRepository;
        this.userRepository = userRepository;
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
    public ResponseEntity<List<RendezVous>> getByUser(@PathVariable Long userId) {
        List<RendezVous> rdvs = rendezVousRepository.findByUserId(userId);
        return new ResponseEntity<>(rdvs, HttpStatus.OK);
    }

    // POST - créer un RDV
    @PostMapping
    public ResponseEntity<RendezVous> create(@RequestBody RendezVous rendezVous) {
        try {
            // Vérifier que l'utilisateur existe
            Optional<com.mon_rdv.model.User> user = userRepository.findById(rendezVous.getUser().getId());
            if (user.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            rendezVous.setUser(user.get());
            rendezVous.setDateCreation(LocalDateTime.now());
            RendezVous saved = rendezVousRepository.save(rendezVous);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
// POST - Injecter des rendez-vous de test
@PostMapping("/test")
public ResponseEntity<String> seedRendezVous() {
    try {
        // Vérifie qu'il y a au moins un utilisateur
        List<com.mon_rdv.model.User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return new ResponseEntity<>("Aucun utilisateur trouvé. Créez d'abord un utilisateur.", HttpStatus.BAD_REQUEST);
        }

        com.mon_rdv.model.User user = users.get(0); // prend le premier utilisateur trouvé pour les tests

        RendezVous rdv1 = new RendezVous();
        rdv1.setUser(user);
        rdv1.setAvailability(LocalDateTime.now().plusDays(1));
        rdv1.setStatus("EN_ATTENTE");
        rdv1.setDateCreation(LocalDateTime.now());

        RendezVous rdv2 = new RendezVous();
        rdv2.setUser(user);
        rdv2.setAvailability(LocalDateTime.now().plusDays(2));
        rdv2.setStatus("CONFIRME");
        rdv2.setDateCreation(LocalDateTime.now());

        rendezVousRepository.save(rdv1);
        rendezVousRepository.save(rdv2);

        return new ResponseEntity<>("Rendez-vous de test injectés avec succès !", HttpStatus.CREATED);
    } catch (Exception e) {
        return new ResponseEntity<>("Erreur lors de l'injection : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

// PUT - mettre à jour un RDV
@PutMapping("/{id}")
public ResponseEntity<RendezVous> update(@PathVariable Long id, @RequestBody RendezVous rendezVous) {
    Optional<RendezVous> existing = rendezVousRepository.findById(id);
    if (existing.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    RendezVous rdv = existing.get();

    // met à jour les champs modifiables
        rdv.setAvailability(rendezVous.getAvailability());
        rdv.setStatus(rendezVous.getStatus());
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

