package com.projectmission.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectmission.dto.UtilisateurDTO;
import com.projectmission.security.JwtUtil;
import com.projectmission.service.UtilisateurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.context.annotation.Import;
import com.projectmission.security.SecurityConfig;

@WebMvcTest(UtilisateurController.class)
@Import(SecurityConfig.class)
public class UtilisateurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UtilisateurService utilisateurService;

    @MockBean
    private JwtUtil jwtUtil; // Required for security context if JwtFilter is involved

    @Autowired
    private ObjectMapper objectMapper;

    private UtilisateurDTO testUser;

    @BeforeEach
    void setUp() {
        testUser = new UtilisateurDTO();
        testUser.setId(1L);
        testUser.setNom("John");
        testUser.setPrenom("Doe");
        testUser.setEmail("john.doe@example.com");
        testUser.setRole("ADMIN");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAll() throws Exception {
        List<UtilisateurDTO> users = Arrays.asList(testUser);
        when(utilisateurService.getAll()).thenReturn(users);

        mockMvc.perform(get("/api/utilisateurs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetByIdSuccess() throws Exception {
        when(utilisateurService.getById(1L)).thenReturn(testUser);

        mockMvc.perform(get("/api/utilisateurs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetByIdNotFound() throws Exception {
        when(utilisateurService.getById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/utilisateurs/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateSuccess() throws Exception {
        when(utilisateurService.update(eq(1L), any(UtilisateurDTO.class))).thenReturn(testUser);

        mockMvc.perform(put("/api/utilisateurs/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteSuccess() throws Exception {
        doNothing().when(utilisateurService).delete(1L);

        mockMvc.perform(delete("/api/utilisateurs/1")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Utilisateur deleted successfully"));
    }

    @Test
    @WithMockUser(roles = "USER") // Role not in (ADMIN, NAGEUR, ENTRAINEUR)
    void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/utilisateurs"))
                .andExpect(status().isForbidden());
    }
}