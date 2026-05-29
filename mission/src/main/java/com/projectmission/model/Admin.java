package com.projectmission.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin")
public class Admin extends Utilisateur {
    public Admin() { super(); }
}

