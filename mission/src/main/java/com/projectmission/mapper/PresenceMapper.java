package com.projectmission.mapper;

import com.projectmission.dto.PresenceDTO;
import com.projectmission.model.Nageur;
import com.projectmission.model.Presence;
import com.projectmission.model.Seance;
import com.projectmission.repository.NageurRepository;
import com.projectmission.repository.SeanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PresenceMapper {

    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private NageurRepository nageurRepository;

    public PresenceDTO toDTO(Presence entity) {
        if (entity == null) return null;
        PresenceDTO dto = new PresenceDTO();
        dto.setId(entity.getId());
        if (entity.getSeance() != null) dto.setSeanceId(entity.getSeance().getId());
        if (entity.getNageur() != null) {
            Nageur n = entity.getNageur();
            dto.setNageurId(n.getId());
            dto.setNageurNom(n.getNom());
            dto.setNageurPrenom(n.getPrenom());
            dto.setNageurCategorie(n.getCategorie());
        }
        dto.setStatut(entity.getStatut());
        return dto;
    }

    public Presence toEntity(PresenceDTO dto) {
        if (dto == null) return null;
        Presence entity = new Presence();
        entity.setId(dto.getId());
        if (dto.getSeanceId() != null) {
            Seance seance = seanceRepository.findById(dto.getSeanceId()).orElse(null);
            entity.setSeance(seance);
        }
        if (dto.getNageurId() != null) {
            Nageur nageur = nageurRepository.findById(dto.getNageurId()).orElse(null);
            entity.setNageur(nageur);
        }
        entity.setStatut(dto.getStatut() != null ? dto.getStatut() : "ABSENT");
        return entity;
    }
}
