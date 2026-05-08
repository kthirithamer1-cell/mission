package com.projectmission.model;

import jakarta.persistence.*;

@Entity
@Table(name = "equipement")
public class Equipement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @ManyToOne
    @JoinColumn(name = "nageur_id")
    private Nageur nageur;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Nageur getNageur() { return nageur; }
    public void setNageur(Nageur nageur) { this.nageur = nageur; }
}
