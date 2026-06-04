package com.projectmission.model;

import jakarta.persistence.*;

@Entity
@Table(name = "resultat")
public class Resultat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String temps;
    private Integer classement;

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
    public Nageur getNageur() { return nageur; }
    public void setNageur(Nageur nageur) { this.nageur = nageur; }
    public Epreuve getEpreuve() { return epreuve; }
    public void setEpreuve(Epreuve epreuve) { this.epreuve = epreuve; }
}