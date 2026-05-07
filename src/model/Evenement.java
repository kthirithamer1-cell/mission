package model;

import java.util.Date;

public class Evenement {
    private String titre;
    private String description;
    private Date date;
    private Club club;

    public Evenement() {}

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public Club getClub() { return club; }
    public void setClub(Club club) { this.club = club; }
}

