package com.projectmission.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin")
public class Admin extends Utilisateur {
    private Boolean superAdmin = false;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    public Admin() { super(); }

    public Boolean getSuperAdmin() { return superAdmin; }
    public void setSuperAdmin(Boolean superAdmin) { this.superAdmin = superAdmin; }
    public Club getClub() { return club; }
    public void setClub(Club club) { this.club = club; }
}
