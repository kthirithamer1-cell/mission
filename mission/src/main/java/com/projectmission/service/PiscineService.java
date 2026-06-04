package com.projectmission.service;

import com.projectmission.dto.PiscineDTO;
import com.projectmission.mapper.PiscineMapper;
import com.projectmission.model.Piscine;
import com.projectmission.repository.PiscineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PiscineService {
    @Autowired
    private PiscineRepository repository;
    @Autowired
    private PiscineMapper mapper;

    public List<PiscineDTO> getAll() {
        return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public PiscineDTO getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).orElse(null);
    }

    public PiscineDTO create(PiscineDTO dto) {
        Piscine saved = repository.save(mapper.toEntity(dto));
        return mapper.toDTO(saved);
    }

    public PiscineDTO update(Long id, PiscineDTO dto) {
        return repository.findById(id).map(existing -> {
            existing.setNom(dto.getNom());
            existing.setAdresse(dto.getAdresse());
            existing.setVille(dto.getVille());
            existing.setNombreCouloirs(dto.getNombreCouloirs());
            existing.setLongueurMetres(dto.getLongueurMetres());
            if (dto.getActive() != null) existing.setActive(dto.getActive());
            return mapper.toDTO(repository.save(existing));
        }).orElse(null);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}