package com.projectmission.dto;

public class ResultatDTO {
    private Long id;
    private String temps;
    private Integer classement;
    private Long nageurId;
    private Long epreuveId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTemps() { return temps; }
    public void setTemps(String temps) { this.temps = temps; }
    public Integer getClassement() { return classement; }
    public void setClassement(Integer classement) { this.classement = classement; }
    public Long getNageurId() { return nageurId; }
    public void setNageurId(Long nageurId) { this.nageurId = nageurId; }
    public Long getEpreuveId() { return epreuveId; }
    public void setEpreuveId(Long epreuveId) { this.epreuveId = epreuveId; }
}
