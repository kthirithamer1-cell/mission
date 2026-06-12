package com.projectmission.service;

import com.projectmission.dto.CompetitionDTO;
import com.projectmission.mapper.CompetitionMapper;
import com.projectmission.model.Competition;
import com.projectmission.repository.CompetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompetitionService {
    @Autowired
    private CompetitionRepository repository;

    @Autowired
    private CompetitionMapper mapper;

    public List<CompetitionDTO> getAll() {
        return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public CompetitionDTO getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).orElse(null);
    }

    public List<CompetitionDTO> getByStatut(String statut) {
        return repository.findByStatut(statut).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<CompetitionDTO> getByType(String type) {
        return repository.findByType(type).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<CompetitionDTO> getBySaison(String saison) {
        return repository.findBySaison(saison).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<CompetitionDTO> getEnCours() {
        return repository.findByStatut("EN_COURS").stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<CompetitionDTO> getAVenir() {
        return repository.findByStatut("A_VENIR").stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public CompetitionDTO create(CompetitionDTO dto) {
        Competition entity = mapper.toEntity(dto);
        if (entity.getStatut() == null) entity.setStatut("A_VENIR");
        Competition saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    public CompetitionDTO update(Long id, CompetitionDTO dto) {
        return repository.findById(id).map(existing -> {
            existing.setNom(dto.getNom());
            existing.setLieu(dto.getLieu());
            existing.setDateDebut(dto.getDateDebut());
            existing.setDateFin(dto.getDateFin());
            existing.setType(dto.getType());
            existing.setStatut(dto.getStatut());
            existing.setNiveau(dto.getNiveau());
            existing.setOrganisateur(dto.getOrganisateur());
            existing.setDescription(dto.getDescription());
            existing.setSpecialite(dto.getSpecialite());
            existing.setEpreuve(dto.getEpreuve());
            existing.setSaison(dto.getSaison());
            return mapper.toDTO(repository.save(existing));
        }).orElse(null);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}