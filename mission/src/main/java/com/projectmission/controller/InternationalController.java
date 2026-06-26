package com.projectmission.controller;

import com.projectmission.model.CompetitionInternationale;
import com.projectmission.model.NageurInternational;
import com.projectmission.model.RecordMondial;
import com.projectmission.repository.CompetitionInternationaleRepository;
import com.projectmission.repository.NageurInternationalRepository;
import com.projectmission.repository.RecordMondialRepository;
import com.projectmission.service.WorldAquaticsScrapingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/international")
public class InternationalController {

    @Autowired
    private RecordMondialRepository recordRepository;

    @Autowired
    private NageurInternationalRepository swimmerRepository;

    @Autowired
    private CompetitionInternationaleRepository competitionRepository;

    @Autowired
    private WorldAquaticsScrapingService scrapingService;

    @GetMapping("/records")
    public ResponseEntity<List<RecordMondial>> getRecords(
            @RequestParam(required = false) String bassin,
            @RequestParam(required = false) String sexe) {
        if (bassin != null && sexe != null) {
            return ResponseEntity.ok(recordRepository.findByBassinAndSexe(bassin, sexe));
        }
        return ResponseEntity.ok(recordRepository.findAll());
    }

    @GetMapping("/nageurs")
    public ResponseEntity<List<NageurInternational>> getSwimmers() {
        return ResponseEntity.ok(swimmerRepository.findAll());
    }

    @GetMapping("/competitions")
    public ResponseEntity<List<CompetitionInternationale>> getCompetitions() {
        return ResponseEntity.ok(competitionRepository.findAll());
    }

    @PostMapping("/scrape")
    public ResponseEntity<String> triggerScrape() {
        try {
            scrapingService.scrapeAndSave();
            return ResponseEntity.ok("Scraping completed successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error triggering scrape: " + e.getMessage());
        }
    }
}
