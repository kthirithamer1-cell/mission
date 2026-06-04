package com.projectmission.dto;

public class CompetitionDTO {
    private Long id;
    private String specialite;
    private String epreuve;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }
    public String getEpreuve() { return epreuve; }
    public void setEpreuve(String epreuve) { this.epreuve = epreuve; }
}