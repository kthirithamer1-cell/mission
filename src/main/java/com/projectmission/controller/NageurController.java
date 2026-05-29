package com.projectmission.controller;

import com.projectmission.dto.NageurDTO;
import com.projectmission.service.NageurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nageurs")
public class NageurController {
    @Autowired
    private NageurService service;

    @GetMapping
    public ResponseEntity<List<NageurDTO>> getAll() { return ResponseEntity.ok(service.getAllForCurrentUser()); }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        NageurDTO dto = service.getById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<NageurDTO> create(@RequestBody NageurDTO dto) { return ResponseEntity.ok(service.create(dto)); }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody NageurDTO dto) {
        NageurDTO updated = service.update(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Nageur deleted successfully");
    }
}
