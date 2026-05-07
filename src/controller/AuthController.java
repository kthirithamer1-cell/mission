package controller;

import dto.UtilisateurDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    // Injecter service utilisateur et JwtUtil

    @PostMapping("/login")
    public String login(@RequestBody UtilisateurDTO dto) {
        // Vérifier l'utilisateur, générer le token JWT avec le rôle
        // return jwtUtil.generateToken(dto.getEmail(), dto.getRole());
        return null;
    }

    @PostMapping("/register")
    public UtilisateurDTO register(@RequestBody UtilisateurDTO dto) {
        // Créer un nouvel utilisateur (hash du mot de passe, etc.)
        return null;
    }
}

