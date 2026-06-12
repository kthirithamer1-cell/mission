package com.projectmission.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "record_mondial")
public class RecordMondial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String epreuve;
    private String temps;
    private String nageur;
    private String nationalite;
    private LocalDate date;
    private String bassin; // 25m or 50m
    private String sexe;   // H or F

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEpreuve() { return epreuve; }
    public void setEpreuve(String epreuve) { this.epreuve = epreuve; }
    public String getTemps() { return temps; }
    public void setTemps(String temps) { this.temps = temps; }
    public String getNageur() { return nageur; }
    public void setNageur(String nageur) { this.nageur = nageur; }
    public String getNationalite() { return nationalite; }
    public void setNationalite(String nationalite) { this.nationalite = nationalite; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getBassin() { return bassin; }
    public void setBassin(String bassin) { this.bassin = bassin; }
    public String getSexe() { return sexe; }
    public void setSexe(String sexe) { this.sexe = sexe; }
}
