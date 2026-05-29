package com.projectmission.controller;

import com.projectmission.dto.UtilisateurDTO;
import com.projectmission.exception.EmailNotVerifiedException;
import com.projectmission.service.UtilisateurService;
import com.projectmission.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UtilisateurDTO dto) {
        if (dto.getEmail() == null || dto.getMotDePasse() == null) {
            return ResponseEntity.status(400).body("Email and password are required");
        }
        try {
            UtilisateurDTO utilisateur = utilisateurService.authenticate(dto.getEmail(), dto.getMotDePasse());
            if (utilisateur != null) {
                String token = jwtUtil.generateToken(utilisateur.getEmail(), utilisateur.getRole());
                return ResponseEntity.ok(new LoginResponse(token, utilisateur));
            }
            return ResponseEntity.status(401).body("Invalid credentials");
        } catch (EmailNotVerifiedException ex) {
            return ResponseEntity.status(403).body(Map.of("message", ex.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (request.getUtilisateurDTO() == null) {
            return ResponseEntity.status(400).body("utilisateurDTO is required");
        }
        UtilisateurDTO dto = request.getUtilisateurDTO();
        String userType = request.getUserType();
        dto.setRole(userType);

        UtilisateurDTO existingUser = utilisateurService.getByEmail(dto.getEmail());
        if (existingUser != null) {
            return ResponseEntity.status(400).body("Email already exists");
        }

        try {
            UtilisateurDTO savedUser = utilisateurService.create(dto);
            return ResponseEntity.ok(new RegisterResponse(
                    "Compte créé. Vérifiez votre email pour activer votre compte.",
                    savedUser
            ));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body(
                    Map.of("message", "Impossible d'envoyer l'email de vérification. Vérifiez la configuration SMTP.")
            );
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        try {
            utilisateurService.verifyEmail(token);
            return ResponseEntity.ok(Map.of("message", "Email vérifié avec succès. Vous pouvez vous connecter."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(400).body(Map.of("message", ex.getMessage()));
        }
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.status(400).body(Map.of("message", "Email requis."));
        }
        try {
            utilisateurService.resendVerificationEmail(email.trim());
            return ResponseEntity.ok(Map.of("message", "Email de vérification renvoyé."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(400).body(Map.of("message", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(
                    Map.of("message", "Impossible d'envoyer l'email de vérification.")
            );
        }
    }

    public static class LoginResponse {
        private String token;
        private UtilisateurDTO utilisateur;

        public LoginResponse(String token, UtilisateurDTO utilisateur) {
            this.token = token;
            this.utilisateur = utilisateur;
        }

        public String getToken() { return token; }
        public UtilisateurDTO getUtilisateur() { return utilisateur; }
    }

    public static class RegisterResponse {
        private String message;
        private UtilisateurDTO utilisateur;

        public RegisterResponse(String message, UtilisateurDTO utilisateur) {
            this.message = message;
            this.utilisateur = utilisateur;
        }

        public String getMessage() { return message; }
        public UtilisateurDTO getUtilisateur() { return utilisateur; }
    }

    public static class RegisterRequest {
        private UtilisateurDTO utilisateurDTO;
        private String userType;

        public UtilisateurDTO getUtilisateurDTO() { return utilisateurDTO; }
        public void setUtilisateurDTO(UtilisateurDTO utilisateurDTO) { this.utilisateurDTO = utilisateurDTO; }
        public String getUserType() { return userType; }
        public void setUserType(String userType) { this.userType = userType; }
    }
}
