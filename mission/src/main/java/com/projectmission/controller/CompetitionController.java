package com.projectmission.controller;

import com.projectmission.dto.CompetitionDTO;
import com.projectmission.service.CompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/competitions")
public class CompetitionController {
    @Autowired
    private CompetitionService service;

    @GetMapping
    public ResponseEntity<List<CompetitionDTO>> getAll(
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String saison) {
        if (statut != null) return ResponseEntity.ok(service.getByStatut(statut));
        if (type != null) return ResponseEntity.ok(service.getByType(type));
        if (saison != null) return ResponseEntity.ok(service.getBySaison(saison));
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/en-cours")
    public ResponseEntity<List<CompetitionDTO>> getEnCours() {
        return ResponseEntity.ok(service.getEnCours());
    }

    @GetMapping("/a-venir")
    public ResponseEntity<List<CompetitionDTO>> getAVenir() {
        return ResponseEntity.ok(service.getAVenir());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        CompetitionDTO dto = service.getById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<CompetitionDTO> create(@RequestBody CompetitionDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody CompetitionDTO dto) {
        CompetitionDTO updated = service.update(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.ok("Competition deleted successfully");
    }
}