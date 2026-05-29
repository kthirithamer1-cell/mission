package com.projectmission.config;

import com.projectmission.model.*;
import com.projectmission.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Configuration
@Profile("dev")
public class DevDataInitializer {

    @Bean
    CommandLineRunner seedData(
            ClubRepository clubRepository,
            AdminRepository adminRepository,
            EntraineurRepository entraineurRepository,
            NageurRepository nageurRepository,
            PiscineRepository piscineRepository,
            ReservationRepository reservationRepository,
            SeanceRepository seanceRepository,
            CompetitionRepository competitionRepository,
            EpreuveRepository epreuveRepository,
            ResultatRepository resultatRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            if (clubRepository.count() > 0) return;

            Club est = new Club();
            est.setNom("Espérance Sportive de Tunis");
            est.setAdresse("Stade El Menzah, Tunis");
            est.setDateAffiliation(LocalDate.of(1925, 1, 1));
            est = clubRepository.save(est);

            Club cnb = new Club();
            cnb.setNom("Club Nautique de Bizerte");
            cnb.setAdresse("Bizerte");
            cnb.setDateAffiliation(LocalDate.of(1980, 6, 15));
            cnb = clubRepository.save(cnb);

            Admin superAdmin = new Admin();
            superAdmin.setNom("Platform");
            superAdmin.setPrenom("Admin");
            superAdmin.setEmail("super@est.tn");
            superAdmin.setMotDePasse(passwordEncoder.encode("admin123"));
            superAdmin.setRole("SUPER_ADMIN");
            superAdmin.setSuperAdmin(true);
            adminRepository.save(superAdmin);

            Admin clubAdmin = new Admin();
            clubAdmin.setNom("Gharbi");
            clubAdmin.setPrenom("Karim");
            clubAdmin.setEmail("admin@est.tn");
            clubAdmin.setMotDePasse(passwordEncoder.encode("admin123"));
            clubAdmin.setRole("ADMIN");
            clubAdmin.setSuperAdmin(false);
            clubAdmin.setClub(est);
            adminRepository.save(clubAdmin);

            Entraineur coach1 = new Entraineur();
            coach1.setNom("Trabelsi");
            coach1.setPrenom("Sami");
            coach1.setEmail("sami.trabelsi@est.tn");
            coach1.setMotDePasse(passwordEncoder.encode("coach123"));
            coach1.setRole("ENTRAINEUR");
            coach1.setClub(est);
            coach1.setGroupes("Cadets, Juniors");
            entraineurRepository.save(coach1);

            Entraineur coach2 = new Entraineur();
            coach2.setNom("Ben Amor");
            coach2.setPrenom("Leila");
            coach2.setEmail("leila.benamor@est.tn");
            coach2.setMotDePasse(passwordEncoder.encode("coach123"));
            coach2.setRole("ENTRAINEUR");
            coach2.setClub(est);
            coach2.setGroupes("Benjamins, Minimes");
            entraineurRepository.save(coach2);

            String[] cats = {"A VENIR", "POUSSIN", "BENJAMINS", "MINIMES", "CADETS", "JUNIORS", "SENIORS"};
            int[] counts = {2, 4, 5, 4, 3, 3, 3};
            int idx = 0;
            for (int c = 0; c < cats.length; c++) {
                for (int i = 0; i < counts[c]; i++) {
                    idx++;
                    Nageur n = new Nageur();
                    n.setNom("Nageur" + idx);
                    n.setPrenom("Demo");
                    n.setEmail("nageur" + idx + "@est.tn");
                    n.setMotDePasse(passwordEncoder.encode("nageur123"));
                    n.setRole("NAGEUR");
                    n.setAge(10 + (idx % 12));
                    n.setSexe(idx % 2 == 0 ? "M" : "F");
                    n.setCategorie(cats[c]);
                    n.setClub(est);
                    nageurRepository.save(n);
                }
            }

            Piscine olympique = new Piscine();
            olympique.setNom("Piscine olympique Tunis");
            olympique.setAdresse("Avenue Mohamed V");
            olympique.setVille("Tunis");
            olympique.setNombreCouloirs(8);
            olympique.setLongueurMetres(50);
            olympique.setActive(true);
            olympique = piscineRepository.save(olympique);

            Piscine eveil = new Piscine();
            eveil.setNom("Piscine éveil");
            eveil.setAdresse("Centre aquatique");
            eveil.setVille("Tunis");
            eveil.setNombreCouloirs(4);
            eveil.setLongueurMetres(25);
            eveil.setActive(true);
            eveil = piscineRepository.save(eveil);

            Reservation r1 = new Reservation();
            r1.setPiscine(olympique);
            r1.setClub(est);
            r1.setDate(LocalDate.now().plusDays(1));
            r1.setHeureDebut(LocalTime.of(18, 0));
            r1.setHeureFin(LocalTime.of(19, 45));
            r1.setCouloirDebut(3);
            r1.setCouloirFin(6);
            r1.setStatut("CONFIRME");
            r1 = reservationRepository.save(r1);

            Reservation r2 = new Reservation();
            r2.setPiscine(olympique);
            r2.setClub(est);
            r2.setDate(LocalDate.now().plusDays(3));
            r2.setHeureDebut(LocalTime.of(17, 30));
            r2.setHeureFin(LocalTime.of(18, 30));
            r2.setCouloirDebut(1);
            r2.setCouloirFin(2);
            r2.setStatut("CONFIRME");
            reservationRepository.save(r2);

            Reservation r3 = new Reservation();
            r3.setPiscine(eveil);
            r3.setClub(est);
            r3.setDate(LocalDate.now().plusDays(4));
            r3.setHeureDebut(LocalTime.of(10, 0));
            r3.setHeureFin(LocalTime.of(11, 0));
            r3.setCouloirDebut(1);
            r3.setCouloirFin(1);
            r3.setStatut("EN_ATTENTE");
            reservationRepository.save(r3);

            Seance seance = new Seance();
            seance.setClub(est);
            seance.setReservation(r1);
            seance.setEntraineur(coach1);
            seance.setTitre("Entraînement cadets / juniors");
            seance.setDate(r1.getDate());
            seance.setHeureDebut(r1.getHeureDebut());
            seance.setHeureFin(r1.getHeureFin());
            seance.setDescription("Technique papillon + endurance");
            seanceRepository.save(seance);

            Competition comp = new Competition();
            comp.setSpecialite("Natation");
            comp.setEpreuve("Championnat TC");
            comp = competitionRepository.save(comp);

            List<Object[]> epreuves = List.of(
                    new Object[]{50, "NL", "CADETS"},
                    new Object[]{100, "NL", "JUNIORS"},
                    new Object[]{100, "PAPILLON", "SENIORS"},
                    new Object[]{100, "DOS", "CADETS"},
                    new Object[]{100, "BRASSE", "MINIMES"},
                    new Object[]{200, "4NAGES", "JUNIORS"}
            );
            List<String> temps = List.of("24.12", "52.45", "54.82", "58.90", "1:05.20", "2:18.44");
            List<Nageur> nageurs = nageurRepository.findByClub_Id(est.getId());
            for (int i = 0; i < epreuves.size(); i++) {
                Object[] ep = epreuves.get(i);
                Epreuve e = new Epreuve();
                e.setDistance((Integer) ep[0]);
                e.setStyle((String) ep[1]);
                e.setCategorie((String) ep[2]);
                e.setCompetition(comp);
                e = epreuveRepository.save(e);

                Resultat res = new Resultat();
                res.setEpreuve(e);
                res.setTemps(temps.get(i));
                res.setClassement(1);
                res.setNageur(nageurs.get(i % nageurs.size()));
                resultatRepository.save(res);
            }
        };
    }
}
