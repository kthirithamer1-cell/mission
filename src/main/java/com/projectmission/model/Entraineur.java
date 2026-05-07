package com.projectmission.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "entraineur")
public class Entraineur extends Utilisateur {
    public Entraineur() { super(); }
}

