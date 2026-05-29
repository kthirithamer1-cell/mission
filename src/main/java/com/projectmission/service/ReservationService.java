package com.projectmission.service;

import com.projectmission.dto.ReservationDTO;
import com.projectmission.mapper.ReservationMapper;
import com.projectmission.model.Piscine;
import com.projectmission.model.Reservation;
import com.projectmission.repository.PiscineRepository;
import com.projectmission.repository.ReservationRepository;
import com.projectmission.security.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository repository;
    @Autowired
    private PiscineRepository piscineRepository;
    @Autowired
    private ReservationMapper mapper;
    @Autowired
    private CurrentUserService currentUserService;

    public List<ReservationDTO> getAllForCurrentUser() {
        if (currentUserService.isSuperAdmin()) {
            return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
        }
        Long clubId = currentUserService.getClubId();
        if (clubId == null) return List.of();
        return repository.findByClub_Id(clubId).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public ReservationDTO getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).orElse(null);
    }

    public ReservationDTO create(ReservationDTO dto) {
        if (!currentUserService.isSuperAdmin()) {
            Long clubId = currentUserService.getClubId();
            if (clubId != null) dto.setClubId(clubId);
            dto.setStatut("EN_ATTENTE");
        }
        validateReservation(dto, null);
        Reservation saved = repository.save(mapper.toEntity(dto));
        return mapper.toDTO(saved);
    }

    public ReservationDTO updateStatus(Long id, String statut) {
        return repository.findById(id).map(existing -> {
            if (!currentUserService.isSuperAdmin() && !"ANNULE".equals(statut)) {
                throw new IllegalStateException("Seul l'administrateur plateforme peut confirmer une réservation");
            }
            existing.setStatut(statut);
            return mapper.toDTO(repository.save(existing));
        }).orElse(null);
    }

    public ReservationDTO update(Long id, ReservationDTO dto) {
        return repository.findById(id).map(existing -> {
            validateReservation(dto, id);
            Reservation mapped = mapper.toEntity(dto);
            existing.setPiscine(mapped.getPiscine());
            existing.setClub(mapped.getClub());
            existing.setDate(mapped.getDate());
            existing.setHeureDebut(mapped.getHeureDebut());
            existing.setHeureFin(mapped.getHeureFin());
            existing.setCouloirDebut(mapped.getCouloirDebut());
            existing.setCouloirFin(mapped.getCouloirFin());
            if (currentUserService.isSuperAdmin() && dto.getStatut() != null) {
                existing.setStatut(dto.getStatut());
            }
            return mapper.toDTO(repository.save(existing));
        }).orElse(null);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    private void validateReservation(ReservationDTO dto, Long excludeId) {
        Piscine piscine = piscineRepository.findById(dto.getPiscineId()).orElse(null);
        if (piscine == null) throw new IllegalArgumentException("Piscine introuvable");
        if (dto.getCouloirFin() < dto.getCouloirDebut()) {
            throw new IllegalArgumentException("Plage de couloirs invalide");
        }
        if (dto.getCouloirFin() > piscine.getNombreCouloirs()) {
            throw new IllegalArgumentException("Couloirs hors limites pour cette piscine");
        }
        LocalDate date = LocalDate.parse(dto.getDate());
        LocalTime start = LocalTime.parse(dto.getHeureDebut());
        LocalTime end = LocalTime.parse(dto.getHeureFin());
        if (!end.isAfter(start)) throw new IllegalArgumentException("Horaire invalide");

        List<Reservation> sameDay = repository.findActiveByPiscineAndDate(dto.getPiscineId(), date);
        for (Reservation other : sameDay) {
            if (excludeId != null && excludeId.equals(other.getId())) continue;
            if (!timesOverlap(start, end, other.getHeureDebut(), other.getHeureFin())) continue;
            if (lanesOverlap(dto.getCouloirDebut(), dto.getCouloirFin(), other.getCouloirDebut(), other.getCouloirFin())) {
                throw new IllegalStateException("Conflit de couloirs avec une autre réservation");
            }
        }
    }

    private boolean timesOverlap(LocalTime s1, LocalTime e1, LocalTime s2, LocalTime e2) {
        return s1.isBefore(e2) && s2.isBefore(e1);
    }

    private boolean lanesOverlap(int aStart, int aEnd, int bStart, int bEnd) {
        return aStart <= bEnd && bStart <= aEnd;
    }
}
