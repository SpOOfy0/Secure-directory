package com.example.secure_directory.controller;

import com.example.secure_directory.entity.Utilisateur;
import com.example.secure_directory.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/profil")
public class ProfilController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping
    public ResponseEntity<?> afficherProfil(Principal principal) {
        String email = principal.getName();
        Utilisateur utilisateur = utilisateurService.findByEmail(email);
        return ResponseEntity.ok(utilisateur);
    }

    @PutMapping("/miseAJour")
    public ResponseEntity<?> mettreAJourProfil(
            Principal principal,
            @RequestBody Map<String, String> profilDto) {
        String email = principal.getName();
        Utilisateur utilisateur = utilisateurService.findByEmail(email);

        utilisateurService.mettreAJourProfil(
            utilisateur.getId(),
            profilDto.get("nom"),
            profilDto.get("prenom"),
            profilDto.get("motDePasse")
        );
        return ResponseEntity.ok(Map.of("message", "Profil mis à jour avec succès"));
    }

    @DeleteMapping("/cloturer")
    public ResponseEntity<?> cloturerCompte(Principal principal) {
        String email = principal.getName();
        utilisateurService.supprimerUtilisateurParEmail(email);
        return ResponseEntity.ok(Map.of("message", "Compte supprimé avec succès"));
    }
}
