package com.projectmission.mapper;

import com.projectmission.dto.NageurDTO;
import com.projectmission.model.Club;
import com.projectmission.model.Licence;
import com.projectmission.model.Nageur;
import com.projectmission.repository.ClubRepository;
import com.projectmission.repository.LicenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NageurMapper {
    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private LicenceRepository licenceRepository;

    public NageurDTO toDTO(Nageur entity) {
        if (entity == null) return null;
        NageurDTO dto = new NageurDTO();
        dto.setId(entity.getId());
        dto.setNom(entity.getNom());
        dto.setPrenom(entity.getPrenom());
        dto.setEmail(entity.getEmail());
        dto.setRole(entity.getRole());
        dto.setAge(entity.getAge());
        dto.setSexe(entity.getSexe());
        dto.setCategorie(entity.getCategorie());
        dto.setClubId(entity.getClub() != null ? entity.getClub().getId() : null);
        dto.setLicenceId(entity.getLicence() != null ? entity.getLicence().getId() : null);
        return dto;
    }

    public Nageur toEntity(NageurDTO dto) {
        if (dto == null) return null;
        Nageur entity = new Nageur();
        entity.setId(dto.getId());
        entity.setNom(dto.getNom());
        entity.setPrenom(dto.getPrenom());
        entity.setEmail(dto.getEmail());
        entity.setMotDePasse(dto.getMotDePasse());
        entity.setRole(dto.getRole() != null ? dto.getRole() : "NAGEUR");
        entity.setAge(dto.getAge());
        entity.setSexe(dto.getSexe());
        entity.setCategorie(dto.getCategorie());
        if (dto.getClubId() != null) {
            Club club = clubRepository.findById(dto.getClubId()).orElse(null);
            entity.setClub(club);
        }
        if (dto.getLicenceId() != null) {
            Licence licence = licenceRepository.findById(dto.getLicenceId()).orElse(null);
            entity.setLicence(licence);
        }
        return entity;
    }
}
