package com.projectmission.service;

import com.projectmission.dto.CoachDashboardDTO;
import com.projectmission.dto.NageurDTO;
import com.projectmission.dto.SeanceDTO;
import com.projectmission.mapper.NageurMapper;
import com.projectmission.mapper.SeanceMapper;
import com.projectmission.model.Entraineur;
import com.projectmission.model.Nageur;
import com.projectmission.model.Presence;
import com.projectmission.model.Seance;
import com.projectmission.repository.EntraineurRepository;
import com.projectmission.repository.NageurRepository;
import com.projectmission.repository.PresenceRepository;
import com.projectmission.repository.SeanceRepository;
import com.projectmission.security.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CoachDashboardService {

    @Autowired
    private EntraineurRepository entraineurRepository;

    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private NageurRepository nageurRepository;

    @Autowired
    private PresenceRepository presenceRepository;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private SeanceMapper seanceMapper;

    @Autowired
    private NageurMapper nageurMapper;

    public CoachDashboardDTO getDashboardData() {
        String email = currentUserService.getEmail();
        Entraineur coach = entraineurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Entraineur not found for email: " + email));

        CoachDashboardDTO dto = new CoachDashboardDTO();

        // 1. Get all coach sessions
        List<Seance> allSessions = seanceRepository.findByEntraineur_Id(coach.getId());

        // 2. Filter sessions for this week (Monday to Sunday)
        LocalDate today = LocalDate.now();
        LocalDate monday = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate sunday = monday.plusDays(6);

        List<Seance> weekSessions = allSessions.stream()
                .filter(s -> s.getDate() != null && !s.getDate().isBefore(monday) && !s.getDate().isAfter(sunday))
                .sorted(Comparator.comparing(Seance::getDate).thenComparing(Seance::getHeureDebut))
                .collect(Collectors.toList());

        dto.setSeancesThisWeek(weekSessions.size());
        dto.setWeekSessions(weekSessions.stream().map(seanceMapper::toDTO).collect(Collectors.toList()));

        // 3. Find next upcoming session
        LocalTime nowTime = LocalTime.now();
        Seance nextSeance = allSessions.stream()
                .filter(s -> s.getDate() != null && 
                        (s.getDate().isAfter(today) || 
                         (s.getDate().isEqual(today) && s.getHeureDebut() != null && s.getHeureDebut().isAfter(nowTime))))
                .min(Comparator.comparing(Seance::getDate).thenComparing(Seance::getHeureDebut))
                .orElse(null);

        if (nextSeance != null) {
            dto.setNextSeance(seanceMapper.toDTO(nextSeance));
        }

        // 4. Find swimmers supervised by this coach
        List<Nageur> allClubSwimmers = coach.getClub() != null ? 
                nageurRepository.findByClub_Id(coach.getClub().getId()) : new ArrayList<>();
        
        List<String> coachGroups = Arrays.stream(coach.getGroupes() != null ? coach.getGroupes().split(",") : new String[0])
                .map(String::trim)
                .map(String::toUpperCase)
                .filter(g -> !g.isEmpty())
                .collect(Collectors.toList());

        List<Nageur> groupSwimmers = allClubSwimmers.stream()
                .filter(n -> n.getCategorie() != null && coachGroups.contains(n.getCategorie().trim().toUpperCase()))
                .collect(Collectors.toList());

        dto.setNageursCount(groupSwimmers.size());
        dto.setGroupSwimmers(groupSwimmers.stream().map(nageurMapper::toDTO).collect(Collectors.toList()));

        // 5. Calculate presences count across all coach's sessions
        long presencesCount = 0;
        for (Seance s : allSessions) {
            presencesCount += presenceRepository.findBySeance_Id(s.getId()).stream()
                    .filter(p -> "PRESENT".equalsIgnoreCase(p.getStatut()))
                    .count();
        }
        dto.setPresencesCount(presencesCount);

        // 6. Build attendance rate by session (last 6 past sessions)
        Map<String, Double> attendanceRateBySession = new LinkedHashMap<>();
        List<Seance> pastSessions = allSessions.stream()
                .filter(s -> s.getDate() != null && s.getDate().isBefore(today.plusDays(1)))
                .sorted(Comparator.comparing(Seance::getDate).reversed())
                .limit(6)
                .collect(Collectors.toList());
        
        // Reverse them again to have chronological order in charts
        Collections.reverse(pastSessions);

        for (Seance s : pastSessions) {
            List<Presence> presences = presenceRepository.findBySeance_Id(s.getId());
            if (!presences.isEmpty()) {
                long presentCount = presences.stream().filter(p -> "PRESENT".equalsIgnoreCase(p.getStatut())).count();
                double rate = ((double) presentCount / presences.size()) * 100.0;
                // Round to 1 decimal place
                rate = Math.round(rate * 10.0) / 10.0;
                String label = s.getTitre() + " (" + s.getDate().toString() + ")";
                attendanceRateBySession.put(label, rate);
            }
        }
        dto.setAttendanceRateBySession(attendanceRateBySession);

        return dto;
    }
}
