package model;

public class Competition extends Evenement {
    private String type;
    private String niveau;

    public Competition() {}

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getNiveau() { return niveau; }
    public void setNiveau(String niveau) { this.niveau = niveau; }
}

