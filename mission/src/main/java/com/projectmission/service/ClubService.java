package com.projectmission.service;

import com.projectmission.dto.ClubDTO;
import com.projectmission.dto.EntraineurDTO;
import com.projectmission.mapper.ClubMapper;
import com.projectmission.mapper.EntraineurMapper;
import com.projectmission.model.Club;
import com.projectmission.repository.ClubRepository;
import com.projectmission.repository.EntraineurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClubService {
    @Autowired
    private ClubRepository repository;

    @Autowired
    private ClubMapper mapper;

    @Autowired
    private EntraineurRepository entraineurRepository;

    @Autowired
    private EntraineurMapper entraineurMapper;

    public List<ClubDTO> getAll() { return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList()); }
    public ClubDTO getById(Long id) { return repository.findById(id).map(mapper::toDTO).orElse(null); }
    public List<EntraineurDTO> getEntraineurs(Long clubId) {
        if (!repository.existsById(clubId)) return null;
        return entraineurRepository.findByClub_Id(clubId).stream().map(entraineurMapper::toDTO).collect(Collectors.toList());
    }
    public ClubDTO create(ClubDTO dto) { Club saved = repository.save(mapper.toEntity(dto)); return mapper.toDTO(saved); }
    public ClubDTO update(Long id, ClubDTO dto) {
        return repository.findById(id).map(existing -> {
            existing.setNom(dto.getNom());
            existing.setAdresse(dto.getAdresse());
            existing.setDateAffiliation(dto.getDateAffiliation());
            return mapper.toDTO(repository.save(existing));
        }).orElse(null);
    }
    public void delete(Long id) { repository.deleteById(id); }
}
