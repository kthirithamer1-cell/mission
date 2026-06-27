package com.projectmission.service;

import com.projectmission.dto.ParticipationDTO;
import com.projectmission.mapper.ParticipationMapper;
import com.projectmission.model.Club;
import com.projectmission.model.Competition;
import com.projectmission.model.Nageur;
import com.projectmission.model.Participation;
import com.projectmission.repository.*;
import com.projectmission.security.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParticipationService {

    @Autowired
    private ParticipationRepository repository;

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private NageurRepository nageurRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ParticipationMapper mapper;

    @Autowired
    private CurrentUserService currentUserService;

    public List<ParticipationDTO> getAll() {
        return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<ParticipationDTO> getByCompetition(Long competitionId) {
        return repository.findByCompetitionId(competitionId).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<ParticipationDTO> getByNageur(Long nageurId) {
        return repository.findByNageurId(nageurId).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<ParticipationDTO> getByClub(Long clubId) {
        return repository.findByClubId(clubId).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public long countByCompetition(Long competitionId) {
        return repository.countByCompetitionId(competitionId);
    }

    public boolean isRegistered(Long competitionId, Long nageurId) {
        return repository.existsByNageurIdAndCompetitionId(nageurId, competitionId);
    }

    public ParticipationDTO register(Long competitionId) {
        Long userId = currentUserService.getId();
        if (userId == null || !"NAGEUR".equals(currentUserService.getRole())) {
            throw new IllegalStateException("Seul un nageur peut s'inscrire à une compétition");
        }

        if (repository.existsByNageurIdAndCompetitionId(userId, competitionId)) {
            throw new IllegalStateException("Vous êtes déjà inscrit à cette compétition");
        }

        Competition competition = competitionRepository.findById(competitionId).orElse(null);
        if (competition == null) {
            throw new IllegalArgumentException("Compétition introuvable");
        }

        Nageur nageur = nageurRepository.findById(userId).orElse(null);
        if (nageur == null) {
            throw new IllegalArgumentException("Nageur introuvable");
        }

        Participation participation = new Participation();
        participation.setNageur(nageur);
        participation.setCompetition(competition);
        participation.setClub(nageur.getClub());
        participation.setDateInscription(LocalDate.now());
        participation.setStatut("INSCRIT");
        participation.setDateCreation(LocalDateTime.now());
        participation.setDateModification(LocalDateTime.now());

        Participation saved = repository.save(participation);
        return mapper.toDTO(saved);
    }

    public ParticipationDTO cancelRegistration(Long competitionId) {
        Long userId = currentUserService.getId();
        if (userId == null) {
            throw new IllegalStateException("Utilisateur non connecté");
        }

        Participation participation = repository.findActiveInscription(competitionId, userId)
                .orElseThrow(() -> new IllegalStateException("Inscription introuvable"));

        participation.setStatut("ANNULE");
        participation.setDateModification(LocalDateTime.now());
        Participation saved = repository.save(participation);
        return mapper.toDTO(saved);
    }

    public ParticipationDTO updateStatus(Long participationId, String statut) {
        Participation participation = repository.findById(participationId)
                .orElseThrow(() -> new IllegalArgumentException("Participation introuvable"));

        participation.setStatut(statut);
        participation.setDateModification(LocalDateTime.now());
        Participation saved = repository.save(participation);
        return mapper.toDTO(saved);
    }

    public void removeParticipant(Long participationId) {
        repository.deleteById(participationId);
    }
}
