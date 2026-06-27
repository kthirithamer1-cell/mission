package com.projectmission.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "piscine_id")
    private Piscine piscine;

    @ManyToOne(optional = false)
    @JoinColumn(name = "club_id")
    private Club club;

    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private Integer couloirDebut;
    private Integer couloirFin;
    /** EN_ATTENTE, CONFIRME, ANNULE, REJETE */
    private String statut = "EN_ATTENTE";
    private String motifRejet;

    @OneToOne(mappedBy = "reservation")
    private Seance seance;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Piscine getPiscine() { return piscine; }
    public void setPiscine(Piscine piscine) { this.piscine = piscine; }
    public Club getClub() { return club; }
    public void setClub(Club club) { this.club = club; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getHeureDebut() { return heureDebut; }
    public void setHeureDebut(LocalTime heureDebut) { this.heureDebut = heureDebut; }
    public LocalTime getHeureFin() { return heureFin; }
    public void setHeureFin(LocalTime heureFin) { this.heureFin = heureFin; }
    public Integer getCouloirDebut() { return couloirDebut; }
    public void setCouloirDebut(Integer couloirDebut) { this.couloirDebut = couloirDebut; }
    public Integer getCouloirFin() { return couloirFin; }
    public void setCouloirFin(Integer couloirFin) { this.couloirFin = couloirFin; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public String getMotifRejet() { return motifRejet; }
    public void setMotifRejet(String motifRejet) { this.motifRejet = motifRejet; }
    public Seance getSeance() { return seance; }
    public void setSeance(Seance seance) { this.seance = seance; }
}