package com.projectmission.dto;

import java.util.List;
import java.util.Map;

public class StatistiqueDTO {

    public static class SwimmerStats {
        private Long nageurId;
        private String nageurNom;
        private int totalCourses;
        private int medaillesOr;
        private int medaillesArgent;
        private int medaillesBronze;
        private double averagePoints;
        private List<ResultatDTO> recordsPersonnels;
        private Map<String, List<ResultatDTO>> progressions; // Key: event name (e.g. "100m NAGE_LIBRE"), Value: chronological results

        public Long getNageurId() { return nageurId; }
        public void setNageurId(Long nageurId) { this.nageurId = nageurId; }
        public String getNageurNom() { return nageurNom; }
        public void setNageurNom(String nageurNom) { this.nageurNom = nageurNom; }
        public int getTotalCourses() { return totalCourses; }
        public void setTotalCourses(int totalCourses) { this.totalCourses = totalCourses; }
        public int getMedaillesOr() { return medaillesOr; }
        public void setMedaillesOr(int medaillesOr) { this.medaillesOr = medaillesOr; }
        public int getMedaillesArgent() { return medaillesArgent; }
        public void setMedaillesArgent(int medaillesArgent) { this.medaillesArgent = medaillesArgent; }
        public int getMedaillesBronze() { return medaillesBronze; }
        public void setMedaillesBronze(int medaillesBronze) { this.medaillesBronze = medaillesBronze; }
        public double getAveragePoints() { return averagePoints; }
        public void setAveragePoints(double averagePoints) { this.averagePoints = averagePoints; }
        public List<ResultatDTO> getRecordsPersonnels() { return recordsPersonnels; }
        public void setRecordsPersonnels(List<ResultatDTO> recordsPersonnels) { this.recordsPersonnels = recordsPersonnels; }
        public Map<String, List<ResultatDTO>> getProgressions() { return progressions; }
        public void setProgressions(Map<String, List<ResultatDTO>> progressions) { this.progressions = progressions; }
    }

    public static class ClubStats {
        private long totalCompetitions;
        private long totalResultats;
        private long totalRecords;
        private Map<String, Long> repartitionStyles;
        private List<SwimmerSummary> topSwimmers;

        public long getTotalCompetitions() { return totalCompetitions; }
        public void setTotalCompetitions(long totalCompetitions) { this.totalCompetitions = totalCompetitions; }
        public long getTotalResultats() { return totalResultats; }
        public void setTotalResultats(long totalResultats) { this.totalResultats = totalResultats; }
        public long getTotalRecords() { return totalRecords; }
        public void setTotalRecords(long totalRecords) { this.totalRecords = totalRecords; }
        public Map<String, Long> getRepartitionStyles() { return repartitionStyles; }
        public void setRepartitionStyles(Map<String, Long> repartitionStyles) { this.repartitionStyles = repartitionStyles; }
        public List<SwimmerSummary> getTopSwimmers() { return topSwimmers; }
        public void setTopSwimmers(List<SwimmerSummary> topSwimmers) { this.topSwimmers = topSwimmers; }
    }

    public static class SwimmerSummary {
        private String nageurNom;
        private int totalMedaillesOr;
        private int totalPoints;

        public SwimmerSummary() {}
        public SwimmerSummary(String nageurNom, int totalMedaillesOr, int totalPoints) {
            this.nageurNom = nageurNom;
            this.totalMedaillesOr = totalMedaillesOr;
            this.totalPoints = totalPoints;
        }

        public String getNageurNom() { return nageurNom; }
        public void setNageurNom(String nageurNom) { this.nageurNom = nageurNom; }
        public int getTotalMedaillesOr() { return totalMedaillesOr; }
        public void setTotalMedaillesOr(int totalMedaillesOr) { this.totalMedaillesOr = totalMedaillesOr; }
        public int getTotalPoints() { return totalPoints; }
        public void setTotalPoints(int totalPoints) { this.totalPoints = totalPoints; }
    }
}
