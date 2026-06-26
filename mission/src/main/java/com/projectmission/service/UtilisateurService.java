package com.projectmission.service;

import com.projectmission.dto.UtilisateurDTO;
import com.projectmission.mapper.UtilisateurMapper;
import com.projectmission.model.Utilisateur;
import com.projectmission.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private UtilisateurMapper utilisateurMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public List<UtilisateurDTO> getAll() {
        return utilisateurRepository.findAll()
                .stream()
                .map(utilisateurMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UtilisateurDTO getById(Long id) {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(id);
        return utilisateur.map(utilisateurMapper::toDTO).orElse(null);
    }

    public UtilisateurDTO getByEmail(String email) {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findByEmail(email);
        return utilisateur.map(utilisateurMapper::toDTO).orElse(null);
    }

    public Utilisateur findEntityByEmail(String email) {
        return utilisateurRepository.findByEmail(email).orElse(null);
    }

    public UtilisateurDTO create(UtilisateurDTO dto) {
        Utilisateur utilisateur = utilisateurMapper.toEntity(dto);
        if (dto.getMotDePasse() != null) {
            utilisateur.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        }
        
        String token = UUID.randomUUID().toString();
        utilisateur.setVerificationToken(token);
        utilisateur.setEmailVerified(false);
        
        Utilisateur saved = utilisateurRepository.save(utilisateur);
        
        emailService.sendVerificationEmail(saved.getEmail(), token);
        
        return utilisateurMapper.toDTO(saved);
    }

    public UtilisateurDTO update(Long id, UtilisateurDTO dto) {
        Optional<Utilisateur> optionalUtilisateur = utilisateurRepository.findById(id);
        if (optionalUtilisateur.isPresent()) {
            Utilisateur utilisateur = optionalUtilisateur.get();
            utilisateur.setNom(dto.getNom());
            utilisateur.setPrenom(dto.getPrenom());
            utilisateur.setEmail(dto.getEmail());
            utilisateur.setRole(dto.getRole());
            Utilisateur updated = utilisateurRepository.save(utilisateur);
            return utilisateurMapper.toDTO(updated);
        }
        return null;
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        if (encodedPassword != null && encodedPassword.startsWith("$2y$")) {
            encodedPassword = "$2a$" + encodedPassword.substring(4);
        }
        return rawPassword != null && encodedPassword != null && passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public void delete(Long id) {
        utilisateurRepository.deleteById(id);
    }

    public boolean verifyEmail(String token) {
        Optional<Utilisateur> optionalUtilisateur = utilisateurRepository.findByVerificationToken(token);
        if (optionalUtilisateur.isPresent()) {
            Utilisateur utilisateur = optionalUtilisateur.get();
            if (!utilisateur.isEmailVerified()) {
                utilisateur.setEmailVerified(true);
                utilisateur.setVerificationToken(null);
                utilisateurRepository.save(utilisateur);
                return true;
            }
        }
        return false;
    }

    public boolean resendVerification(String email) {
        Optional<Utilisateur> optionalUtilisateur = utilisateurRepository.findByEmail(email);
        if (optionalUtilisateur.isPresent()) {
            Utilisateur utilisateur = optionalUtilisateur.get();
            if (!utilisateur.isEmailVerified()) {
                String token = utilisateur.getVerificationToken();
                if (token == null) {
                    token = UUID.randomUUID().toString();
                    utilisateur.setVerificationToken(token);
                    utilisateurRepository.save(utilisateur);
                }
                emailService.sendVerificationEmail(utilisateur.getEmail(), token);
                return true;
            }
        }
        return false;
    }

    public boolean requestPasswordReset(String email) {
        Optional<Utilisateur> optionalUtilisateur = utilisateurRepository.findByEmail(email);
        if (optionalUtilisateur.isPresent()) {
            Utilisateur utilisateur = optionalUtilisateur.get();
            String token = UUID.randomUUID().toString();
            utilisateur.setResetPasswordToken(token);
            utilisateur.setResetPasswordExpiry(java.time.LocalDateTime.now().plusMinutes(30));
            utilisateurRepository.save(utilisateur);
            emailService.sendResetPasswordEmail(email, token);
            return true;
        }
        return false;
    }

    public boolean resetPassword(String token, String newPassword) {
        Optional<Utilisateur> optionalUtilisateur = utilisateurRepository.findByResetPasswordToken(token);
        if (optionalUtilisateur.isPresent()) {
            Utilisateur utilisateur = optionalUtilisateur.get();
            if (utilisateur.getResetPasswordExpiry() != null
                    && utilisateur.getResetPasswordExpiry().isAfter(java.time.LocalDateTime.now())) {
                utilisateur.setMotDePasse(passwordEncoder.encode(newPassword));
                utilisateur.setResetPasswordToken(null);
                utilisateur.setResetPasswordExpiry(null);
                utilisateurRepository.save(utilisateur);
                return true;
            }
        }
        return false;
    }
}