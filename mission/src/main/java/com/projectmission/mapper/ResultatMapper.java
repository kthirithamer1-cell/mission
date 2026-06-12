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
        dto.setPoints(entity.getPoints());
        dto.setRecord(entity.getRecord());
        dto.setDateCompetition(entity.getDateCompetition());
        
        if (entity.getNageur() != null) {
            dto.setNageurId(entity.getNageur().getId());
            dto.setNageurNom(entity.getNageur().getPrenom() + " " + entity.getNageur().getNom());
        }
        
        if (entity.getEpreuve() != null) {
            dto.setEpreuveId(entity.getEpreuve().getId());
            dto.setEpreuveNom(entity.getEpreuve().getDistance() + "m " + entity.getEpreuve().getStyle());
            if (entity.getEpreuve().getCompetition() != null) {
                dto.setCompetitionNom(entity.getEpreuve().getCompetition().getNom());
            }
        }
        return dto;
    }

    public Resultat toEntity(ResultatDTO dto) {
        if (dto == null) return null;
        Resultat entity = new Resultat();
        entity.setId(dto.getId());
        entity.setTemps(dto.getTemps());
        entity.setClassement(dto.getClassement());
        entity.setPoints(dto.getPoints());
        entity.setRecord(dto.getRecord());
        entity.setDateCompetition(dto.getDateCompetition());
        
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