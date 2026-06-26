package com.projectmission.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "competition")
public class Competition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String lieu;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String type;        // CHAMPIONNAT, COUPE, MEETING, CRITÉRIUM
    private String statut;      // A_VENIR, EN_COURS, TERMINE
    private String niveau;      // LOCAL, REGIONAL, NATIONAL, INTERNATIONAL
    private String organisateur;
    @Column(length = 1000)
    private String description;
    private String specialite;
    private String epreuve;
    private String saison;      // e.g. "2023-2024"

    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL)
    private List<Epreuve> epreuves = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public String getNiveau() { return niveau; }
    public void setNiveau(String niveau) { this.niveau = niveau; }
    public String getOrganisateur() { return organisateur; }
    public void setOrganisateur(String organisateur) { this.organisateur = organisateur; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }
    public String getEpreuve() { return epreuve; }
    public void setEpreuve(String epreuve) { this.epreuve = epreuve; }
    public String getSaison() { return saison; }
    public void setSaison(String saison) { this.saison = saison; }
    public List<Epreuve> getEpreuves() { return epreuves; }
    public void setEpreuves(List<Epreuve> epreuves) { this.epreuves = epreuves; }
}