package com.projectmission.mapper;

import com.projectmission.model.Utilisateur;
import com.projectmission.dto.UtilisateurDTO;
import org.springframework.stereotype.Component;

@Component
public class UtilisateurMapper {

    public UtilisateurDTO toDTO(Utilisateur utilisateur) {
        if (utilisateur == null) {
            return null;
        }
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setId(utilisateur.getId());
        dto.setNom(utilisateur.getNom());
        dto.setPrenom(utilisateur.getPrenom());
        dto.setEmail(utilisateur.getEmail());
        dto.setRole(utilisateur.getRole());
        return dto;
    }

    public Utilisateur toEntity(UtilisateurDTO dto) {
        if (dto == null) {
            return null;
        }
        Utilisateur utilisateur;
        String role = dto.getRole() != null ? dto.getRole().toUpperCase() : "";
        
        switch (role) {
            case "ADMIN":
                utilisateur = new com.projectmission.model.Admin();
                break;
            case "ENTRAINEUR":
                utilisateur = new com.projectmission.model.Entraineur();
                break;
            case "NAGEUR":
                utilisateur = new com.projectmission.model.Nageur();
                break;
            default:
                // Default to one of them or throw exception
                utilisateur = new com.projectmission.model.Nageur();
                utilisateur.setRole("NAGEUR");
                break;
        }

        utilisateur.setId(dto.getId());
        utilisateur.setNom(dto.getNom());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setEmail(dto.getEmail());
        if (dto.getRole() != null) {
            utilisateur.setRole(dto.getRole());
        }
        return utilisateur;
    }
}

