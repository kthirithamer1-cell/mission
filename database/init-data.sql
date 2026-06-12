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

-- ============================================================
-- 4. Seed Data for Competitions, Epreuves, and Resultats
-- ============================================================

-- Insert sample competitions
INSERT INTO competition (nom, lieu, date_debut, date_fin, type, statut, niveau, organisateur, description, saison)
VALUES ('Championnat de Tunisie Open', 'Piscine Olympique de Rades', '2026-05-10', '2026-05-15', 'CHAMPIONNAT', 'TERMINE', 'NATIONAL', 'FTN', 'Le plus grand championnat national de natation en Tunisie.', '2025-2026');
SET @comp_id_1 = LAST_INSERT_ID();

INSERT INTO competition (nom, lieu, date_debut, date_fin, type, statut, niveau, organisateur, description, saison)
VALUES ('Coupe de la Ligue de Tunis', 'Piscine El Menzah', '2026-06-04', '2026-06-07', 'COUPE', 'EN_COURS', 'REGIONAL', 'Ligue de Tunis', 'Compétition régionale regroupant les clubs du Grand Tunis.', '2025-2026');
SET @comp_id_2 = LAST_INSERT_ID();

INSERT INTO competition (nom, lieu, date_debut, date_fin, type, statut, niveau, organisateur, description, saison)
VALUES ('Meeting International d\'Eté', 'Piscine de Rades', '2026-07-20', '2026-07-24', 'MEETING', 'A_VENIR', 'INTERNATIONAL', 'FTN / World Aquatics', 'Meeting international de natation réunissant les meilleurs athlètes.', '2025-2026');
SET @comp_id_3 = LAST_INSERT_ID();

-- Insert sample events (epreuves)
-- For comp 1 (Tunisie Open): 100m Nage Libre, 50m Papillon
INSERT INTO epreuve (distance, style, categorie, competition_id) VALUES (100, 'NAGE_LIBRE', 'SENIOR', @comp_id_1);
SET @epreuve_id_1 = LAST_INSERT_ID();
INSERT INTO epreuve (distance, style, categorie, competition_id) VALUES (50, 'PAPILLON', 'SENIOR', @comp_id_1);
SET @epreuve_id_2 = LAST_INSERT_ID();

-- For comp 2 (Coupe de la Ligue): 200m Brasse, 50m Nage Libre
INSERT INTO epreuve (distance, style, categorie, competition_id) VALUES (200, 'BRASSE', 'JUNIOR', @comp_id_2);
SET @epreuve_id_3 = LAST_INSERT_ID();
INSERT INTO epreuve (distance, style, categorie, competition_id) VALUES (50, 'NAGE_LIBRE', 'JUNIOR', @comp_id_2);
SET @epreuve_id_4 = LAST_INSERT_ID();

-- For comp 3 (Meeting): 100m Dos
INSERT INTO epreuve (distance, style, categorie, competition_id) VALUES (100, 'DOS', 'SENIOR', @comp_id_3);
SET @epreuve_id_5 = LAST_INSERT_ID();

-- Set details for swimmer (Amine Trabelsi, id = 3)
UPDATE nageur SET age = 20, sexe = 'M', categorie = 'SENIOR' WHERE id = 3;

-- Insert sample results for Amine Trabelsi (id = 3)
INSERT INTO resultat (temps, classement, points, record, date_competition, nageur_id, epreuve_id)
VALUES ('00:52.45', 2, 790, 0, '2026-05-12', 3, @epreuve_id_1);

INSERT INTO resultat (temps, classement, points, record, date_competition, nageur_id, epreuve_id)
VALUES ('00:24.89', 1, 810, 1, '2026-05-14', 3, @epreuve_id_2);

