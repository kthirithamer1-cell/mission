package com.projectmission.mapper;

import com.projectmission.dto.EntraineurDTO;
import com.projectmission.model.Club;
import com.projectmission.model.Entraineur;
import com.projectmission.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntraineurMapper {
    @Autowired
    private ClubRepository clubRepository;

    public EntraineurDTO toDTO(Entraineur entity) {
        if (entity == null) return null;
        EntraineurDTO dto = new EntraineurDTO();
        dto.setId(entity.getId());
        dto.setNom(entity.getNom());
        dto.setPrenom(entity.getPrenom());
        dto.setEmail(entity.getEmail());
        dto.setRole(entity.getRole());
        dto.setGroupes(entity.getGroupes());
        if (entity.getClub() != null) {
            dto.setClubId(entity.getClub().getId());
            dto.setClubNom(entity.getClub().getNom());
        }
        return dto;
    }

    public Entraineur toEntity(EntraineurDTO dto) {
        if (dto == null) return null;
        Entraineur entity = new Entraineur();
        entity.setId(dto.getId());
        entity.setNom(dto.getNom());
        entity.setPrenom(dto.getPrenom());
        entity.setEmail(dto.getEmail());
        entity.setMotDePasse(dto.getMotDePasse());
        entity.setRole(dto.getRole() != null ? dto.getRole() : "ENTRAINEUR");
        entity.setGroupes(dto.getGroupes());
        if (dto.getClubId() != null) {
            Club club = clubRepository.findById(dto.getClubId()).orElse(null);
            entity.setClub(club);
        }
        return entity;
    }
}