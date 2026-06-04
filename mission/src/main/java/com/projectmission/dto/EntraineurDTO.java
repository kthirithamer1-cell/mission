package com.projectmission.dto;

public class EntraineurDTO extends UtilisateurDTO {
    private Long clubId;
    private String clubNom;
    private String groupes;

    public Long getClubId() { return clubId; }
    public void setClubId(Long clubId) { this.clubId = clubId; }
    public String getClubNom() { return clubNom; }
    public void setClubNom(String clubNom) { this.clubNom = clubNom; }
    public String getGroupes() { return groupes; }
    public void setGroupes(String groupes) { this.groupes = groupes; }
}