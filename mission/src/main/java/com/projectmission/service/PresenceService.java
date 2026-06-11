package com.projectmission.service;

import com.projectmission.dto.PresenceDTO;
import com.projectmission.mapper.PresenceMapper;
import com.projectmission.model.Nageur;
import com.projectmission.model.Presence;
import com.projectmission.model.Seance;
import com.projectmission.repository.NageurRepository;
import com.projectmission.repository.PresenceRepository;
import com.projectmission.repository.SeanceRepository;
import com.projectmission.security.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PresenceService {

    @Autowired
    private PresenceRepository presenceRepository;

    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private NageurRepository nageurRepository;

    @Autowired
    private PresenceMapper mapper;

    @Autowired
    private CurrentUserService currentUserService;

    /**
     * Returns the attendance list for a given session.
     * If no records exist yet, auto-initialises one ABSENT row per swimmer
     * belonging to the same club as the session.
     */
    public List<PresenceDTO> getBySeance(Long seanceId) {
        List<Presence> existing = presenceRepository.findBySeance_Id(seanceId);
        if (!existing.isEmpty()) {
            return existing.stream().map(mapper::toDTO).collect(Collectors.toList());
        }
        // Auto-initialise: create ABSENT records for all club swimmers
        Seance seance = seanceRepository.findById(seanceId).orElse(null);
        if (seance == null || seance.getClub() == null) return List.of();
        Long clubId = seance.getClub().getId();
        List<Nageur> nageurs = nageurRepository.findByClub_Id(clubId);
        List<Presence> initialised = nageurs.stream().map(n -> {
            Presence p = new Presence();
            p.setSeance(seance);
            p.setNageur(n);
            p.setStatut("ABSENT");
            return p;
        }).collect(Collectors.toList());
        presenceRepository.saveAll(initialised);
        return presenceRepository.findBySeance_Id(seanceId)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Upsert: create or update the attendance status for one swimmer in one session.
     */
    public PresenceDTO mark(Long seanceId, Long nageurId, String statut) {
        Optional<Presence> existing = presenceRepository.findBySeance_IdAndNageur_Id(seanceId, nageurId);
        Presence presence = existing.orElseGet(() -> {
            Presence p = new Presence();
            p.setSeance(seanceRepository.findById(seanceId).orElseThrow());
            p.setNageur(nageurRepository.findById(nageurId).orElseThrow());
            return p;
        });
        presence.setStatut(statut);
        return mapper.toDTO(presenceRepository.save(presence));
    }

    /**
     * Bulk save: receives the full list for a session and saves all at once.
     */
    public List<PresenceDTO> saveAll(Long seanceId, List<PresenceDTO> dtos) {
        Seance seance = seanceRepository.findById(seanceId).orElseThrow();
        List<Presence> entities = dtos.stream().map(dto -> {
            Optional<Presence> existing = presenceRepository
                    .findBySeance_IdAndNageur_Id(seanceId, dto.getNageurId());
            Presence p = existing.orElseGet(Presence::new);
            p.setSeance(seance);
            p.setNageur(nageurRepository.findById(dto.getNageurId()).orElseThrow());
            p.setStatut(dto.getStatut() != null ? dto.getStatut() : "ABSENT");
            return p;
        }).collect(Collectors.toList());
        return presenceRepository.saveAll(entities)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }
}
