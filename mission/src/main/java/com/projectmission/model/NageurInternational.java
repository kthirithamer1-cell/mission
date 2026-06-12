package com.projectmission.model;

import jakarta.persistence.*;

@Entity
@Table(name = "nageur_international")
public class NageurInternational {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String nationalite;
    
    @Column(length = 1000)
    private String palmares;
    
    private String photoUrl;
    private String specialite;
    
    @Column(length = 1000)
    private String recordsPersonnels;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getNationalite() { return nationalite; }
    public void setNationalite(String nationalite) { this.nationalite = nationalite; }
    public String getPalmares() { return palmares; }
    public void setPalmares(String palmares) { this.palmares = palmares; }
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }
    public String getRecordsPersonnels() { return recordsPersonnels; }
    public void setRecordsPersonnels(String recordsPersonnels) { this.recordsPersonnels = recordsPersonnels; }
}
