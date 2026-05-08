package com.projectmission.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "nageur")
public class Nageur extends Utilisateur {
    private Integer age;
    private String sexe;
    private String categorie;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    @OneToOne(mappedBy = "nageur")
    private Licence licence;

    @OneToMany(mappedBy = "nageur")
    private List<Equipement> equipements = new ArrayList<>();

    @OneToMany(mappedBy = "nageur")
    private List<Resultat> resultats = new ArrayList<>();

    public Nageur() { super(); }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getSexe() { return sexe; }
    public void setSexe(String sexe) { this.sexe = sexe; }
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public Club getClub() { return club; }
    public void setClub(Club club) { this.club = club; }
    public Licence getLicence() { return licence; }
    public void setLicence(Licence licence) { this.licence = licence; }
    public List<Equipement> getEquipements() { return equipements; }
    public void setEquipements(List<Equipement> equipements) { this.equipements = equipements; }
    public List<Resultat> getResultats() { return resultats; }
    public void setResultats(List<Resultat> resultats) { this.resultats = resultats; }
}
