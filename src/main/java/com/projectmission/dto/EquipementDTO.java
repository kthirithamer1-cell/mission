package com.projectmission.dto;

public class EquipementDTO {
    private Long id;
    private String type;
    private Long nageurId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Long getNageurId() { return nageurId; }
    public void setNageurId(Long nageurId) { this.nageurId = nageurId; }
}
