package com.projectmission.mapper;

import com.projectmission.dto.ParticipationDTO;
import com.projectmission.model.Club;
import com.projectmission.model.Competition;
import com.projectmission.model.Participation;
import com.projectmission.model.Utilisateur;
import com.projectmission.repository.ClubRepository;
import com.projectmission.repository.CompetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ParticipationMapper {

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private ClubRepository clubRepository;

    public ParticipationDTO toDTO(Participation entity) {
        if (entity == null) return null;
        ParticipationDTO dto = new ParticipationDTO();
        dto.setId(entity.getId());
        if (entity.getNageur() != null) {
            dto.setNageurId(entity.getNageur().getId());
            dto.setNageurNom(entity.getNageur().getNom());
            dto.setNageurPrenom(entity.getNageur().getPrenom());
            dto.setNageurEmail(entity.getNageur().getEmail());
        }
        if (entity.getCompetition() != null) {
            dto.setCompetitionId(entity.getCompetition().getId());
            dto.setCompetitionNom(entity.getCompetition().getNom());
        }
        if (entity.getClub() != null) {
            dto.setClubId(entity.getClub().getId());
            dto.setClubNom(entity.getClub().getNom());
        }
        dto.setDateInscription(entity.getDateInscription());
        dto.setStatut(entity.getStatut());
        if (entity.getDateCreation() != null) {
            dto.setDateCreation(entity.getDateCreation().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }
        if (entity.getDateModification() != null) {
            dto.setDateModification(entity.getDateModification().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }
        return dto;
    }

    public Participation toEntity(ParticipationDTO dto) {
        if (dto == null) return null;
        Participation entity = new Participation();
        entity.setId(dto.getId());
        entity.setDateInscription(dto.getDateInscription());
        entity.setStatut(dto.getStatut() != null ? dto.getStatut() : "INSCRIT");
        if (dto.getCompetitionId() != null) {
            Competition comp = competitionRepository.findById(dto.getCompetitionId()).orElse(null);
            entity.setCompetition(comp);
        }
        if (dto.getClubId() != null) {
            Club club = clubRepository.findById(dto.getClubId()).orElse(null);
            entity.setClub(club);
        }
        return entity;
    }
}
