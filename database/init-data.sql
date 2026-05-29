-- ============================================================
-- Project Mission – Database Seed Script
-- ============================================================
-- This script creates the database (if needed), the required
-- tables, and inserts demo accounts for easy testing.
--
-- Passwords are hashed with BCrypt (strength 10), compatible
-- with Spring Security's BCryptPasswordEncoder.
-- ============================================================

-- 1. Create the database
CREATE DATABASE IF NOT EXISTS projectmission
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE projectmission;

-- 2. Create tables (matches JPA @Inheritance JOINED strategy)
CREATE TABLE IF NOT EXISTS utilisateur (
    id                        BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom                       VARCHAR(255) NOT NULL,
    prenom                    VARCHAR(255) NOT NULL,
    email                     VARCHAR(255) NOT NULL UNIQUE,
    mot_de_passe              VARCHAR(255) NOT NULL,
    role                      VARCHAR(50)  NOT NULL,
    email_verified            TINYINT(1)   DEFAULT 1,
    verification_token        VARCHAR(255) DEFAULT NULL,
    verification_token_expiry DATETIME     DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS admin (
    id BIGINT PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES utilisateur(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS entraineur (
    id BIGINT PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES utilisateur(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS nageur (
    id BIGINT PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES utilisateur(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Insert demo accounts
--    All accounts have email_verified = 1 so you can login immediately.
--
--    ┌──────────────┬─────────────────────────┬─────────────┐
--    │ Role         │ Email                   │ Password    │
--    ├──────────────┼─────────────────────────┼─────────────┤
--    │ ADMIN        │ admin@mission.tn        │ Admin@123   │
--    │ ENTRAINEUR   │ coach@mission.tn        │ Coach@123   │
--    │ NAGEUR       │ nageur@mission.tn       │ Nageur@123  │
--    └──────────────┴─────────────────────────┴─────────────┘

-- Admin account
INSERT INTO utilisateur (nom, prenom, email, mot_de_passe, role, email_verified)
VALUES ('Kthiri', 'Thamer', 'admin@mission.tn',
        '$2y$10$H/oYChp5xIcbllxdjWLwPO6y.rdn5yRHCgiUQLhl6mLyPzcAbHGcG',
        'ADMIN', 1);
INSERT INTO admin (id) VALUES (LAST_INSERT_ID());

-- Entraineur (Coach) account
INSERT INTO utilisateur (nom, prenom, email, mot_de_passe, role, email_verified)
VALUES ('Ben Ali', 'Mohamed', 'coach@mission.tn',
        '$2y$10$WNvemz11W/2cbDZdG1lONuWT6HtTJmw1Lqx3FAYEmwhcPtk.63Lhy',
        'ENTRAINEUR', 1);
INSERT INTO entraineur (id) VALUES (LAST_INSERT_ID());

-- Nageur (Swimmer) account
INSERT INTO utilisateur (nom, prenom, email, mot_de_passe, role, email_verified)
VALUES ('Trabelsi', 'Amine', 'nageur@mission.tn',
        '$2y$10$UDPemtqLIJTpYO/L/JETjepoxfOSy6B4sskl1ej4TTvHgYTP/WMXW',
        'NAGEUR', 1);
INSERT INTO nageur (id) VALUES (LAST_INSERT_ID());
