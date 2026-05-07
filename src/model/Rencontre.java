package model;

public class Rencontre extends Evenement {
    private String theme;
    private String intervenant;

    public Rencontre() {}

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    public String getIntervenant() { return intervenant; }
    public void setIntervenant(String intervenant) { this.intervenant = intervenant; }
}

