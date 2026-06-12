package com.projectmission.dto;

import java.time.LocalDate;

public class CompetitionDTO {
    private Long id;
    private String nom;
    private String lieu;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String type;
    private String statut;
    private String niveau;
    private String organisateur;
    private String description;
    private String specialite;
    private String epreuve;
    private String saison;
    private int nombreEpreuves;

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
    public int getNombreEpreuves() { return nombreEpreuves; }
    public void setNombreEpreuves(int nombreEpreuves) { this.nombreEpreuves = nombreEpreuves; }
}