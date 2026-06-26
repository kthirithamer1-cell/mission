package com.projectmission.dto;

import java.util.List;
import java.util.Map;

public class CoachDashboardDTO {
    private long seancesThisWeek;
    private long nageursCount;
    private long presencesCount;
    private SeanceDTO nextSeance;
    private List<SeanceDTO> weekSessions;
    private List<NageurDTO> groupSwimmers;
    private Map<String, Double> attendanceRateBySession;

    public CoachDashboardDTO() {}

    public long getSeancesThisWeek() { return seancesThisWeek; }
    public void setSeancesThisWeek(long seancesThisWeek) { this.seancesThisWeek = seancesThisWeek; }

    public long getNageursCount() { return nageursCount; }
    public void setNageursCount(long nageursCount) { this.nageursCount = nageursCount; }

    public long getPresencesCount() { return presencesCount; }
    public void setPresencesCount(long presencesCount) { this.presencesCount = presencesCount; }

    public SeanceDTO getNextSeance() { return nextSeance; }
    public void setNextSeance(SeanceDTO nextSeance) { this.nextSeance = nextSeance; }

    public List<SeanceDTO> getWeekSessions() { return weekSessions; }
    public void setWeekSessions(List<SeanceDTO> weekSessions) { this.weekSessions = weekSessions; }

    public List<NageurDTO> getGroupSwimmers() { return groupSwimmers; }
    public void setGroupSwimmers(List<NageurDTO> groupSwimmers) { this.groupSwimmers = groupSwimmers; }

    public Map<String, Double> getAttendanceRateBySession() { return attendanceRateBySession; }
    public void setAttendanceRateBySession(Map<String, Double> attendanceRateBySession) { this.attendanceRateBySession = attendanceRateBySession; }
}
