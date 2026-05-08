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

    public List<CompetitionDTO> getAll() { return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList()); }
    public CompetitionDTO getById(Long id) { return repository.findById(id).map(mapper::toDTO).orElse(null); }
    public CompetitionDTO create(CompetitionDTO dto) { Competition saved = repository.save(mapper.toEntity(dto)); return mapper.toDTO(saved); }
    public CompetitionDTO update(Long id, CompetitionDTO dto) {
        return repository.findById(id).map(existing -> {
            existing.setSpecialite(dto.getSpecialite());
            existing.setEpreuve(dto.getEpreuve());
            return mapper.toDTO(repository.save(existing));
        }).orElse(null);
    }
    public void delete(Long id) { repository.deleteById(id); }
}
