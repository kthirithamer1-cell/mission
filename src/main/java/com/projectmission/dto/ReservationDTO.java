package com.projectmission.dto;

public class ReservationDTO {
    private Long id;
    private Long piscineId;
    private String piscineNom;
    private Long clubId;
    private String clubNom;
    private String date;
    private String heureDebut;
    private String heureFin;
    private Integer couloirDebut;
    private Integer couloirFin;
    private String statut;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPiscineId() { return piscineId; }
    public void setPiscineId(Long piscineId) { this.piscineId = piscineId; }
    public String getPiscineNom() { return piscineNom; }
    public void setPiscineNom(String piscineNom) { this.piscineNom = piscineNom; }
    public Long getClubId() { return clubId; }
    public void setClubId(Long clubId) { this.clubId = clubId; }
    public String getClubNom() { return clubNom; }
    public void setClubNom(String clubNom) { this.clubNom = clubNom; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getHeureDebut() { return heureDebut; }
    public void setHeureDebut(String heureDebut) { this.heureDebut = heureDebut; }
    public String getHeureFin() { return heureFin; }
    public void setHeureFin(String heureFin) { this.heureFin = heureFin; }
    public Integer getCouloirDebut() { return couloirDebut; }
    public void setCouloirDebut(Integer couloirDebut) { this.couloirDebut = couloirDebut; }
    public Integer getCouloirFin() { return couloirFin; }
    public void setCouloirFin(Integer couloirFin) { this.couloirFin = couloirFin; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}
