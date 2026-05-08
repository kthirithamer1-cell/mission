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
import java.util.stream.Collectors;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private UtilisateurMapper utilisateurMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        Utilisateur saved = utilisateurRepository.save(utilisateur);
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
        return rawPassword != null && encodedPassword != null && passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public void delete(Long id) {
        utilisateurRepository.deleteById(id);
    }
}
