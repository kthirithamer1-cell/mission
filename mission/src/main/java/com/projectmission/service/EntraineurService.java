package com.projectmission.service;

import com.projectmission.dto.EntraineurDTO;
import com.projectmission.mapper.EntraineurMapper;
import com.projectmission.model.Entraineur;
import com.projectmission.repository.EntraineurRepository;
import com.projectmission.security.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntraineurService {
    @Autowired
    private EntraineurRepository repository;
    @Autowired
    private EntraineurMapper mapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CurrentUserService currentUserService;

    public List<EntraineurDTO> getAllForCurrentUser() {
        if (currentUserService.isSuperAdmin()) {
            return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
        }
        Long clubId = currentUserService.getClubId();
        if (clubId == null) return List.of();
        return repository.findByClub_Id(clubId).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public EntraineurDTO getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).orElse(null);
    }

    public EntraineurDTO create(EntraineurDTO dto) {
        if (!currentUserService.isSuperAdmin()) {
            dto.setClubId(currentUserService.getClubId());
        }
        Entraineur entity = mapper.toEntity(dto);
        if (dto.getMotDePasse() != null) {
            entity.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        }
        return mapper.toDTO(repository.save(entity));
    }

    public EntraineurDTO update(Long id, EntraineurDTO dto) {
        return repository.findById(id).map(existing -> {
            existing.setNom(dto.getNom());
            existing.setPrenom(dto.getPrenom());
            existing.setEmail(dto.getEmail());
            existing.setGroupes(dto.getGroupes());
            Entraineur mapped = mapper.toEntity(dto);
            if (mapped.getClub() != null) existing.setClub(mapped.getClub());
            if (dto.getMotDePasse() != null && !dto.getMotDePasse().isBlank()) {
                existing.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
            }
            return mapper.toDTO(repository.save(existing));
        }).orElse(null);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public EntraineurDTO getMe() {
        String email = currentUserService.getEmail();
        return repository.findByEmail(email).map(mapper::toDTO).orElse(null);
    }

    public EntraineurDTO updateMe(EntraineurDTO dto) {
        String email = currentUserService.getEmail();
        return repository.findByEmail(email).map(existing -> {
            existing.setNom(dto.getNom());
            existing.setPrenom(dto.getPrenom());
            existing.setGroupes(dto.getGroupes());
            if (dto.getMotDePasse() != null && !dto.getMotDePasse().isBlank()) {
                existing.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
            }
            return mapper.toDTO(repository.save(existing));
        }).orElse(null);
    }

    public EntraineurDTO updatePhoto(String photoUrl) {
        String email = currentUserService.getEmail();
        return repository.findByEmail(email).map(existing -> {
            existing.setPhotoUrl(photoUrl);
            return mapper.toDTO(repository.save(existing));
        }).orElse(null);
    }
}