package com.projectmission.dto;

public class ProfileSetupRequest {
    private String token;
    private String nom;
    private String prenom;
    private Integer age;
    private String sexe;
    private String categorie;
    private Long clubId;
    private String groupes;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getSexe() { return sexe; }
    public void setSexe(String sexe) { this.sexe = sexe; }
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public Long getClubId() { return clubId; }
    public void setClubId(Long clubId) { this.clubId = clubId; }
    public String getGroupes() { return groupes; }
    public void setGroupes(String groupes) { this.groupes = groupes; }
}
