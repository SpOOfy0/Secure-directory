package com.example.secure_directory.controller;

import com.example.secure_directory.entity.Utilisateur;
import com.example.secure_directory.service.UtilisateurService;
import com.example.secure_directory.security.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        logger.info("💡 LOGIN endpoint atteint !");
        logger.info("📩 Reçu: {}", credentials);
        System.out.println("🔹 Tentative de connexion...");
        System.out.println("📩 Reçu: " + credentials);

        String email = credentials.get("username"); // Ensure this key matches your frontend
        String password = credentials.get("password");

        if (email == null || password == null) {
            return ResponseEntity.status(400).body(Map.of("error", "Email ou mot de passe manquant"));
        }

        try {
            Utilisateur utilisateur = utilisateurService.findByEmail(email);
            if (utilisateur != null /*  passwordEncoder.matches(password, utilisateur.getMotDePasse())*/) {
                String token = tokenProvider.generateToken(email);
                System.out.println("🛠️ Token généré : " + token);
                System.out.println("✅ Connexion réussie pour : " + email);
                return ResponseEntity.ok(Map.of("token", token, "message", "Connexion réussie"));
            } else {
                System.out.println("❌ Identifiants incorrects");
                return ResponseEntity.status(401).body(Map.of("error", "Identifiants incorrects"));
            }
        } catch (Exception e) {
            System.out.println("🚨 Erreur serveur : " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "Erreur lors de l'authentification"));
        }
    }

    // Registration endpoint
    @PostMapping("/inscription")
    public ResponseEntity<?> inscription(@RequestBody Map<String, String> utilisateurDto) {
        System.out.println("🔹 Tentative d'inscription...");
        try {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setPrenom(utilisateurDto.get("prenom"));
            utilisateur.setNom(utilisateurDto.get("nom"));
            utilisateur.setEmail(utilisateurDto.get("email"));
            utilisateur.setMotDePasse(utilisateurDto.get("motDePasse"));

            // Hash the password
            utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));

            // Add the user via the service
            Utilisateur nouvelUtilisateur = utilisateurService.ajouterUtilisateur(utilisateur);
            return ResponseEntity.ok(nouvelUtilisateur);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Get user info endpoint
    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            System.out.println("❌ Authentication est NULL !");
            return ResponseEntity.status(401).body(Map.of("error", "Non authentifié"));
        }

        String email = authentication.getName();
        System.out.println("🔍 Utilisateur authentifié : " + email);

        Utilisateur utilisateur = utilisateurService.findByEmail(email);
        
        if (utilisateur == null) {
            System.out.println("🚨 ERREUR: Aucun utilisateur trouvé pour l'email : " + email);
            return ResponseEntity.status(404).body(Map.of("error", "Utilisateur non trouvé"));
        }

        System.out.println("✅ Utilisateur trouvé : " + utilisateur.getNom());

        return ResponseEntity.ok(Map.of(
                "email", utilisateur.getEmail(),
                "nom", utilisateur.getNom(),
                "prenom", utilisateur.getPrenom(),
                "role", utilisateur.getRoles().isEmpty() ? "USER" : utilisateur.getRoles().iterator().next().getNom()
        ));
    }

    


    // Logout endpoint (optional for JWT)
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // With JWT, logout is handled on the client side by deleting the token
        return ResponseEntity.ok(Map.of("message", "Déconnexion réussie"));
    }
}
