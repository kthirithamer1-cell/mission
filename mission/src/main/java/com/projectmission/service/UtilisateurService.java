package com.projectmission.service;

import com.projectmission.dto.UtilisateurDTO;
import com.projectmission.exception.EmailNotVerifiedException;
import com.projectmission.mapper.UtilisateurMapper;
import com.projectmission.model.Utilisateur;
import com.projectmission.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
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

    @Value("${app.verification.token-expiry-hours:24}")
    private int verificationTokenExpiryHours;

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

    public UtilisateurDTO authenticate(String email, String rawPassword) {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findByEmail(email);
        if (utilisateur.isEmpty()) {
            return null;
        }
        Utilisateur user = utilisateur.get();
        if (user.getMotDePasse() == null || !passwordEncoder.matches(rawPassword, user.getMotDePasse())) {
            return null;
        }
        if (!user.isAccountVerified()) {
            throw new EmailNotVerifiedException("Email non vérifié. Consultez votre boîte mail.");
        }
        return utilisateurMapper.toDTO(user);
    }

    public UtilisateurDTO create(UtilisateurDTO dto) {
        Utilisateur utilisateur = utilisateurMapper.toEntity(dto);
        if (dto.getMotDePasse() != null) {
            utilisateur.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        }
        utilisateur.setEmailVerified(false);
        String token = UUID.randomUUID().toString();
        utilisateur.setVerificationToken(token);
        utilisateur.setVerificationTokenExpiry(
                LocalDateTime.now().plusHours(verificationTokenExpiryHours)
        );
        Utilisateur saved = utilisateurRepository.save(utilisateur);
        emailService.sendVerificationEmail(saved.getEmail(), token, saved.getPrenom());
        return utilisateurMapper.toDTO(saved);
    }

    public void verifyEmail(String token) {
        Utilisateur user = utilisateurRepository.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Lien de vérification invalide ou expiré."));

        if (user.getVerificationTokenExpiry() == null
                || user.getVerificationTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Lien de vérification invalide ou expiré.");
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationTokenExpiry(null);
        utilisateurRepository.save(user);
    }

    public void resendVerificationEmail(String email) {
        Utilisateur user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Aucun compte trouvé pour cet email."));

        if (user.isAccountVerified()) {
            throw new IllegalArgumentException("Cet email est déjà vérifié.");
        }

        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setVerificationTokenExpiry(
                LocalDateTime.now().plusHours(verificationTokenExpiryHours)
        );
        utilisateurRepository.save(user);
        emailService.sendVerificationEmail(user.getEmail(), token, user.getPrenom());
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

    public void delete(Long id) {
        utilisateurRepository.deleteById(id);
    }
}
