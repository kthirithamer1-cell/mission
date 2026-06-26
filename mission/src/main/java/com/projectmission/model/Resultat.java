package com.projectmission.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "resultat")
public class Resultat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String temps;
    private Integer classement;
    private Integer points;
    private Boolean record;
    private LocalDate dateCompetition;

    @ManyToOne
    @JoinColumn(name = "nageur_id")
    private Nageur nageur;

    @ManyToOne
    @JoinColumn(name = "epreuve_id")
    private Epreuve epreuve;

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
    public Nageur getNageur() { return nageur; }
    public void setNageur(Nageur nageur) { this.nageur = nageur; }
    public Epreuve getEpreuve() { return epreuve; }
    public void setEpreuve(Epreuve epreuve) { this.epreuve = epreuve; }
}