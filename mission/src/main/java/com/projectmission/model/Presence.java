package com.projectmission.model;

import jakarta.persistence.*;

@Entity
@Table(name = "presence", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"seance_id", "nageur_id"})
})
public class Presence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "seance_id")
    private Seance seance;

    @ManyToOne(optional = false)
    @JoinColumn(name = "nageur_id")
    private Nageur nageur;

    /** PRESENT | ABSENT | JUSTIFIE */
    private String statut = "ABSENT";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Seance getSeance() { return seance; }
    public void setSeance(Seance seance) { this.seance = seance; }
    public Nageur getNageur() { return nageur; }
    public void setNageur(Nageur nageur) { this.nageur = nageur; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}
