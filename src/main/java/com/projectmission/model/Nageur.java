package com.projectmission.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "nageur")
public class Nageur extends Utilisateur {
    public Nageur() { super(); }
}

