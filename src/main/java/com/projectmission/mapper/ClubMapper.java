package com.projectmission.mapper;

import com.projectmission.dto.ClubDTO;
import com.projectmission.model.Club;
import org.springframework.stereotype.Component;

@Component
public class ClubMapper {
    public ClubDTO toDTO(Club entity) {
        if (entity == null) return null;
        ClubDTO dto = new ClubDTO();
        dto.setId(entity.getId());
        dto.setNom(entity.getNom());
        dto.setAdresse(entity.getAdresse());
        dto.setDateAffiliation(entity.getDateAffiliation());
        return dto;
    }

    public Club toEntity(ClubDTO dto) {
        if (dto == null) return null;
        Club entity = new Club();
        entity.setId(dto.getId());
        entity.setNom(dto.getNom());
        entity.setAdresse(dto.getAdresse());
        entity.setDateAffiliation(dto.getDateAffiliation());
        return entity;
    }
}
