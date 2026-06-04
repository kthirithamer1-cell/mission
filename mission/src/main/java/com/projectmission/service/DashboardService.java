package com.projectmission.service;

import com.projectmission.dto.DashboardStatsDTO;
import com.projectmission.model.Epreuve;
import com.projectmission.model.Nageur;
import com.projectmission.model.Reservation;
import com.projectmission.model.Resultat;
import com.projectmission.repository.*;
import com.projectmission.security.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private static final List<String> CATEGORIES = List.of(
            "A VENIR", "POUSSIN", "BENJAMINS", "MINIMES", "CADETS", "JUNIORS", "SENIORS");

    @Autowired
    private NageurRepository nageurRepository;
    @Autowired
    private EntraineurRepository entraineurRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private PiscineRepository piscineRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private ResultatRepository resultatRepository;
    @Autowired
    private CurrentUserService currentUserService;

    public DashboardStatsDTO getStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        boolean superAdmin = currentUserService.isSuperAdmin();
        Long clubId = currentUserService.getClubId();
        LocalDate today = LocalDate.now();
        LocalDate weekEnd = today.plusDays(7);

        if (superAdmin) {
            stats.setPiscinesCount(piscineRepository.count());
            stats.setClubsCount(clubRepository.count());
            stats.setReservationsEnAttente(
                    reservationRepository.findByStatut("EN_ATTENTE").size());
            stats.setNageursCount(nageurRepository.count());
            stats.setEntraineursCount(entraineurRepository.count());
            buildCategoryStats(stats, nageurRepository.findAll());
            stats.setRecords(buildRecords(null));
            stats.setCouloirsReserves(countCouloirs(reservationRepository.findAll(), today, weekEnd));
            stats.setCreneauxAVenir(countUpcoming(reservationRepository.findAll(), today, weekEnd));
        } else if (clubId != null) {
            stats.setNageursCount(nageurRepository.countByClub_Id(clubId));
            stats.setEntraineursCount(entraineurRepository.countByClub_Id(clubId));
            stats.setReservationsEnAttente(
                    reservationRepository.findByClub_IdAndStatut(clubId, "EN_ATTENTE").size());
            List<Reservation> clubReservations = reservationRepository.findByClub_Id(clubId);
            stats.setCouloirsReserves(countCouloirs(clubReservations, today, weekEnd));
            stats.setCreneauxAVenir(countUpcoming(clubReservations, today, weekEnd));
            buildCategoryStats(stats, nageurRepository.findByClub_Id(clubId));
            stats.setRecords(buildRecords(clubId));
        }
        return stats;
    }

    private void buildCategoryStats(DashboardStatsDTO stats, List<Nageur> nageurs) {
        Map<String, Long> repartition = new LinkedHashMap<>();
        for (String cat : CATEGORIES) {
            long count = nageurs.stream()
                    .filter(n -> cat.equalsIgnoreCase(n.getCategorie() != null ? n.getCategorie().trim() : ""))
                    .count();
            repartition.put(cat, count);
        }
        stats.setRepartitionCategories(repartition);

        Map<String, List<Long>> evolution = new LinkedHashMap<>();
        for (String cat : CATEGORIES) {
            long current = repartition.getOrDefault(cat, 0L);
            List<Long> series = new ArrayList<>();
            for (int i = 5; i >= 0; i--) {
                series.add(Math.max(0, current - i));
            }
            evolution.put(cat, series);
        }
        stats.setEvolutionParCategorie(evolution);
    }

    private List<DashboardStatsDTO.RecordDTO> buildRecords(Long clubId) {
        List<Resultat> all = resultatRepository.findAll();
        Map<String, DashboardStatsDTO.RecordDTO> best = new LinkedHashMap<>();
        for (Resultat r : all) {
            if (clubId != null && (r.getNageur() == null || r.getNageur().getClub() == null
                    || !clubId.equals(r.getNageur().getClub().getId()))) {
                continue;
            }
            Epreuve e = r.getEpreuve();
            if (e == null || r.getTemps() == null) continue;
            String key = e.getDistance() + "m " + e.getStyle();
            String label = e.getDistance() + " m " + formatStyle(e.getStyle());
            Nageur n = r.getNageur();
            String nageurNom = n != null ? n.getPrenom() + " " + n.getNom() : "ΓÇö";
            String cat = n != null ? n.getCategorie() : "ΓÇö";
            if (!best.containsKey(key) || compareTime(r.getTemps(), best.get(key).getTemps()) < 0) {
                best.put(key, new DashboardStatsDTO.RecordDTO(label, r.getTemps(), nageurNom, cat));
            }
        }
        return new ArrayList<>(best.values()).stream().limit(8).collect(Collectors.toList());
    }

    private String formatStyle(String style) {
        if (style == null) return "ΓÇö";
        return switch (style.toUpperCase()) {
            case "NL", "NAGE LIBRE" -> "nage libre";
            case "PAPILLON" -> "papillon";
            case "DOS" -> "dos";
            case "BRASSE" -> "brasse";
            case "4NAGES", "4 NAGES" -> "4 nages";
            default -> style.toLowerCase();
        };
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

    private long countCouloirs(List<Reservation> reservations, LocalDate from, LocalDate to) {
        return reservations.stream()
                .filter(r -> "CONFIRME".equals(r.getStatut()))
                .filter(r -> r.getDate() != null && !r.getDate().isBefore(from) && !r.getDate().isAfter(to))
                .mapToLong(r -> (long) r.getCouloirFin() - r.getCouloirDebut() + 1)
                .sum();
    }

    private long countUpcoming(List<Reservation> reservations, LocalDate from, LocalDate to) {
        return reservations.stream()
                .filter(r -> !"ANNULE".equals(r.getStatut()))
                .filter(r -> r.getDate() != null && !r.getDate().isBefore(from) && !r.getDate().isAfter(to))
                .count();
    }
}