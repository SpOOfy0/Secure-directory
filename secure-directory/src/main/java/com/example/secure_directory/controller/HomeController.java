package com.example.secure_directory.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> afficherAccueil(Authentication authentication) {
        System.out.println("🏠 Accueillllllllllllllllllllllllllllllllllllllllllll");
        // Détermine le rôle principal
        String role = getPrincipalRole(authentication);

        // Vérifie les rôles pour l'affichage conditionnel
        boolean isAdmin = role.equals("ADMIN");
        boolean isUser = role.equals("USER");

        // Crée une réponse sous forme de JSON
        Map<String, Object> response = new HashMap<>();
        response.put("role", role); // Rôle principal
        response.put("isAdmin", isAdmin);
        response.put("isUser", isUser);

        return ResponseEntity.ok(response); // Retourne la réponse JSON
    }

    /**
     * Récupère le rôle principal de l'utilisateur authentifié.
     */
    private String getPrincipalRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority) // Récupère le rôle complet (e.g., ROLE_ADMIN)
                .map(role -> role.startsWith("ROLE_") ? role.substring(5) : role) // Supprime le préfixe ROLE_
                .filter(role -> role.equals("ADMIN") || role.equals("USER")) // Filtre uniquement ADMIN ou USER
                .findFirst() // Retourne le premier rôle trouvé
                .orElse("UNKNOWN"); // Rôle inconnu si aucun n'est trouvé
    }
}
