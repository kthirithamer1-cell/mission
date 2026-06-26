package com.projectmission.dto;

import java.util.ArrayList;
import java.util.List;

public class CalendarEventDTO {
    private Long id;
    private String titre;
    private String date;
    private String heureDebut;
    private String heureFin;
    private String description;
    private Long clubId;
    private String clubNom;
    private Long entraineurId;
    private String entraineurNom;
    private Long reservationId;
    private String piscineNom;
    private String couloirsLabel;

    // Coach context
    private Integer studentsTotal;
    private Integer presentCount;
    private Integer absentCount;
    private Integer justifieCount;
    private Double attendanceRate;
    private List<String> studentNames = new ArrayList<>();

    // Nageur context
    private String myPresenceStatus;
    private boolean relevantToMe;
    private String categorie;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getHeureDebut() { return heureDebut; }
    public void setHeureDebut(String heureDebut) { this.heureDebut = heureDebut; }
    public String getHeureFin() { return heureFin; }
    public void setHeureFin(String heureFin) { this.heureFin = heureFin; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getClubId() { return clubId; }
    public void setClubId(Long clubId) { this.clubId = clubId; }
    public String getClubNom() { return clubNom; }
    public void setClubNom(String clubNom) { this.clubNom = clubNom; }
    public Long getEntraineurId() { return entraineurId; }
    public void setEntraineurId(Long entraineurId) { this.entraineurId = entraineurId; }
    public String getEntraineurNom() { return entraineurNom; }
    public void setEntraineurNom(String entraineurNom) { this.entraineurNom = entraineurNom; }
    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }
    public String getPiscineNom() { return piscineNom; }
    public void setPiscineNom(String piscineNom) { this.piscineNom = piscineNom; }
    public String getCouloirsLabel() { return couloirsLabel; }
    public void setCouloirsLabel(String couloirsLabel) { this.couloirsLabel = couloirsLabel; }
    public Integer getStudentsTotal() { return studentsTotal; }
    public void setStudentsTotal(Integer studentsTotal) { this.studentsTotal = studentsTotal; }
    public Integer getPresentCount() { return presentCount; }
    public void setPresentCount(Integer presentCount) { this.presentCount = presentCount; }
    public Integer getAbsentCount() { return absentCount; }
    public void setAbsentCount(Integer absentCount) { this.absentCount = absentCount; }
    public Integer getJustifieCount() { return justifieCount; }
    public void setJustifieCount(Integer justifieCount) { this.justifieCount = justifieCount; }
    public Double getAttendanceRate() { return attendanceRate; }
    public void setAttendanceRate(Double attendanceRate) { this.attendanceRate = attendanceRate; }
    public List<String> getStudentNames() { return studentNames; }
    public void setStudentNames(List<String> studentNames) { this.studentNames = studentNames; }
    public String getMyPresenceStatus() { return myPresenceStatus; }
    public void setMyPresenceStatus(String myPresenceStatus) { this.myPresenceStatus = myPresenceStatus; }
    public boolean isRelevantToMe() { return relevantToMe; }
    public void setRelevantToMe(boolean relevantToMe) { this.relevantToMe = relevantToMe; }
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
}
