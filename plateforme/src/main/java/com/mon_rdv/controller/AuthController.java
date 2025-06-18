package com.mon_rdv.controller;




import com.mon_rdv.dto.UserRegistrationDTO;
import com.mon_rdv.model.User;
import com.mon_rdv.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService,
                          PasswordEncoder passwordEncoder) {
        this.userService     = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * POST /api/auth/register
     * Inscription d'un nouvel utilisateur.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @Valid @RequestBody UserRegistrationDTO dto,
            BindingResult bindingResult
    ) {
        // Vérification des erreurs de validation
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getAllErrors()
                                          .get(0)
                                          .getDefaultMessage();
            return ResponseEntity
                    .badRequest()
                    .body(errorMsg);
        }

        // Vérifier que l'email n'existe pas déjà
        if (userService.existsByEmail(dto.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Cet email est déjà utilisé");
        }

        // Créer l'entité User et hashe le mot de passe
        User user = new User(
            dto.getForename(),
            dto.getSurname(),
            dto.getEmail(),
            passwordEncoder.encode(dto.getPassword())
        );

        // Sauvegarde
        userService.save(user);

        // Réponse 
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Inscription réussie");
    }
}

