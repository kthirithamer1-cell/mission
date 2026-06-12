package com.projectmission.model;

import jakarta.persistence.*;

@Entity
@Table(name = "competition_internationale")
public class CompetitionInternationale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String lieu;
    private String dates;
    private String type; // e.g. JO, MONDIAUX, COUPE_MONDE
    
    @Column(length = 2000)
    private String resultatsPrincipaux;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }
    public String getDates() { return dates; }
    public void setDates(String dates) { this.dates = dates; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getResultatsPrincipaux() { return resultatsPrincipaux; }
    public void setResultatsPrincipaux(String resultatsPrincipaux) { this.resultatsPrincipaux = resultatsPrincipaux; }
}
