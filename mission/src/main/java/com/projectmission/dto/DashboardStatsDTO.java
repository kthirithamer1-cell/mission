package com.projectmission.dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DashboardStatsDTO {
    private long nageursCount;
    private long entraineursCount;
    private long couloirsReserves;
    private long creneauxAVenir;
    private long reservationsEnAttente;
    private long piscinesCount;
    private long clubsCount;
    private Map<String, Long> repartitionCategories = new LinkedHashMap<>();
    private Map<String, List<Long>> evolutionParCategorie = new LinkedHashMap<>();
    private List<RecordDTO> records;

    public static class RecordDTO {
        private String epreuve;
        private String temps;
        private String nageurNom;
        private String categorie;

        public RecordDTO() {}
        public RecordDTO(String epreuve, String temps, String nageurNom, String categorie) {
            this.epreuve = epreuve;
            this.temps = temps;
            this.nageurNom = nageurNom;
            this.categorie = categorie;
        }

        public String getEpreuve() { return epreuve; }
        public void setEpreuve(String epreuve) { this.epreuve = epreuve; }
        public String getTemps() { return temps; }
        public void setTemps(String temps) { this.temps = temps; }
        public String getNageurNom() { return nageurNom; }
        public void setNageurNom(String nageurNom) { this.nageurNom = nageurNom; }
        public String getCategorie() { return categorie; }
        public void setCategorie(String categorie) { this.categorie = categorie; }
    }

    public long getNageursCount() { return nageursCount; }
    public void setNageursCount(long nageursCount) { this.nageursCount = nageursCount; }
    public long getEntraineursCount() { return entraineursCount; }
    public void setEntraineursCount(long entraineursCount) { this.entraineursCount = entraineursCount; }
    public long getCouloirsReserves() { return couloirsReserves; }
    public void setCouloirsReserves(long couloirsReserves) { this.couloirsReserves = couloirsReserves; }
    public long getCreneauxAVenir() { return creneauxAVenir; }
    public void setCreneauxAVenir(long creneauxAVenir) { this.creneauxAVenir = creneauxAVenir; }
    public long getReservationsEnAttente() { return reservationsEnAttente; }
    public void setReservationsEnAttente(long reservationsEnAttente) { this.reservationsEnAttente = reservationsEnAttente; }
    public long getPiscinesCount() { return piscinesCount; }
    public void setPiscinesCount(long piscinesCount) { this.piscinesCount = piscinesCount; }
    public long getClubsCount() { return clubsCount; }
    public void setClubsCount(long clubsCount) { this.clubsCount = clubsCount; }
    public Map<String, Long> getRepartitionCategories() { return repartitionCategories; }
    public void setRepartitionCategories(Map<String, Long> repartitionCategories) { this.repartitionCategories = repartitionCategories; }
    public Map<String, List<Long>> getEvolutionParCategorie() { return evolutionParCategorie; }
    public void setEvolutionParCategorie(Map<String, List<Long>> evolutionParCategorie) { this.evolutionParCategorie = evolutionParCategorie; }
    public List<RecordDTO> getRecords() { return records; }
    public void setRecords(List<RecordDTO> records) { this.records = records; }
}