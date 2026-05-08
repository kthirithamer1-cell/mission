package com.projectmission.dto;

import java.time.LocalDate;

public class ClubDTO {
    private Long id;
    private String nom;
    private String adresse;
    private LocalDate dateAffiliation;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public LocalDate getDateAffiliation() { return dateAffiliation; }
    public void setDateAffiliation(LocalDate dateAffiliation) { this.dateAffiliation = dateAffiliation; }
}
