package com.projectmission.controller;

import com.projectmission.dto.UtilisateurDTO;
import com.projectmission.service.UtilisateurService;
import com.projectmission.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UtilisateurDTO dto) {
        UtilisateurDTO utilisateur = utilisateurService.getByEmail(dto.getEmail());
        if (utilisateur != null) {
            String token = jwtUtil.generateToken(utilisateur.getEmail(), utilisateur.getRole());
            return ResponseEntity.ok(new LoginResponse(token, utilisateur));
        }
        return ResponseEntity.status(401).body("Invalid credentials");
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

        UtilisateurDTO savedUser = utilisateurService.create(dto);
        return ResponseEntity.ok(savedUser);
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

    public static class RegisterRequest {
        private UtilisateurDTO utilisateurDTO;
        private String userType;

        public UtilisateurDTO getUtilisateurDTO() { return utilisateurDTO; }
        public void setUtilisateurDTO(UtilisateurDTO utilisateurDTO) { this.utilisateurDTO = utilisateurDTO; }
        public String getUserType() { return userType; }
        public void setUserType(String userType) { this.userType = userType; }
    }
}

