package com.projectmission.service;

import com.projectmission.dto.EpreuveDTO;
import com.projectmission.mapper.EpreuveMapper;
import com.projectmission.model.Epreuve;
import com.projectmission.repository.EpreuveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EpreuveService {
    @Autowired
    private EpreuveRepository repository;

    @Autowired
    private EpreuveMapper mapper;

    public List<EpreuveDTO> getAll() { return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList()); }
    public EpreuveDTO getById(Long id) { return repository.findById(id).map(mapper::toDTO).orElse(null); }
    public EpreuveDTO create(EpreuveDTO dto) { Epreuve saved = repository.save(mapper.toEntity(dto)); return mapper.toDTO(saved); }
    public EpreuveDTO update(Long id, EpreuveDTO dto) {
        return repository.findById(id).map(existing -> {
            existing.setDistance(dto.getDistance());
            existing.setStyle(dto.getStyle());
            existing.setCategorie(dto.getCategorie());
            Epreuve mapped = mapper.toEntity(dto);
            if (mapped.getCompetition() != null) {
                existing.setCompetition(mapped.getCompetition());
            }
            return mapper.toDTO(repository.save(existing));
        }).orElse(null);
    }
    public void delete(Long id) { repository.deleteById(id); }
}
