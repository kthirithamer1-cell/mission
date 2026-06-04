package com.projectmission.dto;

public class PiscineDTO {
    private Long id;
    private String nom;
    private String adresse;
    private String ville;
    private Integer nombreCouloirs;
    private Integer longueurMetres;
    private Boolean active;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }
    public Integer getNombreCouloirs() { return nombreCouloirs; }
    public void setNombreCouloirs(Integer nombreCouloirs) { this.nombreCouloirs = nombreCouloirs; }
    public Integer getLongueurMetres() { return longueurMetres; }
    public void setLongueurMetres(Integer longueurMetres) { this.longueurMetres = longueurMetres; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}