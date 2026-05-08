package com.projectmission.dto;

public class EpreuveDTO {
    private Long id;
    private Integer distance;
    private String style;
    private String categorie;
    private Long competitionId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getDistance() { return distance; }
    public void setDistance(Integer distance) { this.distance = distance; }
    public String getStyle() { return style; }
    public void setStyle(String style) { this.style = style; }
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public Long getCompetitionId() { return competitionId; }
    public void setCompetitionId(Long competitionId) { this.competitionId = competitionId; }
}
