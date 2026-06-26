package com.projectmission.controller;

import com.projectmission.dto.EntraineurDTO;
import com.projectmission.service.EntraineurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entraineurs")
public class EntraineurController {
    @Autowired
    private EntraineurService service;

    @Autowired
    private com.projectmission.service.FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<List<EntraineurDTO>> getAll() {
        return ResponseEntity.ok(service.getAllForCurrentUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        EntraineurDTO dto = service.getById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<EntraineurDTO> create(@RequestBody EntraineurDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody EntraineurDTO dto) {
        EntraineurDTO updated = service.update(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe() {
        EntraineurDTO dto = service.getMe();
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateMe(@RequestBody EntraineurDTO dto) {
        EntraineurDTO updated = service.updateMe(dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.ok("Entraîneur supprimé");
    }

    @PostMapping("/me/photo")
    public ResponseEntity<?> uploadPhoto(@RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        String photoUrl = fileStorageService.storeFile(file);
        EntraineurDTO updated = service.updatePhoto(photoUrl);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
}