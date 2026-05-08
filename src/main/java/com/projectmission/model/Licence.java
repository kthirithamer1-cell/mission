package com.projectmission.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "licence")
public class Licence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numLicence;
    private LocalDate dateDebut;
    private LocalDate dateExpiration;
    private String statut;

    @OneToOne
    @JoinColumn(name = "nageur_id", unique = true)
    private Nageur nageur;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumLicence() { return numLicence; }
    public void setNumLicence(String numLicence) { this.numLicence = numLicence; }
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    public LocalDate getDateExpiration() { return dateExpiration; }
    public void setDateExpiration(LocalDate dateExpiration) { this.dateExpiration = dateExpiration; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public Nageur getNageur() { return nageur; }
    public void setNageur(Nageur nageur) { this.nageur = nageur; }
}
