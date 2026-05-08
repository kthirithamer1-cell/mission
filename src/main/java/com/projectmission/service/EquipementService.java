package com.projectmission.service;

import com.projectmission.dto.EquipementDTO;
import com.projectmission.mapper.EquipementMapper;
import com.projectmission.model.Equipement;
import com.projectmission.repository.EquipementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipementService {
    @Autowired
    private EquipementRepository repository;

    @Autowired
    private EquipementMapper mapper;

    public List<EquipementDTO> getAll() { return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList()); }
    public EquipementDTO getById(Long id) { return repository.findById(id).map(mapper::toDTO).orElse(null); }
    public EquipementDTO create(EquipementDTO dto) { Equipement saved = repository.save(mapper.toEntity(dto)); return mapper.toDTO(saved); }
    public EquipementDTO update(Long id, EquipementDTO dto) {
        return repository.findById(id).map(existing -> {
            existing.setType(dto.getType());
            Equipement mapped = mapper.toEntity(dto);
            if (mapped.getNageur() != null) {
                existing.setNageur(mapped.getNageur());
            }
            return mapper.toDTO(repository.save(existing));
        }).orElse(null);
    }
    public void delete(Long id) { repository.deleteById(id); }
}
