package com.projectmission.service;

import com.projectmission.dto.NageurDashboardDTO;
import com.projectmission.dto.SeanceDTO;
import com.projectmission.mapper.SeanceMapper;
import com.projectmission.model.Epreuve;
import com.projectmission.model.Nageur;
import com.projectmission.model.Presence;
import com.projectmission.model.Resultat;
import com.projectmission.model.Seance;
import com.projectmission.repository.NageurRepository;
import com.projectmission.repository.PresenceRepository;
import com.projectmission.repository.ResultatRepository;
import com.projectmission.repository.SeanceRepository;
import com.projectmission.security.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NageurDashboardService {

    @Autowired
    private NageurRepository nageurRepository;

    @Autowired
    private ResultatRepository resultatRepository;

    @Autowired
    private PresenceRepository presenceRepository;

    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private SeanceMapper seanceMapper;

    public NageurDashboardDTO getDashboardData() {
        String email = currentUserService.getEmail();
        Nageur swimmer = nageurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Nageur not found for email: " + email));

        NageurDashboardDTO dto = new NageurDashboardDTO();
        dto.setCategorie(swimmer.getCategorie());

        // 1. Results Count
        List<Resultat> results = swimmer.getId() != null ? 
                resultatRepository.findByNageur_Id(swimmer.getId()) : new ArrayList<>();
        dto.setResultatsCount(results.size());

        // 2. Presence Count (PRESENT statut)
        List<Presence> presences = swimmer.getId() != null ? 
                presenceRepository.findByNageur_Id(swimmer.getId()) : new ArrayList<>();
        long presentCount = presences.stream()
                .filter(p -> "PRESENT".equalsIgnoreCase(p.getStatut()))
                .count();
        dto.setPresencesCount(presentCount);

        // 3. Upcoming sessions for swimmer's club
        List<Seance> allClubSessions = swimmer.getClub() != null ? 
                seanceRepository.findByClub_Id(swimmer.getClub().getId()) : new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalTime nowTime = LocalTime.now();

        List<Seance> upcomingSessions = allClubSessions.stream()
                .filter(s -> s.getDate() != null && 
                        (s.getDate().isAfter(today) || 
                         (s.getDate().isEqual(today) && s.getHeureDebut() != null && s.getHeureDebut().isAfter(nowTime))))
                .sorted(Comparator.comparing(Seance::getDate).thenComparing(Seance::getHeureDebut))
                .collect(Collectors.toList());

        dto.setUpcomingSessions(upcomingSessions.stream()
                .limit(5)
                .map(seanceMapper::toDTO)
                .collect(Collectors.toList()));

        if (!upcomingSessions.isEmpty()) {
            dto.setNextSeance(seanceMapper.toDTO(upcomingSessions.get(0)));
        }

        // 4. Recent Results Details
        List<NageurDashboardDTO.ResultDetailDTO> recentResultDetails = results.stream()
                .sorted((r1, r2) -> {
                    // Sort by Resultat ID descending as fallback for newest
                    return r2.getId().compareTo(r1.getId());
                })
                .limit(5)
                .map(r -> {
                    Epreuve ep = r.getEpreuve();
                    String style = ep != null ? ep.getStyle() : "NL";
                    Integer dist = ep != null ? ep.getDistance() : 50;
                    String compNom = (ep != null && ep.getCompetition() != null) ? ep.getCompetition().getEpreuve() : "Compétition";
                    return new NageurDashboardDTO.ResultDetailDTO(
                            r.getId(),
                            r.getTemps(),
                            r.getClassement(),
                            style,
                            dist,
                            compNom
                    );
                })
                .collect(Collectors.toList());
        dto.setRecentResults(recentResultDetails);

        // 5. Personal Records (best time per distance + style)
        Map<String, Resultat> bestResults = new HashMap<>();
        for (Resultat r : results) {
            Epreuve ep = r.getEpreuve();
            if (ep == null || r.getTemps() == null) continue;
            String key = ep.getDistance() + "m " + ep.getStyle();
            if (!bestResults.containsKey(key) || compareTime(r.getTemps(), bestResults.get(key).getTemps()) < 0) {
                bestResults.put(key, r);
            }
        }

        List<NageurDashboardDTO.RecordDTO> personalRecords = bestResults.entrySet().stream()
                .map(entry -> {
                    Resultat r = entry.getValue();
                    Epreuve ep = r.getEpreuve();
                    String dateStr = (ep != null && ep.getCompetition() != null) ? ep.getCompetition().getEpreuve() : "Record";
                    return new NageurDashboardDTO.RecordDTO(entry.getKey(), r.getTemps(), dateStr);
                })
                .collect(Collectors.toList());
        dto.setPersonalRecords(personalRecords);

        // 6. Progression Data
        Map<String, List<Double>> progressionData = new HashMap<>();
        // Group all results by event type (style + distance)
        Map<String, List<Resultat>> grouped = results.stream()
                .filter(r -> r.getEpreuve() != null && r.getTemps() != null)
                .collect(Collectors.groupingBy(r -> r.getEpreuve().getDistance() + "m " + r.getEpreuve().getStyle()));

        for (Map.Entry<String, List<Resultat>> entry : grouped.entrySet()) {
            List<Double> times = entry.getValue().stream()
                    .sorted(Comparator.comparing(Resultat::getId)) // chronological order by insertion
                    .map(r -> parseSeconds(r.getTemps()))
                    .collect(Collectors.toList());
            progressionData.put(entry.getKey(), times);
        }
        dto.setProgressionData(progressionData);

        return dto;
    }

    private int compareTime(String a, String b) {
        return parseSeconds(a).compareTo(parseSeconds(b));
    }

    private Double parseSeconds(String t) {
        try {
            if (t.contains(":")) {
                String[] p = t.split(":");
                return Double.parseDouble(p[0]) * 60 + Double.parseDouble(p[1]);
            }
            return Double.parseDouble(t.replace(",", "."));
        } catch (Exception e) {
            return Double.MAX_VALUE;
        }
    }
}
