package com.projectmission.dto;

import java.time.LocalDate;

public class ResultatDTO {
    private Long id;
    private String temps;
    private Integer classement;
    private Integer points;
    private Boolean record;
    private LocalDate dateCompetition;
    private Long nageurId;
    private String nageurNom;
    private Long epreuveId;
    private String epreuveNom;
    private String competitionNom;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTemps() { return temps; }
    public void setTemps(String temps) { this.temps = temps; }
    public Integer getClassement() { return classement; }
    public void setClassement(Integer classement) { this.classement = classement; }
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
    public Boolean getRecord() { return record; }
    public void setRecord(Boolean record) { this.record = record; }
    public LocalDate getDateCompetition() { return dateCompetition; }
    public void setDateCompetition(LocalDate dateCompetition) { this.dateCompetition = dateCompetition; }
    public Long getNageurId() { return nageurId; }
    public void setNageurId(Long nageurId) { this.nageurId = nageurId; }
    public String getNageurNom() { return nageurNom; }
    public void setNageurNom(String nageurNom) { this.nageurNom = nageurNom; }
    public Long getEpreuveId() { return epreuveId; }
    public void setEpreuveId(Long epreuveId) { this.epreuveId = epreuveId; }
    public String getEpreuveNom() { return epreuveNom; }
    public void setEpreuveNom(String epreuveNom) { this.epreuveNom = epreuveNom; }
    public String getCompetitionNom() { return competitionNom; }
    public void setCompetitionNom(String competitionNom) { this.competitionNom = competitionNom; }
}