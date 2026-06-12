package com.projectmission.service;

import com.projectmission.dto.ResultatDTO;
import com.projectmission.dto.StatistiqueDTO;
import com.projectmission.mapper.ResultatMapper;
import com.projectmission.model.Competition;
import com.projectmission.model.Nageur;
import com.projectmission.model.Resultat;
import com.projectmission.repository.CompetitionRepository;
import com.projectmission.repository.NageurRepository;
import com.projectmission.repository.ResultatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatistiqueService {

    @Autowired
    private ResultatRepository resultatRepository;

    @Autowired
    private NageurRepository nageurRepository;

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private ResultatMapper resultatMapper;

    public StatistiqueDTO.SwimmerStats getSwimmerStats(Long nageurId) {
        Nageur nageur = nageurRepository.findById(nageurId).orElse(null);
        if (nageur == null) {
            return null;
        }

        List<Resultat> results = resultatRepository.findByNageurId(nageurId);

        StatistiqueDTO.SwimmerStats stats = new StatistiqueDTO.SwimmerStats();
        stats.setNageurId(nageurId);
        stats.setNageurNom(nageur.getPrenom() + " " + nageur.getNom());
        stats.setTotalCourses(results.size());

        int gold = 0, silver = 0, bronze = 0;
        double totalPoints = 0;
        int pointsCount = 0;

        for (Resultat r : results) {
            if (r.getClassement() != null) {
                if (r.getClassement() == 1) gold++;
                else if (r.getClassement() == 2) silver++;
                else if (r.getClassement() == 3) bronze++;
            }
            if (r.getPoints() != null) {
                totalPoints += r.getPoints();
                pointsCount++;
            }
        }

        stats.setMedaillesOr(gold);
        stats.setMedaillesArgent(silver);
        stats.setMedaillesBronze(bronze);
        stats.setAveragePoints(pointsCount > 0 ? totalPoints / pointsCount : 0.0);

        // Personal records (where record flag is true)
        List<ResultatDTO> records = results.stream()
                .filter(r -> r.getRecord() != null && r.getRecord())
                .map(resultatMapper::toDTO)
                .collect(Collectors.toList());
        stats.setRecordsPersonnels(records);

        Map<String, List<ResultatDTO>> progressions = new LinkedHashMap<>();
        for (Resultat r : results) {
            if (r.getEpreuve() != null) {
                String eventName = r.getEpreuve().getDistance() + "m " + r.getEpreuve().getStyle();
                progressions.computeIfAbsent(eventName, k -> new ArrayList<>()).add(resultatMapper.toDTO(r));
            }
        }

        // Sort progressions by date chronologically
        for (Map.Entry<String, List<ResultatDTO>> entry : progressions.entrySet()) {
            entry.getValue().sort((a, b) -> {
                if (a.getDateCompetition() == null && b.getDateCompetition() == null) return 0;
                if (a.getDateCompetition() == null) return -1;
                if (b.getDateCompetition() == null) return 1;
                return a.getDateCompetition().compareTo(b.getDateCompetition());
            });
        }

        stats.setProgressions(progressions);
        return stats;
    }

    public StatistiqueDTO.ClubStats getClubStats() {
        StatistiqueDTO.ClubStats stats = new StatistiqueDTO.ClubStats();
        
        long totalComps = competitionRepository.count();
        long totalRes = resultatRepository.count();
        
        List<Resultat> allResults = resultatRepository.findAll();
        long totalRecs = allResults.stream()
                .filter(r -> r.getRecord() != null && r.getRecord())
                .count();

        stats.setTotalCompetitions(totalComps);
        stats.setTotalResultats(totalRes);
        stats.setTotalRecords(totalRecs);

        // Repartition of styles
        Map<String, Long> stylesRepartition = allResults.stream()
                .filter(r -> r.getEpreuve() != null && r.getEpreuve().getStyle() != null)
                .collect(Collectors.groupingBy(
                        r -> r.getEpreuve().getStyle(),
                        Collectors.counting()
                ));
        stats.setRepartitionStyles(stylesRepartition);

        // Top Swimmers (by FINA points sum or medal count)
        // Let's group by swimmer name, summing up FINA points, and counting 1st places
        Map<String, SwimmerPointsAndGolds> swimmerStatsMap = new HashMap<>();
        for (Resultat r : allResults) {
            if (r.getNageur() != null) {
                String name = r.getNageur().getPrenom() + " " + r.getNageur().getNom();
                SwimmerPointsAndGolds spg = swimmerStatsMap.computeIfAbsent(name, k -> new SwimmerPointsAndGolds());
                if (r.getPoints() != null) {
                    spg.points += r.getPoints();
                }
                if (r.getClassement() != null && r.getClassement() == 1) {
                    spg.golds++;
                }
            }
        }

        List<StatistiqueDTO.SwimmerSummary> topSwimmers = swimmerStatsMap.entrySet().stream()
                .map(entry -> new StatistiqueDTO.SwimmerSummary(
                        entry.getKey(),
                        entry.getValue().golds,
                        entry.getValue().points
                ))
                // Sort by gold medals first, then total points descending
                .sorted((a, b) -> {
                    if (b.getTotalMedaillesOr() != a.getTotalMedaillesOr()) {
                        return Integer.compare(b.getTotalMedaillesOr(), a.getTotalMedaillesOr());
                    }
                    return Integer.compare(b.getTotalPoints(), a.getTotalPoints());
                })
                .limit(5) // top 5 swimmers
                .collect(Collectors.toList());

        stats.setTopSwimmers(topSwimmers);

        return stats;
    }

    private static class SwimmerPointsAndGolds {
        int points = 0;
        int golds = 0;
    }
}
