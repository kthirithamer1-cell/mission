package com.projectmission.service;

import com.projectmission.model.CompetitionInternationale;
import com.projectmission.model.NageurInternational;
import com.projectmission.model.RecordMondial;
import com.projectmission.repository.CompetitionInternationaleRepository;
import com.projectmission.repository.NageurInternationalRepository;
import com.projectmission.repository.RecordMondialRepository;
import jakarta.annotation.PostConstruct;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class WorldAquaticsScrapingService {

    @Autowired
    private RecordMondialRepository recordRepository;

    @Autowired
    private NageurInternationalRepository swimmerRepository;

    @Autowired
    private CompetitionInternationaleRepository competitionRepository;

    @PostConstruct
    public void initData() {
        // Automatically check if database has records, if not, seed them
        if (recordRepository.count() == 0) {
            scrapeAndSave();
        }
    }

    // Run once a day at midnight to refresh records
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduledScrape() {
        scrapeAndSave();
    }

    public synchronized void scrapeAndSave() {
        boolean success = false;
        try {
            // Try to scrape world records from Wikipedia
            String url = "https://en.wikipedia.org/wiki/List_of_world_records_in_swimming";
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(10000)
                    .get();

            List<RecordMondial> recordsList = new ArrayList<>();
            // The page contains tables for Men's long course, Women's long course, Mixed long course, etc.
            Elements tables = doc.select("table.wikitable");
            
            // Let's parse the first two tables: Men's long course and Women's long course
            if (tables.size() >= 2) {
                // Table 0: Men's Long Course
                parseWikipediaTable(tables.get(0), "50m", "H", recordsList);
                // Table 1: Women's Long Course
                parseWikipediaTable(tables.get(1), "50m", "F", recordsList);
                
                if (!recordsList.isEmpty()) {
                    recordRepository.deleteAll();
                    recordRepository.saveAll(recordsList);
                    success = true;
                }
            }
        } catch (Exception e) {
            System.err.println("Scraping from Wikipedia failed, loading high-quality fallback: " + e.getMessage());
        }

        if (!success) {
            loadFallbackRecords();
        }

        // Seed International Swimmers & Competitions if empty
        if (swimmerRepository.count() == 0) {
            seedSwimmers();
        }
        if (competitionRepository.count() == 0) {
            seedCompetitions();
        }
    }

    private void parseWikipediaTable(Element table, String bassin, String sexe, List<RecordMondial> recordsList) {
        Elements rows = table.select("tbody tr");
        for (Element row : rows) {
            Elements cols = row.select("td, th");
            // Usually, Wikipedia swimming records table has columns:
            // Event, Time, Swimmer, Nationality, Date, Meet, Location, Ref
            if (cols.size() >= 5) {
                try {
                    String event = cols.get(0).text().trim();
                    // Skip headers or relay events we don't want
                    if (event.contains("Event") || event.contains("relay") || event.contains("Relay")) {
                        continue;
                    }
                    String time = cols.get(1).text().split("\\[")[0].trim();
                    String swimmer = cols.get(2).text().trim();
                    String nat = cols.get(3).text().trim();
                    if (nat.length() > 3) {
                        nat = nat.substring(0, 3).toUpperCase();
                    }
                    String dateText = cols.get(4).text().split("\\[")[0].trim();
                    
                    LocalDate date = null;
                    try {
                        // Wikipedia date is usually "1 August 2021" or "August 1, 2021"
                        date = parseWikipediaDate(dateText);
                    } catch (Exception dateEx) {
                        date = LocalDate.now().minusYears(1); // Default if parsing fails
                    }

                    RecordMondial rec = new RecordMondial();
                    rec.setEpreuve(event);
                    rec.setTemps(time);
                    rec.setNageur(swimmer);
                    rec.setNationalite(nat);
                    rec.setDate(date);
                    rec.setBassin(bassin);
                    rec.setSexe(sexe);
                    recordsList.add(rec);
                } catch (Exception e) {
                    // Skip rows that fail
                }
            }
        }
    }

    private LocalDate parseWikipediaDate(String text) {
        // Try standard format: "d MMMM yyyy"
        try {
            return LocalDate.parse(text, DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH));
        } catch (Exception e) {
            // Try another format: "MMMM d, yyyy"
            try {
                return LocalDate.parse(text, DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH));
            } catch (Exception e2) {
                // Parse manually if contains month names
                String lowercase = text.toLowerCase();
                String[] parts = text.split(" ");
                if (parts.length == 3) {
                    int day = Integer.parseInt(parts[0].replaceAll("\\D", ""));
                    int year = Integer.parseInt(parts[2].replaceAll("\\D", ""));
                    int month = 1;
                    if (lowercase.contains("jan")) month = 1;
                    else if (lowercase.contains("feb")) month = 2;
                    else if (lowercase.contains("mar")) month = 3;
                    else if (lowercase.contains("apr")) month = 4;
                    else if (lowercase.contains("may")) month = 5;
                    else if (lowercase.contains("jun")) month = 6;
                    else if (lowercase.contains("jul")) month = 7;
                    else if (lowercase.contains("aug")) month = 8;
                    else if (lowercase.contains("sep")) month = 9;
                    else if (lowercase.contains("oct")) month = 10;
                    else if (lowercase.contains("nov")) month = 11;
                    else if (lowercase.contains("dec")) month = 12;
                    return LocalDate.of(year, month, day);
                }
                throw new IllegalArgumentException("Unsupported date format: " + text);
            }
        }
    }

    private void loadFallbackRecords() {
        recordRepository.deleteAll();
        List<RecordMondial> list = new ArrayList<>();

        // Men's 50m Course Records
        list.add(createRecord("50m Nage Libre", "00:20.91", "Cesar Cielo", "BRA", LocalDate.of(2009, 12, 18), "50m", "H"));
        list.add(createRecord("100m Nage Libre", "00:46.40", "Pan Zhanle", "CHN", LocalDate.of(2024, 7, 31), "50m", "H"));
        list.add(createRecord("200m Nage Libre", "01:42.00", "Paul Biedermann", "GER", LocalDate.of(2009, 7, 28), "50m", "H"));
        list.add(createRecord("400m Nage Libre", "03:40.07", "Paul Biedermann", "GER", LocalDate.of(2009, 7, 26), "50m", "H"));
        list.add(createRecord("800m Nage Libre", "07:32.12", "Zhang Lin", "CHN", LocalDate.of(2009, 7, 29), "50m", "H"));
        list.add(createRecord("1500m Nage Libre", "14:30.67", "Bobby Finke", "USA", LocalDate.of(2024, 8, 4), "50m", "H"));
        list.add(createRecord("50m Dos", "00:23.55", "Kliment Kolesnikov", "RUS", LocalDate.of(2023, 7, 27), "50m", "H"));
        list.add(createRecord("100m Dos", "00:51.60", "Thomas Ceccon", "ITA", LocalDate.of(2022, 6, 20), "50m", "H"));
        list.add(createRecord("200m Dos", "01:51.92", "Aaron Peirsol", "USA", LocalDate.of(2009, 7, 31), "50m", "H"));
        list.add(createRecord("50m Brasse", "00:25.95", "Adam Peaty", "GBR", LocalDate.of(2017, 7, 25), "50m", "H"));
        list.add(createRecord("100m Brasse", "00:56.88", "Adam Peaty", "GBR", LocalDate.of(2019, 7, 21), "50m", "H"));
        list.add(createRecord("200m Brasse", "02:05.48", "Qin Haiyang", "CHN", LocalDate.of(2023, 7, 28), "50m", "H"));
        list.add(createRecord("50m Papillon", "00:22.27", "Andrii Govorov", "UKR", LocalDate.of(2018, 7, 1), "50m", "H"));
        list.add(createRecord("100m Papillon", "00:49.45", "Caeleb Dressel", "USA", LocalDate.of(2021, 7, 31), "50m", "H"));
        list.add(createRecord("200m Papillon", "01:50.34", "Kristof Milak", "HUN", LocalDate.of(2022, 6, 21), "50m", "H"));
        list.add(createRecord("200m 4-Nages", "01:54.00", "Ryan Lochte", "USA", LocalDate.of(2011, 7, 28), "50m", "H"));
        list.add(createRecord("400m 4-Nages", "04:02.50", "Léon Marchand", "FRA", LocalDate.of(2023, 7, 23), "50m", "H"));

        // Women's 50m Course Records
        list.add(createRecord("50m Nage Libre", "00:23.61", "Sarah Sjöström", "SWE", LocalDate.of(2023, 7, 29), "50m", "F"));
        list.add(createRecord("100m Nage Libre", "00:51.71", "Sarah Sjöström", "SWE", LocalDate.of(2017, 7, 23), "50m", "F"));
        list.add(createRecord("200m Nage Libre", "01:52.85", "Mollie O'Callaghan", "AUS", LocalDate.of(2023, 7, 26), "50m", "F"));
        list.add(createRecord("400m Nage Libre", "03:55.38", "Ariarne Titmus", "AUS", LocalDate.of(2023, 7, 23), "50m", "F"));
        list.add(createRecord("800m Nage Libre", "08:04.79", "Katie Ledecky", "USA", LocalDate.of(2016, 8, 12), "50m", "F"));
        list.add(createRecord("1500m Nage Libre", "15:20.48", "Katie Ledecky", "USA", LocalDate.of(2018, 5, 16), "50m", "F"));
        list.add(createRecord("50m Dos", "00:26.86", "Kaylee McKeown", "AUS", LocalDate.of(2023, 10, 20), "50m", "F"));
        list.add(createRecord("100m Dos", "00:57.13", "Regan Smith", "USA", LocalDate.of(2024, 6, 18), "50m", "F"));
        list.add(createRecord("200m Dos", "02:03.14", "Kaylee McKeown", "AUS", LocalDate.of(2023, 3, 10), "50m", "F"));
        list.add(createRecord("50m Brasse", "00:29.16", "Rūta Meilutytė", "LTU", LocalDate.of(2023, 7, 30), "50m", "F"));
        list.add(createRecord("100m Brasse", "01:04.13", "Lilly King", "USA", LocalDate.of(2017, 7, 25), "50m", "F"));
        list.add(createRecord("200m Brasse", "02:17.55", "Evgeniia Chikunova", "RUS", LocalDate.of(2023, 4, 21), "50m", "F"));
        list.add(createRecord("50m Papillon", "00:24.43", "Sarah Sjöström", "SWE", LocalDate.of(2014, 7, 5), "50m", "F"));
        list.add(createRecord("100m Papillon", "00:55.18", "Gretchen Walsh", "USA", LocalDate.of(2024, 6, 15), "50m", "F"));
        list.add(createRecord("200m Papillon", "02:01.81", "Liu Zige", "CHN", LocalDate.of(2009, 10, 21), "50m", "F"));
        list.add(createRecord("200m 4-Nages", "02:06.12", "Katinka Hosszú", "HUN", LocalDate.of(2015, 8, 3), "50m", "F"));
        list.add(createRecord("400m 4-Nages", "04:25.87", "Summer McIntosh", "CAN", LocalDate.of(2024, 5, 16), "50m", "F"));

        recordRepository.saveAll(list);
    }

    private RecordMondial createRecord(String epreuve, String temps, String nageur, String nat, LocalDate date, String bassin, String sexe) {
        RecordMondial rm = new RecordMondial();
        rm.setEpreuve(epreuve);
        rm.setTemps(temps);
        rm.setNageur(nageur);
        rm.setNationalite(nat);
        rm.setDate(date);
        rm.setBassin(bassin);
        rm.setSexe(sexe);
        return rm;
    }

    private void seedSwimmers() {
        List<NageurInternational> list = new ArrayList<>();

        NageurInternational n1 = new NageurInternational();
        n1.setNom("Léon Marchand");
        n1.setNationalite("FRA");
        n1.setPalmares("4x Champion Olympique (Paris 2024 - 400m 4N, 200m 4N, 200m Brasse, 200m Papillon), Multiple Champion du Monde");
        n1.setPhotoUrl("https://images.unsplash.com/photo-1530541930197-ff16ac917b0e?w=400");
        n1.setSpecialite("4-Nages, Brasse, Papillon");
        n1.setRecordsPersonnels("400m 4N: 4:02.50 (WR), 200m Brasse: 2:05.85, 200m Papillon: 1:51.21");
        list.add(n1);

        NageurInternational n2 = new NageurInternational();
        n2.setNom("Katie Ledecky");
        n2.setNationalite("USA");
        n2.setPalmares("9x Championne Olympique, 21x Championne du Monde");
        n2.setPhotoUrl("https://images.unsplash.com/photo-1519751138087-5bf79df62d5b?w=400");
        n2.setSpecialite("Nage Libre (800m, 1500m, 400m)");
        n2.setRecordsPersonnels("800m Libre: 8:04.79 (WR), 1500m Libre: 15:20.48 (WR), 400m Libre: 3:56.46");
        list.add(n2);

        NageurInternational n3 = new NageurInternational();
        n3.setNom("Sarah Sjöström");
        n3.setNationalite("SWE");
        n3.setPalmares("3x Championne Olympique, 14x Championne du Monde");
        n3.setPhotoUrl("https://images.unsplash.com/photo-1476527887737-a14f4477c777?w=400");
        n3.setSpecialite("Sprint (50m/100m Libre, 50m/100m Papillon)");
        n3.setRecordsPersonnels("50m Libre: 23.61 (WR), 100m Libre: 51.71 (WR), 50m Papillon: 24.43 (WR)");
        list.add(n3);

        NageurInternational n4 = new NageurInternational();
        n4.setNom("Pan Zhanle");
        n4.setNationalite("CHN");
        n4.setPalmares("Champion Olympique du 100m Libre (Paris 2024), Champion du Monde");
        n4.setPhotoUrl("https://images.unsplash.com/photo-1500333917452-4841274f1aa5?w=400");
        n4.setSpecialite("Sprint (Nage Libre)");
        n4.setRecordsPersonnels("100m Libre: 46.40 (WR), 50m Libre: 21.45");
        list.add(n4);

        swimmerRepository.saveAll(list);
    }

    private void seedCompetitions() {
        List<CompetitionInternationale> list = new ArrayList<>();

        CompetitionInternationale c1 = new CompetitionInternationale();
        c1.setNom("Jeux Olympiques de Paris 2024");
        c1.setLieu("Paris, France");
        c1.setDates("27 Juillet - 4 Août 2024");
        c1.setType("JEUX_OLYMPIQUES");
        c1.setResultatsPrincipaux("Léon Marchand remporte 4 médailles d'or individuelles. Pan Zhanle pulvérise le record du monde du 100m Nage Libre en 46.40. Bobby Finke établit un nouveau record du monde du 1500m Nage Libre (14:30.67).");
        list.add(c1);

        CompetitionInternationale c2 = new CompetitionInternationale();
        c2.setNom("Championnats du Monde de Natation - Doha 2024");
        c2.setLieu("Doha, Qatar");
        c2.setDates("11 - 18 Février 2024");
        c2.setType("CHAMPIONNAT_MONDE");
        c2.setResultatsPrincipaux("Pan Zhanle bat le record du monde du 100m Nage Libre lors du relais (46.80). L'Italie et les USA se partagent le haut du tableau dans de nombreuses épreuves.");
        list.add(c2);

        CompetitionInternationale c3 = new CompetitionInternationale();
        c3.setNom("Championnats du Monde de Natation - Fukuoka 2023");
        c3.setLieu("Fukuoka, Japon");
        c3.setDates("23 - 30 Juillet 2023");
        c3.setType("CHAMPIONNAT_MONDE");
        c3.setResultatsPrincipaux("Léon Marchand bat le record légendaire du 400m 4-Nages de Michael Phelps en 4:02.50. Qin Haiyang réalise le triplé en brasse (50m, 100m, 200m) avec record du monde sur le 200m (2:05.48).");
        list.add(c3);

        competitionRepository.saveAll(list);
    }
}
