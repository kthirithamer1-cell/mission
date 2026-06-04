package com.projectmission.controller;

import com.projectmission.dto.UtilisateurDTO;
import com.projectmission.model.Utilisateur;
import com.projectmission.security.JwtUtil;
import com.projectmission.service.UtilisateurService;
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
        Utilisateur utilisateur = utilisateurService.findEntityByEmail(dto.getEmail());
        if (utilisateur != null && utilisateurService.checkPassword(dto.getMotDePasse(), utilisateur.getMotDePasse())) {
            if (!utilisateur.isEmailVerified()) {
                return ResponseEntity.status(403).body(new MessageResponse("Email not verified"));
            }
            String token = jwtUtil.generateToken(utilisateur.getEmail(), utilisateur.getRole());
            return ResponseEntity.ok(new LoginResponse(token, utilisateurService.getByEmail(utilisateur.getEmail())));
        }
        return ResponseEntity.status(401).body(new MessageResponse("Invalid credentials"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (request.getUtilisateurDTO() == null) {
            return ResponseEntity.status(400).body(new MessageResponse("utilisateurDTO is required"));
        }
        UtilisateurDTO dto = request.getUtilisateurDTO();
        String userType = request.getUserType();
        dto.setRole(userType != null ? userType.toUpperCase() : null);

        UtilisateurDTO existingUser = utilisateurService.getByEmail(dto.getEmail());
        if (existingUser != null) {
            return ResponseEntity.status(400).body(new MessageResponse("Email already exists"));
        }

        UtilisateurDTO savedUser = utilisateurService.create(dto);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        boolean verified = utilisateurService.verifyEmail(token);
        if (verified) {
            return ResponseEntity.ok(new MessageResponse("Email verified successfully"));
        }
        return ResponseEntity.status(400).body(new MessageResponse("Invalid or expired verification token"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        utilisateurService.requestPasswordReset(request.getEmail());
        // Always return 200 to prevent user enumeration
        return ResponseEntity.ok(new MessageResponse("If that email exists, a reset link has been sent."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        boolean success = utilisateurService.resetPassword(request.getToken(), request.getNewPassword());
        if (success) {
            return ResponseEntity.ok(new MessageResponse("Password has been reset successfully."));
        }
        return ResponseEntity.status(400).body(new MessageResponse("Invalid or expired reset token"));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@RequestBody ResendVerificationRequest request) {
        boolean success = utilisateurService.resendVerification(request.getEmail());
        if (success) {
            return ResponseEntity.ok(new MessageResponse("Verification email sent successfully."));
        }
        return ResponseEntity.status(400).body(new MessageResponse("Email not found or already verified."));
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

    public static class ForgotPasswordRequest {
        private String email;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    public static class ResetPasswordRequest {
        private String token;
        private String newPassword;

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }

    public static class ResendVerificationRequest {
        private String email;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    public static class MessageResponse {
        private String message;

        public MessageResponse(String message) { this.message = message; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}