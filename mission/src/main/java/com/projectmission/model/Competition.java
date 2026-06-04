package com.projectmission.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "competition")
public class Competition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String specialite;
    private String epreuve;

    @OneToMany(mappedBy = "competition")
    private List<Epreuve> epreuves = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }
    public String getEpreuve() { return epreuve; }
    public void setEpreuve(String epreuve) { this.epreuve = epreuve; }
    public List<Epreuve> getEpreuves() { return epreuves; }
    public void setEpreuves(List<Epreuve> epreuves) { this.epreuves = epreuves; }
}