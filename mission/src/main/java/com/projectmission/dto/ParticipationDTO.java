package com.projectmission.dto;

import java.time.LocalDate;

public class ParticipationDTO {
    private Long id;
    private Long nageurId;
    private String nageurNom;
    private String nageurPrenom;
    private String nageurEmail;
    private Long competitionId;
    private String competitionNom;
    private Long clubId;
    private String clubNom;
    private LocalDate dateInscription;
    private String statut;      // INSCRIT, PRESENT, ABSENT, ANNULE
    private String dateCreation;
    private String dateModification;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getNageurId() { return nageurId; }
    public void setNageurId(Long nageurId) { this.nageurId = nageurId; }
    public String getNageurNom() { return nageurNom; }
    public void setNageurNom(String nageurNom) { this.nageurNom = nageurNom; }
    public String getNageurPrenom() { return nageurPrenom; }
    public void setNageurPrenom(String nageurPrenom) { this.nageurPrenom = nageurPrenom; }
    public String getNageurEmail() { return nageurEmail; }
    public void setNageurEmail(String nageurEmail) { this.nageurEmail = nageurEmail; }
    public Long getCompetitionId() { return competitionId; }
    public void setCompetitionId(Long competitionId) { this.competitionId = competitionId; }
    public String getCompetitionNom() { return competitionNom; }
    public void setCompetitionNom(String competitionNom) { this.competitionNom = competitionNom; }
    public Long getClubId() { return clubId; }
    public void setClubId(Long clubId) { this.clubId = clubId; }
    public String getClubNom() { return clubNom; }
    public void setClubNom(String clubNom) { this.clubNom = clubNom; }
    public LocalDate getDateInscription() { return dateInscription; }
    public void setDateInscription(LocalDate dateInscription) { this.dateInscription = dateInscription; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public String getDateCreation() { return dateCreation; }
    public void setDateCreation(String dateCreation) { this.dateCreation = dateCreation; }
    public String getDateModification() { return dateModification; }
    public void setDateModification(String dateModification) { this.dateModification = dateModification; }
}
