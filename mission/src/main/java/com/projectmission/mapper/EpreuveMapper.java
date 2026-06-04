package com.projectmission.mapper;

import com.projectmission.dto.EpreuveDTO;
import com.projectmission.model.Competition;
import com.projectmission.model.Epreuve;
import com.projectmission.repository.CompetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EpreuveMapper {
    @Autowired
    private CompetitionRepository competitionRepository;

    public EpreuveDTO toDTO(Epreuve entity) {
        if (entity == null) return null;
        EpreuveDTO dto = new EpreuveDTO();
        dto.setId(entity.getId());
        dto.setDistance(entity.getDistance());
        dto.setStyle(entity.getStyle());
        dto.setCategorie(entity.getCategorie());
        dto.setCompetitionId(entity.getCompetition() != null ? entity.getCompetition().getId() : null);
        return dto;
    }

    public Epreuve toEntity(EpreuveDTO dto) {
        if (dto == null) return null;
        Epreuve entity = new Epreuve();
        entity.setId(dto.getId());
        entity.setDistance(dto.getDistance());
        entity.setStyle(dto.getStyle());
        entity.setCategorie(dto.getCategorie());
        if (dto.getCompetitionId() != null) {
            Competition competition = competitionRepository.findById(dto.getCompetitionId()).orElse(null);
            entity.setCompetition(competition);
        }
        return entity;
    }
}