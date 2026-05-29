package com.projectmission.security;

import com.projectmission.dto.UtilisateurDTO;
import com.projectmission.mapper.UtilisateurMapper;
import com.projectmission.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private UtilisateurMapper utilisateurMapper;

    public String getEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) return null;
        return auth.getPrincipal().toString();
    }

    public UtilisateurDTO getCurrentUser() {
        String email = getEmail();
        if (email == null) return null;
        return utilisateurRepository.findByEmail(email)
                .map(utilisateurMapper::toDTO)
                .orElse(null);
    }

    public boolean isSuperAdmin() {
        UtilisateurDTO user = getCurrentUser();
        return user != null && ("SUPER_ADMIN".equals(user.getRole()) || Boolean.TRUE.equals(user.getSuperAdmin()));
    }

    public Long getClubId() {
        UtilisateurDTO user = getCurrentUser();
        return user != null ? user.getClubId() : null;
    }

    public String getRole() {
        UtilisateurDTO user = getCurrentUser();
        return user != null ? user.getRole() : null;
    }
}
