package com.projectmission.mapper;

import com.projectmission.dto.LicenceDTO;
import com.projectmission.model.Licence;
import com.projectmission.model.Nageur;
import com.projectmission.repository.NageurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LicenceMapper {
    @Autowired
    private NageurRepository nageurRepository;

    public LicenceDTO toDTO(Licence entity) {
        if (entity == null) return null;
        LicenceDTO dto = new LicenceDTO();
        dto.setId(entity.getId());
        dto.setNumLicence(entity.getNumLicence());
        dto.setDateDebut(entity.getDateDebut());
        dto.setDateExpiration(entity.getDateExpiration());
        dto.setStatut(entity.getStatut());
        dto.setNageurId(entity.getNageur() != null ? entity.getNageur().getId() : null);
        return dto;
    }

    public Licence toEntity(LicenceDTO dto) {
        if (dto == null) return null;
        Licence entity = new Licence();
        entity.setId(dto.getId());
        entity.setNumLicence(dto.getNumLicence());
        entity.setDateDebut(dto.getDateDebut());
        entity.setDateExpiration(dto.getDateExpiration());
        entity.setStatut(dto.getStatut());
        if (dto.getNageurId() != null) {
            Nageur nageur = nageurRepository.findById(dto.getNageurId()).orElse(null);
            entity.setNageur(nageur);
        }
        return entity;
    }
}