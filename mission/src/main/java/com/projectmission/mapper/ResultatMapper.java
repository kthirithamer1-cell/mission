package com.projectmission.mapper;

import com.projectmission.dto.ResultatDTO;
import com.projectmission.model.Epreuve;
import com.projectmission.model.Nageur;
import com.projectmission.model.Resultat;
import com.projectmission.repository.EpreuveRepository;
import com.projectmission.repository.NageurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResultatMapper {
    @Autowired
    private NageurRepository nageurRepository;

    @Autowired
    private EpreuveRepository epreuveRepository;

    public ResultatDTO toDTO(Resultat entity) {
        if (entity == null) return null;
        ResultatDTO dto = new ResultatDTO();
        dto.setId(entity.getId());
        dto.setTemps(entity.getTemps());
        dto.setClassement(entity.getClassement());
        dto.setNageurId(entity.getNageur() != null ? entity.getNageur().getId() : null);
        dto.setEpreuveId(entity.getEpreuve() != null ? entity.getEpreuve().getId() : null);
        return dto;
    }

    public Resultat toEntity(ResultatDTO dto) {
        if (dto == null) return null;
        Resultat entity = new Resultat();
        entity.setId(dto.getId());
        entity.setTemps(dto.getTemps());
        entity.setClassement(dto.getClassement());
        if (dto.getNageurId() != null) {
            Nageur nageur = nageurRepository.findById(dto.getNageurId()).orElse(null);
            entity.setNageur(nageur);
        }
        if (dto.getEpreuveId() != null) {
            Epreuve epreuve = epreuveRepository.findById(dto.getEpreuveId()).orElse(null);
            entity.setEpreuve(epreuve);
        }
        return entity;
    }
}