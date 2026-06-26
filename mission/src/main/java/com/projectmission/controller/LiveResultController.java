package com.projectmission.controller;

import com.projectmission.dto.LiveResultEvent;
import com.projectmission.dto.ResultatDTO;
import com.projectmission.model.Epreuve;
import com.projectmission.repository.EpreuveRepository;
import com.projectmission.service.ResultatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resultats/live")
public class LiveResultController {

    @Autowired
    private ResultatService resultatService;

    @Autowired
    private EpreuveRepository epreuveRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping
    public ResponseEntity<ResultatDTO> submitLiveResult(@RequestBody ResultatDTO dto) {
        // 1. Save result to database
        ResultatDTO savedDto = resultatService.create(dto);

        // 2. Fetch epreuve to get competition ID
        Long competitionId = null;
        if (savedDto.getEpreuveId() != null) {
            Epreuve epreuve = epreuveRepository.findById(savedDto.getEpreuveId()).orElse(null);
            if (epreuve != null && epreuve.getCompetition() != null) {
                competitionId = epreuve.getCompetition().getId();
            }
        }

        // 3. Construct live event
        LiveResultEvent event = new LiveResultEvent(
                savedDto.getNageurNom(),
                savedDto.getEpreuveNom(),
                savedDto.getTemps(),
                savedDto.getClassement(),
                savedDto.getPoints(),
                savedDto.getRecord(),
                competitionId
        );

        // 4. Broadcast to topic
        if (competitionId != null) {
            String destination = "/topic/competition/" + competitionId;
            messagingTemplate.convertAndSend(destination, event);
            
            // Also broadcast to a general live stream topic
            messagingTemplate.convertAndSend("/topic/live", event);
        }

        return ResponseEntity.ok(savedDto);
    }
}
