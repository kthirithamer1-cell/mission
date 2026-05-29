package com.projectmission.mapper;

import com.projectmission.dto.ReservationDTO;
import com.projectmission.model.Club;
import com.projectmission.model.Piscine;
import com.projectmission.model.Reservation;
import com.projectmission.repository.ClubRepository;
import com.projectmission.repository.PiscineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class ReservationMapper {
    @Autowired
    private PiscineRepository piscineRepository;
    @Autowired
    private ClubRepository clubRepository;

    public ReservationDTO toDTO(Reservation entity) {
        if (entity == null) return null;
        ReservationDTO dto = new ReservationDTO();
        dto.setId(entity.getId());
        if (entity.getPiscine() != null) {
            dto.setPiscineId(entity.getPiscine().getId());
            dto.setPiscineNom(entity.getPiscine().getNom());
        }
        if (entity.getClub() != null) {
            dto.setClubId(entity.getClub().getId());
            dto.setClubNom(entity.getClub().getNom());
        }
        dto.setDate(entity.getDate() != null ? entity.getDate().toString() : null);
        dto.setHeureDebut(entity.getHeureDebut() != null ? entity.getHeureDebut().toString() : null);
        dto.setHeureFin(entity.getHeureFin() != null ? entity.getHeureFin().toString() : null);
        dto.setCouloirDebut(entity.getCouloirDebut());
        dto.setCouloirFin(entity.getCouloirFin());
        dto.setStatut(entity.getStatut());
        return dto;
    }

    public Reservation toEntity(ReservationDTO dto) {
        if (dto == null) return null;
        Reservation entity = new Reservation();
        entity.setId(dto.getId());
        if (dto.getPiscineId() != null) {
            Piscine piscine = piscineRepository.findById(dto.getPiscineId()).orElse(null);
            entity.setPiscine(piscine);
        }
        if (dto.getClubId() != null) {
            Club club = clubRepository.findById(dto.getClubId()).orElse(null);
            entity.setClub(club);
        }
        if (dto.getDate() != null) entity.setDate(LocalDate.parse(dto.getDate()));
        if (dto.getHeureDebut() != null) entity.setHeureDebut(LocalTime.parse(dto.getHeureDebut()));
        if (dto.getHeureFin() != null) entity.setHeureFin(LocalTime.parse(dto.getHeureFin()));
        entity.setCouloirDebut(dto.getCouloirDebut());
        entity.setCouloirFin(dto.getCouloirFin());
        entity.setStatut(dto.getStatut() != null ? dto.getStatut() : "EN_ATTENTE");
        return entity;
    }
}
