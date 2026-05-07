package model;

public class Resultat {
    private String classement;
    private String score;
    private Competition competition;
    private Equipe equipe;

    public Resultat() {}

    public String getClassement() { return classement; }
    public void setClassement(String classement) { this.classement = classement; }
    public String getScore() { return score; }
    public void setScore(String score) { this.score = score; }
    public Competition getCompetition() { return competition; }
    public void setCompetition(Competition competition) { this.competition = competition; }
    public Equipe getEquipe() { return equipe; }
    public void setEquipe(Equipe equipe) { this.equipe = equipe; }
}

