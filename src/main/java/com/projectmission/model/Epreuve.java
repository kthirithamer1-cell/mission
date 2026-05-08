package com.projectmission.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "epreuve")
public class Epreuve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer distance;
    private String style;
    private String categorie;

    @ManyToOne
    @JoinColumn(name = "competition_id")
    private Competition competition;

    @OneToMany(mappedBy = "epreuve")
    private List<Resultat> resultats = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getDistance() { return distance; }
    public void setDistance(Integer distance) { this.distance = distance; }
    public String getStyle() { return style; }
    public void setStyle(String style) { this.style = style; }
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public Competition getCompetition() { return competition; }
    public void setCompetition(Competition competition) { this.competition = competition; }
    public List<Resultat> getResultats() { return resultats; }
    public void setResultats(List<Resultat> resultats) { this.resultats = resultats; }
}
