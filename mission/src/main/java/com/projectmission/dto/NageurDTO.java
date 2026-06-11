package com.projectmission.dto;

public class NageurDTO extends UtilisateurDTO {
    private Integer age;
    private String sexe;
    private String categorie;
    private Long clubId;
    private String clubNom;
    private Long licenceId;

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getSexe() { return sexe; }
    public void setSexe(String sexe) { this.sexe = sexe; }
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public Long getClubId() { return clubId; }
    public void setClubId(Long clubId) { this.clubId = clubId; }
    public String getClubNom() { return clubNom; }
    public void setClubNom(String clubNom) { this.clubNom = clubNom; }
    public Long getLicenceId() { return licenceId; }
    public void setLicenceId(Long licenceId) { this.licenceId = licenceId; }
}