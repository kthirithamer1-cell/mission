package com.projectmission.mapper;

import com.projectmission.model.Utilisateur;
import com.projectmission.dto.UtilisateurDTO;
import com.projectmission.model.Admin;
import com.projectmission.model.Club;
import com.projectmission.model.Entraineur;
import com.projectmission.model.Nageur;
import com.projectmission.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UtilisateurMapper {
    @Autowired
    private ClubRepository clubRepository;

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
        if (utilisateur instanceof Admin admin) {
            dto.setSuperAdmin(Boolean.TRUE.equals(admin.getSuperAdmin()));
            if (admin.getClub() != null) {
                dto.setClubId(admin.getClub().getId());
                dto.setClubNom(admin.getClub().getNom());
            }
        } else if (utilisateur instanceof Entraineur entraineur && entraineur.getClub() != null) {
            dto.setClubId(entraineur.getClub().getId());
            dto.setClubNom(entraineur.getClub().getNom());
        } else if (utilisateur instanceof Nageur nageur && nageur.getClub() != null) {
            dto.setClubId(nageur.getClub().getId());
            dto.setClubNom(nageur.getClub().getNom());
        }
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
        applyClubFields(utilisateur, dto);
        return utilisateur;
    }

    private void applyClubFields(Utilisateur utilisateur, UtilisateurDTO dto) {
        Club club = dto.getClubId() != null ? clubRepository.findById(dto.getClubId()).orElse(null) : null;
        if (utilisateur instanceof Admin admin) {
            admin.setSuperAdmin(Boolean.TRUE.equals(dto.getSuperAdmin()));
            admin.setClub(club);
        } else if (utilisateur instanceof Entraineur entraineur) {
            entraineur.setClub(club);
        } else if (utilisateur instanceof Nageur nageur) {
            nageur.setClub(club);
        }
    }
}
