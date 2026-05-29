package com.projectmission.mapper;

import com.projectmission.dto.PiscineDTO;
import com.projectmission.model.Piscine;
import org.springframework.stereotype.Component;

@Component
public class PiscineMapper {
    public PiscineDTO toDTO(Piscine entity) {
        if (entity == null) return null;
        PiscineDTO dto = new PiscineDTO();
        dto.setId(entity.getId());
        dto.setNom(entity.getNom());
        dto.setAdresse(entity.getAdresse());
        dto.setVille(entity.getVille());
        dto.setNombreCouloirs(entity.getNombreCouloirs());
        dto.setLongueurMetres(entity.getLongueurMetres());
        dto.setActive(entity.getActive());
        return dto;
    }

    public Piscine toEntity(PiscineDTO dto) {
        if (dto == null) return null;
        Piscine entity = new Piscine();
        entity.setId(dto.getId());
        entity.setNom(dto.getNom());
        entity.setAdresse(dto.getAdresse());
        entity.setVille(dto.getVille());
        entity.setNombreCouloirs(dto.getNombreCouloirs());
        entity.setLongueurMetres(dto.getLongueurMetres());
        entity.setActive(dto.getActive() != null ? dto.getActive() : true);
        return entity;
    }
}
