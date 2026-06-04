package com.projectmission.dto;

public class SeanceDTO {
    private Long id;
    private Long clubId;
    private String clubNom;
    private Long reservationId;
    private Long entraineurId;
    private String entraineurNom;
    private String titre;
    private String date;
    private String heureDebut;
    private String heureFin;
    private String description;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getClubId() { return clubId; }
    public void setClubId(Long clubId) { this.clubId = clubId; }
    public String getClubNom() { return clubNom; }
    public void setClubNom(String clubNom) { this.clubNom = clubNom; }
    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }
    public Long getEntraineurId() { return entraineurId; }
    public void setEntraineurId(Long entraineurId) { this.entraineurId = entraineurId; }
    public String getEntraineurNom() { return entraineurNom; }
    public void setEntraineurNom(String entraineurNom) { this.entraineurNom = entraineurNom; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getHeureDebut() { return heureDebut; }
    public void setHeureDebut(String heureDebut) { this.heureDebut = heureDebut; }
    public String getHeureFin() { return heureFin; }
    public void setHeureFin(String heureFin) { this.heureFin = heureFin; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}