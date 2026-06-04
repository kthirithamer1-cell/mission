package com.projectmission.service;

import com.projectmission.dto.LicenceDTO;
import com.projectmission.mapper.LicenceMapper;
import com.projectmission.model.Licence;
import com.projectmission.repository.LicenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LicenceService {
    @Autowired
    private LicenceRepository repository;

    @Autowired
    private LicenceMapper mapper;

    public List<LicenceDTO> getAll() { return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList()); }
    public LicenceDTO getById(Long id) { return repository.findById(id).map(mapper::toDTO).orElse(null); }
    public LicenceDTO create(LicenceDTO dto) { Licence saved = repository.save(mapper.toEntity(dto)); return mapper.toDTO(saved); }
    public LicenceDTO update(Long id, LicenceDTO dto) {
        return repository.findById(id).map(existing -> {
            existing.setNumLicence(dto.getNumLicence());
            existing.setDateDebut(dto.getDateDebut());
            existing.setDateExpiration(dto.getDateExpiration());
            existing.setStatut(dto.getStatut());
            Licence mapped = mapper.toEntity(dto);
            if (mapped.getNageur() != null) {
                existing.setNageur(mapped.getNageur());
            }
            return mapper.toDTO(repository.save(existing));
        }).orElse(null);
    }
    public void delete(Long id) { repository.deleteById(id); }
}