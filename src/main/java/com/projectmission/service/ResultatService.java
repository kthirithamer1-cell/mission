package com.projectmission.service;

import com.projectmission.dto.ResultatDTO;
import com.projectmission.mapper.ResultatMapper;
import com.projectmission.model.Resultat;
import com.projectmission.repository.ResultatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResultatService {
    @Autowired
    private ResultatRepository repository;

    @Autowired
    private ResultatMapper mapper;

    public List<ResultatDTO> getAll() { return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList()); }
    public ResultatDTO getById(Long id) { return repository.findById(id).map(mapper::toDTO).orElse(null); }
    public ResultatDTO create(ResultatDTO dto) { Resultat saved = repository.save(mapper.toEntity(dto)); return mapper.toDTO(saved); }
    public ResultatDTO update(Long id, ResultatDTO dto) {
        return repository.findById(id).map(existing -> {
            existing.setTemps(dto.getTemps());
            existing.setClassement(dto.getClassement());
            Resultat mapped = mapper.toEntity(dto);
            if (mapped.getNageur() != null) {
                existing.setNageur(mapped.getNageur());
            }
            if (mapped.getEpreuve() != null) {
                existing.setEpreuve(mapped.getEpreuve());
            }
            return mapper.toDTO(repository.save(existing));
        }).orElse(null);
    }
    public void delete(Long id) { repository.deleteById(id); }
}
