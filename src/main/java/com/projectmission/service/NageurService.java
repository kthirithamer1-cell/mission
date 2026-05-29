package com.projectmission.service;

import com.projectmission.dto.NageurDTO;
import com.projectmission.mapper.NageurMapper;
import com.projectmission.model.Nageur;
import com.projectmission.repository.NageurRepository;
import com.projectmission.security.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NageurService {
    @Autowired
    private NageurRepository repository;

    @Autowired
    private NageurMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CurrentUserService currentUserService;

    public List<NageurDTO> getAllForCurrentUser() {
        if (currentUserService.isSuperAdmin()) {
            return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
        }
        Long clubId = currentUserService.getClubId();
        if (clubId == null) return List.of();
        return repository.findByClub_Id(clubId).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<NageurDTO> getAll() { return getAllForCurrentUser(); }
    public NageurDTO getById(Long id) { return repository.findById(id).map(mapper::toDTO).orElse(null); }
    public NageurDTO create(NageurDTO dto) {
        if (!currentUserService.isSuperAdmin()) {
            dto.setClubId(currentUserService.getClubId());
        }
        Nageur nageur = mapper.toEntity(dto);
        if (dto.getMotDePasse() != null) {
            nageur.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        }
        Nageur saved = repository.save(nageur);
        return mapper.toDTO(saved);
    }
    public NageurDTO update(Long id, NageurDTO dto) {
        return repository.findById(id).map(existing -> {
            existing.setNom(dto.getNom());
            existing.setPrenom(dto.getPrenom());
            existing.setEmail(dto.getEmail());
            existing.setAge(dto.getAge());
            existing.setSexe(dto.getSexe());
            existing.setCategorie(dto.getCategorie());
            Nageur mapped = mapper.toEntity(dto);
            if (mapped.getClub() != null) {
                existing.setClub(mapped.getClub());
            }
            if (mapped.getLicence() != null) {
                existing.setLicence(mapped.getLicence());
            }
            return mapper.toDTO(repository.save(existing));
        }).orElse(null);
    }
    public void delete(Long id) { repository.deleteById(id); }
}
