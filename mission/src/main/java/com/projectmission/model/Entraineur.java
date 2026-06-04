package com.projectmission.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "entraineur")
public class Entraineur extends Utilisateur {
    private String groupes;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    public Entraineur() { super(); }

    public String getGroupes() { return groupes; }
    public void setGroupes(String groupes) { this.groupes = groupes; }
    public Club getClub() { return club; }
    public void setClub(Club club) { this.club = club; }
}