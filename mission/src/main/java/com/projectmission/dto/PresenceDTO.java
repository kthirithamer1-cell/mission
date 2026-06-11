package com.projectmission.dto;

public class PresenceDTO {
    private Long id;
    private Long seanceId;
    private Long nageurId;
    private String nageurNom;
    private String nageurPrenom;
    private String nageurCategorie;
    private String statut;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getSeanceId() { return seanceId; }
    public void setSeanceId(Long seanceId) { this.seanceId = seanceId; }
    public Long getNageurId() { return nageurId; }
    public void setNageurId(Long nageurId) { this.nageurId = nageurId; }
    public String getNageurNom() { return nageurNom; }
    public void setNageurNom(String nageurNom) { this.nageurNom = nageurNom; }
    public String getNageurPrenom() { return nageurPrenom; }
    public void setNageurPrenom(String nageurPrenom) { this.nageurPrenom = nageurPrenom; }
    public String getNageurCategorie() { return nageurCategorie; }
    public void setNageurCategorie(String nageurCategorie) { this.nageurCategorie = nageurCategorie; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}
