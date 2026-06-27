package com.projectmission.controller;

import com.projectmission.dto.ParticipationDTO;
import com.projectmission.service.ParticipationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/participations")
public class ParticipationController {
    @Autowired
    private ParticipationService service;

    @GetMapping("/competition/{competitionId}")
    public ResponseEntity<List<ParticipationDTO>> getByCompetition(@PathVariable("competitionId") Long competitionId) {
        return ResponseEntity.ok(service.getByCompetition(competitionId));
    }

    @GetMapping("/nageur/{nageurId}")
    public ResponseEntity<List<ParticipationDTO>> getByNageur(@PathVariable("nageurId") Long nageurId) {
        return ResponseEntity.ok(service.getByNageur(nageurId));
    }

    @GetMapping("/club/{clubId}")
    public ResponseEntity<List<ParticipationDTO>> getByClub(@PathVariable("clubId") Long clubId) {
        return ResponseEntity.ok(service.getByClub(clubId));
    }

    @GetMapping("/competition/{competitionId}/count")
    public ResponseEntity<Long> countByCompetition(@PathVariable("competitionId") Long competitionId) {
        return ResponseEntity.ok(service.countByCompetition(competitionId));
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> isRegistered(
            @RequestParam("competitionId") Long competitionId,
            @RequestParam("nageurId") Long nageurId) {
        return ResponseEntity.ok(service.isRegistered(competitionId, nageurId));
    }

    @PostMapping("/register/{competitionId}")
    public ResponseEntity<?> register(@PathVariable("competitionId") Long competitionId) {
        try {
            return ResponseEntity.ok(service.register(competitionId));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/cancel/{competitionId}")
    public ResponseEntity<?> cancel(@PathVariable("competitionId") Long competitionId) {
        try {
            return ResponseEntity.ok(service.cancelRegistration(competitionId));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{participationId}/statut")
    public ResponseEntity<?> updateStatus(@PathVariable("participationId") Long participationId, @RequestBody Map<String, String> body) {
        try {
            return ResponseEntity.ok(service.updateStatus(participationId, body.get("statut")));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{participationId}")
    public ResponseEntity<String> remove(@PathVariable("participationId") Long participationId) {
        service.removeParticipant(participationId);
        return ResponseEntity.ok("Participation supprimée");
    }
}
