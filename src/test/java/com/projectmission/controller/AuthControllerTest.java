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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UtilisateurService utilisateurService;

    @MockBean
    private JwtUtil jwtUtil;

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
    @WithMockUser
    void testLoginSuccess() throws Exception {
        UtilisateurDTO loginDto = new UtilisateurDTO();
        loginDto.setEmail("john.doe@example.com");
        loginDto.setMotDePasse("password123");

        when(utilisateurService.authenticate("john.doe@example.com", "password123")).thenReturn(testUser);
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("mock-token");

        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-token"))
                .andExpect(jsonPath("$.utilisateur.email").value("john.doe@example.com"));
    }

    @Test
    @WithMockUser
    void testLoginFailure() throws Exception {
        UtilisateurDTO loginDto = new UtilisateurDTO();
        loginDto.setEmail("wrong@example.com");
        loginDto.setMotDePasse("wrong");

        when(utilisateurService.authenticate("wrong@example.com", "wrong")).thenReturn(null);

        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    @Test
    @WithMockUser
    void testLoginEmailNotVerified() throws Exception {
        UtilisateurDTO loginDto = new UtilisateurDTO();
        loginDto.setEmail("john.doe@example.com");
        loginDto.setMotDePasse("password123");

        when(utilisateurService.authenticate("john.doe@example.com", "password123"))
                .thenThrow(new com.projectmission.exception.EmailNotVerifiedException(
                        "Email non vérifié. Consultez votre boîte mail."
                ));

        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Email non vérifié. Consultez votre boîte mail."));
    }

    @Test
    @WithMockUser
    void testVerifyEmailSuccess() throws Exception {
        doNothing().when(utilisateurService).verifyEmail("valid-token");

        mockMvc.perform(get("/api/auth/verify-email")
                .param("token", "valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @WithMockUser
    void testRegisterSuccess() throws Exception {
        AuthController.RegisterRequest request = new AuthController.RegisterRequest();
        request.setUtilisateurDTO(testUser);
        request.setUserType("ADMIN");

        when(utilisateurService.getByEmail(anyString())).thenReturn(null);
        when(utilisateurService.create(any(UtilisateurDTO.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.utilisateur.email").value("john.doe@example.com"));
    }

    @Test
    @WithMockUser
    void testRegisterEmailAlreadyExists() throws Exception {
        AuthController.RegisterRequest request = new AuthController.RegisterRequest();
        request.setUtilisateurDTO(testUser);
        request.setUserType("ADMIN");

        when(utilisateurService.getByEmail("john.doe@example.com")).thenReturn(testUser);

        mockMvc.perform(post("/api/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email already exists"));
    }
}
