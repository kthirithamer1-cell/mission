package com.projectmission.controller;

import com.projectmission.dto.LicenceDTO;
import com.projectmission.service.LicenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/licences")
public class LicenceController {
    @Autowired
    private LicenceService service;

    @GetMapping
    public ResponseEntity<List<LicenceDTO>> getAll() { return ResponseEntity.ok(service.getAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        LicenceDTO dto = service.getById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<LicenceDTO> create(@RequestBody LicenceDTO dto) { return ResponseEntity.ok(service.create(dto)); }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody LicenceDTO dto) {
        LicenceDTO updated = service.update(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Licence deleted successfully");
    }
}
