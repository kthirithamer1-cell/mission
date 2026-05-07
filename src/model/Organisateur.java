package model;

public class Organisateur extends Utilisateur {
    private String organisation;
    private String diplome;

    public Organisateur() {}

    public String getOrganisation() { return organisation; }
    public void setOrganisation(String organisation) { this.organisation = organisation; }
    public String getDiplome() { return diplome; }
    public void setDiplome(String diplome) { this.diplome = diplome; }
}

