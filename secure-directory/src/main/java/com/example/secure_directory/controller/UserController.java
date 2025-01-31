package com.example.secure_directory.controller;

import com.example.secure_directory.entity.Utilisateur;
import com.example.secure_directory.service.UtilisateurService;
import com.example.secure_directory.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UtilisateurService utilisateurService;

    // Lister les utilisateurs avec transformation des rôles
    @GetMapping("/utilisateurs")
    public ResponseEntity<?> listerUtilisateurs(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Accès non autorisé. Veuillez vous authentifier."));
        }

        System.out.println("🔍 Token reçu : " + authHeader);

        List<Utilisateur> utilisateurs = utilisateurService.findAll();
        
        // 🔹 Ajout de logs pour voir les données récupérées
        System.out.println("📡 Utilisateurs trouvés : " + utilisateurs.size());

        // 🔹 Vérifier si certains utilisateurs ont des valeurs null
        for (Utilisateur utilisateur : utilisateurs) {
            System.out.println("👤 Utilisateur ID: " + utilisateur.getId() + ", Email: " + utilisateur.getEmail());

            if (utilisateur.getEmail() == null || utilisateur.getNom() == null || utilisateur.getPrenom() == null) {
                System.out.println("⚠️ Problème détecté: Un utilisateur a des valeurs null !");
            }

            for (Role role : utilisateur.getRoles()) {
                System.out.println("   🔹 Rôle: " + role.getNom());
            }
        }

        // Transformation des utilisateurs pour éviter les valeurs null et la récursivité
        List<Map<String, Object>> utilisateursTransformes = utilisateurs.stream()
            .map(utilisateur -> {
                return Map.of(
                    "id", utilisateur.getId() != null ? utilisateur.getId() : -1,  // Si null, mettre une valeur par défaut
                    "email", utilisateur.getEmail() != null ? utilisateur.getEmail() : "Email inconnu",
                    "nom", utilisateur.getNom() != null ? utilisateur.getNom() : "Nom inconnu",
                    "prenom", utilisateur.getPrenom() != null ? utilisateur.getPrenom() : "Prénom inconnu",
                    "actif", utilisateur.isActif(),
                    "roles", utilisateur.getRoles() != null ? utilisateur.getRoles().stream().map(Role::getNom).collect(Collectors.toList()) : List.of("Aucun rôle")
                );
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(utilisateursTransformes);
    }



}
