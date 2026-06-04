package com.projectmission.service;

import com.projectmission.dto.SeanceDTO;
import com.projectmission.mapper.SeanceMapper;
import com.projectmission.model.Seance;
import com.projectmission.repository.SeanceRepository;
import com.projectmission.security.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeanceService {
    @Autowired
    private SeanceRepository repository;
    @Autowired
    private SeanceMapper mapper;
    @Autowired
    private CurrentUserService currentUserService;

    public List<SeanceDTO> getAllForCurrentUser() {
        if (currentUserService.isSuperAdmin()) {
            return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
        }
        Long clubId = currentUserService.getClubId();
        if (clubId == null) return List.of();
        return repository.findByClub_Id(clubId).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public SeanceDTO getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).orElse(null);
    }

    public SeanceDTO create(SeanceDTO dto) {
        if (!currentUserService.isSuperAdmin()) {
            dto.setClubId(currentUserService.getClubId());
        }
        Seance saved = repository.save(mapper.toEntity(dto));
        return mapper.toDTO(saved);
    }

    public SeanceDTO update(Long id, SeanceDTO dto) {
        return repository.findById(id).map(existing -> {
            Seance mapped = mapper.toEntity(dto);
            existing.setTitre(mapped.getTitre());
            existing.setDate(mapped.getDate());
            existing.setHeureDebut(mapped.getHeureDebut());
            existing.setHeureFin(mapped.getHeureFin());
            existing.setDescription(mapped.getDescription());
            existing.setEntraineur(mapped.getEntraineur());
            existing.setReservation(mapped.getReservation());
            if (currentUserService.isSuperAdmin() && mapped.getClub() != null) {
                existing.setClub(mapped.getClub());
            }
            return mapper.toDTO(repository.save(existing));
        }).orElse(null);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}