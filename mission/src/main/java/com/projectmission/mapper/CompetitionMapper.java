package com.projectmission.mapper;

import com.projectmission.dto.CompetitionDTO;
import com.projectmission.model.Competition;
import org.springframework.stereotype.Component;

@Component
public class CompetitionMapper {
    public CompetitionDTO toDTO(Competition entity) {
        if (entity == null) return null;
        CompetitionDTO dto = new CompetitionDTO();
        dto.setId(entity.getId());
        dto.setNom(entity.getNom());
        dto.setLieu(entity.getLieu());
        dto.setDateDebut(entity.getDateDebut());
        dto.setDateFin(entity.getDateFin());
        dto.setType(entity.getType());
        dto.setStatut(entity.getStatut());
        dto.setNiveau(entity.getNiveau());
        dto.setOrganisateur(entity.getOrganisateur());
        dto.setDescription(entity.getDescription());
        dto.setSpecialite(entity.getSpecialite());
        dto.setEpreuve(entity.getEpreuve());
        dto.setSaison(entity.getSaison());
        dto.setNombreEpreuves(entity.getEpreuves() != null ? entity.getEpreuves().size() : 0);
        return dto;
    }

    public Competition toEntity(CompetitionDTO dto) {
        if (dto == null) return null;
        Competition entity = new Competition();
        entity.setId(dto.getId());
        entity.setNom(dto.getNom());
        entity.setLieu(dto.getLieu());
        entity.setDateDebut(dto.getDateDebut());
        entity.setDateFin(dto.getDateFin());
        entity.setType(dto.getType());
        entity.setStatut(dto.getStatut());
        entity.setNiveau(dto.getNiveau());
        entity.setOrganisateur(dto.getOrganisateur());
        entity.setDescription(dto.getDescription());
        entity.setSpecialite(dto.getSpecialite());
        entity.setEpreuve(dto.getEpreuve());
        entity.setSaison(dto.getSaison());
        return entity;
    }
}