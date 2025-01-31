package com.example.secure_directory.controller;

import com.example.secure_directory.entity.Utilisateur;
import com.example.secure_directory.security.JwtTokenProvider;
import com.example.secure_directory.service.UtilisateurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest {

    @Mock
    private UtilisateurService utilisateurService;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    private Utilisateur utilisateur;

    @BeforeEach
    void setUp() {
        utilisateur = new Utilisateur();
        utilisateur.setEmail("admin@gmail.com");
        utilisateur.setMotDePasse("admin");
    }

    @Test
    void testLogin_Success() throws Exception {
        // Simulation de l'utilisateur trouvé en BDD
        Mockito.when(utilisateurService.findByEmail(anyString())).thenReturn(utilisateur);
        Mockito.when(passwordEncoder.matches("admin", utilisateur.getMotDePasse())).thenReturn(true);
        Mockito.when(tokenProvider.generateToken(anyString())).thenReturn("fake-jwt-token");

        String loginRequest = "{ \"username\": \"admin@gmail.com\", \"password\": \"admin\" }";

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.message").value("Connexion réussie"));
    }
}
