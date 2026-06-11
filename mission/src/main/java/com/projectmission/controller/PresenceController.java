package com.projectmission.controller;

import com.projectmission.dto.PresenceDTO;
import com.projectmission.service.PresenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/presences")
public class PresenceController {

    @Autowired
    private PresenceService service;

    @GetMapping("/seance/{seanceId}")
    public ResponseEntity<List<PresenceDTO>> getBySeance(@PathVariable Long seanceId) {
        return ResponseEntity.ok(service.getBySeance(seanceId));
    }

    @PutMapping("/seance/{seanceId}/nageur/{nageurId}")
    public ResponseEntity<PresenceDTO> mark(
            @PathVariable Long seanceId,
            @PathVariable Long nageurId,
            @RequestParam String statut) {
        return ResponseEntity.ok(service.mark(seanceId, nageurId, statut));
    }

    @PostMapping("/seance/{seanceId}/bulk")
    public ResponseEntity<List<PresenceDTO>> saveAll(
            @PathVariable Long seanceId,
            @RequestBody List<PresenceDTO> dtos) {
        return ResponseEntity.ok(service.saveAll(seanceId, dtos));
    }
}
