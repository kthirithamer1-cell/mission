package com.projectmission.dto;

public class LiveResultEvent {
    private String nageurNom;
    private String epreuveNom;
    private String temps;
    private Integer classement;
    private Integer points;
    private Boolean record;
    private Long competitionId;
    private long timestamp;

    public LiveResultEvent() {
        this.timestamp = System.currentTimeMillis();
    }

    public LiveResultEvent(String nageurNom, String epreuveNom, String temps, Integer classement, Integer points, Boolean record, Long competitionId) {
        this.nageurNom = nageurNom;
        this.epreuveNom = epreuveNom;
        this.temps = temps;
        this.classement = classement;
        this.points = points;
        this.record = record;
        this.competitionId = competitionId;
        this.timestamp = System.currentTimeMillis();
    }

    public String getNageurNom() { return nageurNom; }
    public void setNageurNom(String nageurNom) { this.nageurNom = nageurNom; }
    public String getEpreuveNom() { return epreuveNom; }
    public void setEpreuveNom(String epreuveNom) { this.epreuveNom = epreuveNom; }
    public String getTemps() { return temps; }
    public void setTemps(String temps) { this.temps = temps; }
    public Integer getClassement() { return classement; }
    public void setClassement(Integer classement) { this.classement = classement; }
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
    public Boolean getRecord() { return record; }
    public void setRecord(Boolean record) { this.record = record; }
    public Long getCompetitionId() { return competitionId; }
    public void setCompetitionId(Long competitionId) { this.competitionId = competitionId; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
