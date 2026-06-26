package com.projectmission.service;

import com.projectmission.dto.CalendarEventDTO;
import com.projectmission.dto.CalendarResponseDTO;
import com.projectmission.model.Entraineur;
import com.projectmission.model.Nageur;
import com.projectmission.model.Presence;
import com.projectmission.model.Reservation;
import com.projectmission.model.Seance;
import com.projectmission.repository.EntraineurRepository;
import com.projectmission.repository.NageurRepository;
import com.projectmission.repository.PresenceRepository;
import com.projectmission.repository.SeanceRepository;
import com.projectmission.security.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CalendarService {

    @Autowired
    private EntraineurRepository entraineurRepository;

    @Autowired
    private NageurRepository nageurRepository;

    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private PresenceRepository presenceRepository;

    @Autowired
    private CurrentUserService currentUserService;

    public CalendarResponseDTO getCoachCalendar(LocalDate from, LocalDate to) {
        String email = currentUserService.getEmail();
        Entraineur coach = entraineurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Entraineur not found for email: " + email));

        LocalDate[] range = resolveRange(from, to);
        from = range[0];
        to = range[1];

        List<Seance> sessions = seanceRepository.findByEntraineur_IdAndDateBetween(coach.getId(), from, to);
        sessions.sort(Comparator.comparing(Seance::getDate).thenComparing(Seance::getHeureDebut));

        List<Nageur> groupSwimmers = findGroupSwimmers(coach);
        List<String> swimmerNames = groupSwimmers.stream()
                .map(n -> n.getPrenom() + " " + n.getNom())
                .collect(Collectors.toList());

        List<CalendarEventDTO> events = new ArrayList<>();
        for (Seance s : sessions) {
            CalendarEventDTO event = toBaseEvent(s);
            event.setStudentsTotal(groupSwimmers.size());
            event.setStudentNames(swimmerNames.stream().limit(5).collect(Collectors.toList()));

            List<Presence> presences = presenceRepository.findBySeance_Id(s.getId());
            int present = (int) presences.stream().filter(p -> "PRESENT".equalsIgnoreCase(p.getStatut())).count();
            int absent = (int) presences.stream().filter(p -> "ABSENT".equalsIgnoreCase(p.getStatut())).count();
            int justifie = (int) presences.stream().filter(p -> "JUSTIFIE".equalsIgnoreCase(p.getStatut())).count();
            event.setPresentCount(present);
            event.setAbsentCount(absent);
            event.setJustifieCount(justifie);
            if (!presences.isEmpty()) {
                double rate = Math.round(((double) present / presences.size()) * 1000.0) / 10.0;
                event.setAttendanceRate(rate);
            }
            events.add(event);
        }

        return buildResponse(from, to, events);
    }

    public CalendarResponseDTO getNageurCalendar(LocalDate from, LocalDate to) {
        String email = currentUserService.getEmail();
        Nageur swimmer = nageurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Nageur not found for email: " + email));

        LocalDate[] range = resolveRange(from, to);
        from = range[0];
        to = range[1];

        List<Seance> sessions = swimmer.getClub() != null
                ? seanceRepository.findByClub_IdAndDateBetween(swimmer.getClub().getId(), from, to)
                : Collections.emptyList();
        sessions.sort(Comparator.comparing(Seance::getDate).thenComparing(Seance::getHeureDebut));

        String swimmerCategory = swimmer.getCategorie() != null ? swimmer.getCategorie().trim().toUpperCase() : "";

        List<CalendarEventDTO> events = new ArrayList<>();
        for (Seance s : sessions) {
            CalendarEventDTO event = toBaseEvent(s);
            event.setCategorie(swimmerCategory);

            boolean relevant = false;
            if (s.getEntraineur() != null && swimmerCategory.length() > 0) {
                relevant = coachSupervisesCategory(s.getEntraineur(), swimmerCategory);
            }
            event.setRelevantToMe(relevant);

            if (swimmer.getId() != null && s.getId() != null) {
                presenceRepository.findBySeance_IdAndNageur_Id(s.getId(), swimmer.getId())
                        .ifPresent(p -> event.setMyPresenceStatus(p.getStatut()));
            }
            events.add(event);
        }

        return buildResponse(from, to, events);
    }

    private CalendarEventDTO toBaseEvent(Seance s) {
        CalendarEventDTO event = new CalendarEventDTO();
        event.setId(s.getId());
        event.setTitre(s.getTitre());
        event.setDate(s.getDate() != null ? s.getDate().toString() : null);
        event.setHeureDebut(s.getHeureDebut() != null ? s.getHeureDebut().toString().substring(0, 5) : null);
        event.setHeureFin(s.getHeureFin() != null ? s.getHeureFin().toString().substring(0, 5) : null);
        event.setDescription(s.getDescription());
        if (s.getClub() != null) {
            event.setClubId(s.getClub().getId());
            event.setClubNom(s.getClub().getNom());
        }
        if (s.getEntraineur() != null) {
            event.setEntraineurId(s.getEntraineur().getId());
            event.setEntraineurNom(s.getEntraineur().getPrenom() + " " + s.getEntraineur().getNom());
        }
        Reservation reservation = s.getReservation();
        if (reservation != null) {
            event.setReservationId(reservation.getId());
            if (reservation.getPiscine() != null) {
                event.setPiscineNom(reservation.getPiscine().getNom());
            }
            if (reservation.getCouloirDebut() != null && reservation.getCouloirFin() != null) {
                event.setCouloirsLabel("Couloirs " + reservation.getCouloirDebut() + "–" + reservation.getCouloirFin());
            }
        }
        return event;
    }

    private List<Nageur> findGroupSwimmers(Entraineur coach) {
        if (coach.getClub() == null) return Collections.emptyList();
        List<String> coachGroups = parseGroups(coach.getGroupes());
        if (coachGroups.isEmpty()) return Collections.emptyList();
        return nageurRepository.findByClub_Id(coach.getClub().getId()).stream()
                .filter(n -> n.getCategorie() != null && coachGroups.contains(n.getCategorie().trim().toUpperCase()))
                .collect(Collectors.toList());
    }

    private boolean coachSupervisesCategory(Entraineur coach, String categorie) {
        return parseGroups(coach.getGroupes()).contains(categorie.trim().toUpperCase());
    }

    private List<String> parseGroups(String groupes) {
        if (groupes == null || groupes.isBlank()) return Collections.emptyList();
        return Arrays.stream(groupes.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .filter(g -> !g.isEmpty())
                .collect(Collectors.toList());
    }

    private LocalDate[] resolveRange(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            LocalDate today = LocalDate.now();
            LocalDate monday = today.with(DayOfWeek.MONDAY);
            LocalDate sunday = monday.plusDays(6);
            return new LocalDate[]{monday, sunday};
        }
        if (to.isBefore(from)) {
            LocalDate tmp = from;
            from = to;
            to = tmp;
        }
        return new LocalDate[]{from, to};
    }

    private CalendarResponseDTO buildResponse(LocalDate from, LocalDate to, List<CalendarEventDTO> events) {
        CalendarResponseDTO response = new CalendarResponseDTO();
        response.setFrom(from.toString());
        response.setTo(to.toString());
        response.setEvents(events);
        response.setTotalEvents(events.size());

        LocalDate today = LocalDate.now();
        LocalTime nowTime = LocalTime.now();
        int upcoming = 0;
        int totalMinutes = 0;
        for (CalendarEventDTO e : events) {
            if (e.getHeureDebut() != null && e.getHeureFin() != null) {
                String[] start = e.getHeureDebut().split(":");
                String[] end = e.getHeureFin().split(":");
                int mins = (Integer.parseInt(end[0]) * 60 + Integer.parseInt(end[1]))
                        - (Integer.parseInt(start[0]) * 60 + Integer.parseInt(start[1]));
                if (mins > 0) totalMinutes += mins;
            }
            if (e.getDate() != null) {
                LocalDate d = LocalDate.parse(e.getDate());
                if (d.isAfter(today) || (d.isEqual(today) && e.getHeureDebut() != null
                        && LocalTime.parse(e.getHeureDebut()).isAfter(nowTime))) {
                    upcoming++;
                }
            }
        }
        response.setUpcomingCount(upcoming);
        response.setTotalHours((int) Math.round(totalMinutes / 60.0));
        return response;
    }
}
