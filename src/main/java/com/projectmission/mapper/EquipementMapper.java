package com.projectmission.mapper;

import com.projectmission.dto.EquipementDTO;
import com.projectmission.model.Equipement;
import com.projectmission.model.Nageur;
import com.projectmission.repository.NageurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EquipementMapper {
    @Autowired
    private NageurRepository nageurRepository;

    public EquipementDTO toDTO(Equipement entity) {
        if (entity == null) return null;
        EquipementDTO dto = new EquipementDTO();
        dto.setId(entity.getId());
        dto.setType(entity.getType());
        dto.setNageurId(entity.getNageur() != null ? entity.getNageur().getId() : null);
        return dto;
    }

    public Equipement toEntity(EquipementDTO dto) {
        if (dto == null) return null;
        Equipement entity = new Equipement();
        entity.setId(dto.getId());
        entity.setType(dto.getType());
        if (dto.getNageurId() != null) {
            Nageur nageur = nageurRepository.findById(dto.getNageurId()).orElse(null);
            entity.setNageur(nageur);
        }
        return entity;
    }
}
