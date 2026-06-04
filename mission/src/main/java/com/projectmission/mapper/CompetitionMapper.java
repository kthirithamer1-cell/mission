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
        dto.setSpecialite(entity.getSpecialite());
        dto.setEpreuve(entity.getEpreuve());
        return dto;
    }

    public Competition toEntity(CompetitionDTO dto) {
        if (dto == null) return null;
        Competition entity = new Competition();
        entity.setId(dto.getId());
        entity.setSpecialite(dto.getSpecialite());
        entity.setEpreuve(dto.getEpreuve());
        return entity;
    }
}