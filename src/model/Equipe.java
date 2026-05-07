package model;

import java.util.List;

public class Equipe {
    private String nom;
    private String categorie;
    private List<Adherent> membres;

    public Equipe() {}

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public List<Adherent> getMembres() { return membres; }
    public void setMembres(List<Adherent> membres) { this.membres = membres; }
}

