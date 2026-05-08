package com.projectmission.controller;

import com.projectmission.dto.ResultatDTO;
import com.projectmission.service.ResultatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resultats")
public class ResultatController {
    @Autowired
    private ResultatService service;

    @GetMapping
    public ResponseEntity<List<ResultatDTO>> getAll() { return ResponseEntity.ok(service.getAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        ResultatDTO dto = service.getById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ResultatDTO> create(@RequestBody ResultatDTO dto) { return ResponseEntity.ok(service.create(dto)); }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ResultatDTO dto) {
        ResultatDTO updated = service.update(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Resultat deleted successfully");
    }
}
