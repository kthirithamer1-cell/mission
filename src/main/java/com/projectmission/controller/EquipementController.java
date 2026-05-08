package com.projectmission.controller;

import com.projectmission.dto.EquipementDTO;
import com.projectmission.service.EquipementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipements")
public class EquipementController {
    @Autowired
    private EquipementService service;

    @GetMapping
    public ResponseEntity<List<EquipementDTO>> getAll() { return ResponseEntity.ok(service.getAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        EquipementDTO dto = service.getById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<EquipementDTO> create(@RequestBody EquipementDTO dto) { return ResponseEntity.ok(service.create(dto)); }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody EquipementDTO dto) {
        EquipementDTO updated = service.update(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Equipement deleted successfully");
    }
}
