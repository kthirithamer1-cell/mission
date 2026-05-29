package com.projectmission.mapper;

import com.projectmission.dto.SeanceDTO;
import com.projectmission.model.Club;
import com.projectmission.model.Entraineur;
import com.projectmission.model.Reservation;
import com.projectmission.model.Seance;
import com.projectmission.repository.ClubRepository;
import com.projectmission.repository.EntraineurRepository;
import com.projectmission.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class SeanceMapper {
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private EntraineurRepository entraineurRepository;

    public SeanceDTO toDTO(Seance entity) {
        if (entity == null) return null;
        SeanceDTO dto = new SeanceDTO();
        dto.setId(entity.getId());
        if (entity.getClub() != null) {
            dto.setClubId(entity.getClub().getId());
            dto.setClubNom(entity.getClub().getNom());
        }
        if (entity.getReservation() != null) dto.setReservationId(entity.getReservation().getId());
        if (entity.getEntraineur() != null) {
            dto.setEntraineurId(entity.getEntraineur().getId());
            dto.setEntraineurNom(entity.getEntraineur().getPrenom() + " " + entity.getEntraineur().getNom());
        }
        dto.setTitre(entity.getTitre());
        dto.setDate(entity.getDate() != null ? entity.getDate().toString() : null);
        dto.setHeureDebut(entity.getHeureDebut() != null ? entity.getHeureDebut().toString() : null);
        dto.setHeureFin(entity.getHeureFin() != null ? entity.getHeureFin().toString() : null);
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public Seance toEntity(SeanceDTO dto) {
        if (dto == null) return null;
        Seance entity = new Seance();
        entity.setId(dto.getId());
        if (dto.getClubId() != null) {
            Club club = clubRepository.findById(dto.getClubId()).orElse(null);
            entity.setClub(club);
        }
        if (dto.getReservationId() != null) {
            Reservation reservation = reservationRepository.findById(dto.getReservationId()).orElse(null);
            entity.setReservation(reservation);
        }
        if (dto.getEntraineurId() != null) {
            Entraineur entraineur = entraineurRepository.findById(dto.getEntraineurId()).orElse(null);
            entity.setEntraineur(entraineur);
        }
        entity.setTitre(dto.getTitre());
        // Defensive parsing: treat empty strings as null to avoid DateTimeParseException
        if (dto.getDate() != null && !dto.getDate().trim().isEmpty()) {
            entity.setDate(LocalDate.parse(dto.getDate()));
        }
        if (dto.getHeureDebut() != null && !dto.getHeureDebut().trim().isEmpty()) {
            entity.setHeureDebut(LocalTime.parse(dto.getHeureDebut()));
        }
        if (dto.getHeureFin() != null && !dto.getHeureFin().trim().isEmpty()) {
            entity.setHeureFin(LocalTime.parse(dto.getHeureFin()));
        }
        entity.setDescription(dto.getDescription());
        return entity;
    }
}
