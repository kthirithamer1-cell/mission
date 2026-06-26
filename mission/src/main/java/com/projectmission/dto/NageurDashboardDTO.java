package com.projectmission.dto;

import java.util.List;
import java.util.Map;

public class NageurDashboardDTO {
    private long resultatsCount;
    private long presencesCount;
    private SeanceDTO nextSeance;
    private String categorie;
    private List<ResultDetailDTO> recentResults;
    private List<SeanceDTO> upcomingSessions;
    private List<RecordDTO> personalRecords;
    private Map<String, List<Double>> progressionData;

    public static class ResultDetailDTO {
        private Long id;
        private String temps;
        private Integer classement;
        private String epreuveStyle;
        private Integer epreuveDistance;
        private String competitionNom;

        public ResultDetailDTO() {}
        public ResultDetailDTO(Long id, String temps, Integer classement, String style, Integer distance, String compNom) {
            this.id = id;
            this.temps = temps;
            this.classement = classement;
            this.epreuveStyle = style;
            this.epreuveDistance = distance;
            this.competitionNom = compNom;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getTemps() { return temps; }
        public void setTemps(String temps) { this.temps = temps; }
        public Integer getClassement() { return classement; }
        public void setClassement(Integer classement) { this.classement = classement; }
        public String getEpreuveStyle() { return epreuveStyle; }
        public void setEpreuveStyle(String epreuveStyle) { this.epreuveStyle = epreuveStyle; }
        public Integer getEpreuveDistance() { return epreuveDistance; }
        public void setEpreuveDistance(Integer epreuveDistance) { this.epreuveDistance = epreuveDistance; }
        public String getCompetitionNom() { return competitionNom; }
        public void setCompetitionNom(String competitionNom) { this.competitionNom = competitionNom; }
    }

    public static class RecordDTO {
        private String epreuveLabel;
        private String temps;
        private String date;

        public RecordDTO() {}
        public RecordDTO(String epreuveLabel, String temps, String date) {
            this.epreuveLabel = epreuveLabel;
            this.temps = temps;
            this.date = date;
        }

        public String getEpreuveLabel() { return epreuveLabel; }
        public void setEpreuveLabel(String epreuveLabel) { this.epreuveLabel = epreuveLabel; }
        public String getTemps() { return temps; }
        public void setTemps(String temps) { this.temps = temps; }
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
    }

    public NageurDashboardDTO() {}

    public long getResultatsCount() { return resultatsCount; }
    public void setResultatsCount(long resultatsCount) { this.resultatsCount = resultatsCount; }

    public long getPresencesCount() { return presencesCount; }
    public void setPresencesCount(long presencesCount) { this.presencesCount = presencesCount; }

    public SeanceDTO getNextSeance() { return nextSeance; }
    public void setNextSeance(SeanceDTO nextSeance) { this.nextSeance = nextSeance; }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    public List<ResultDetailDTO> getRecentResults() { return recentResults; }
    public void setRecentResults(List<ResultDetailDTO> recentResults) { this.recentResults = recentResults; }

    public List<SeanceDTO> getUpcomingSessions() { return upcomingSessions; }
    public void setUpcomingSessions(List<SeanceDTO> upcomingSessions) { this.upcomingSessions = upcomingSessions; }

    public List<RecordDTO> getPersonalRecords() { return personalRecords; }
    public void setPersonalRecords(List<RecordDTO> personalRecords) { this.personalRecords = personalRecords; }

    public Map<String, List<Double>> getProgressionData() { return progressionData; }
    public void setProgressionData(Map<String, List<Double>> progressionData) { this.progressionData = progressionData; }
}
