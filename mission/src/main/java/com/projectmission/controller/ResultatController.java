package com.projectmission.controller;

import com.projectmission.dto.ResultatDTO;
import com.projectmission.dto.StatistiqueDTO;
import com.projectmission.service.ResultatService;
import com.projectmission.service.StatistiqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resultats")
public class ResultatController {
    
    @Autowired
    private ResultatService service;

    @Autowired
    private StatistiqueService statistiqueService;

    @GetMapping
    public ResponseEntity<List<ResultatDTO>> getAll() { 
        return ResponseEntity.ok(service.getAll()); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        ResultatDTO dto = service.getById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @GetMapping("/nageur/{nageurId}")
    public ResponseEntity<List<ResultatDTO>> getByNageur(@PathVariable("nageurId") Long nageurId) {
        return ResponseEntity.ok(service.getByNageur(nageurId));
    }

    @GetMapping("/competition/{competitionId}")
    public ResponseEntity<List<ResultatDTO>> getByCompetition(@PathVariable("competitionId") Long competitionId) {
        return ResponseEntity.ok(service.getByCompetition(competitionId));
    }

    @GetMapping("/statistiques")
    public ResponseEntity<StatistiqueDTO.ClubStats> getClubStats() {
        return ResponseEntity.ok(statistiqueService.getClubStats());
    }

    @GetMapping("/nageur/{nageurId}/statistiques")
    public ResponseEntity<?> getSwimmerStats(@PathVariable("nageurId") Long nageurId) {
        StatistiqueDTO.SwimmerStats stats = statistiqueService.getSwimmerStats(nageurId);
        return stats != null ? ResponseEntity.ok(stats) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ResultatDTO> create(@RequestBody ResultatDTO dto) { 
        return ResponseEntity.ok(service.create(dto)); 
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ResultatDTO dto) {
        ResultatDTO updated = service.update(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.ok("Resultat deleted successfully");
    }
}