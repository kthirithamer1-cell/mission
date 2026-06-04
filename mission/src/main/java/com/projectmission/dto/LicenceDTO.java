package com.projectmission.dto;

import java.time.LocalDate;

public class LicenceDTO {
    private Long id;
    private String numLicence;
    private LocalDate dateDebut;
    private LocalDate dateExpiration;
    private String statut;
    private Long nageurId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumLicence() { return numLicence; }
    public void setNumLicence(String numLicence) { this.numLicence = numLicence; }
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    public LocalDate getDateExpiration() { return dateExpiration; }
    public void setDateExpiration(LocalDate dateExpiration) { this.dateExpiration = dateExpiration; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public Long getNageurId() { return nageurId; }
    public void setNageurId(Long nageurId) { this.nageurId = nageurId; }
}