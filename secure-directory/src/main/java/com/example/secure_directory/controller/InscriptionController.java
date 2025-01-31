package com.example.secure_directory.controller;

import com.example.secure_directory.entity.Utilisateur;
import com.example.secure_directory.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inscription")
public class InscriptionController {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<?> inscrireUtilisateur(
            @RequestParam String email,
            @RequestParam String motDePasse,
            @RequestParam String prenom,
            @RequestParam String nom
    ) {
        // Vérifiez si l'utilisateur existe déjà
        if (utilisateurService.existeParEmail(email)) {
            return ResponseEntity.badRequest().body("Un compte avec cet email existe déjà !");
        }

        // Créer un nouvel utilisateur avec les informations fournies
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(email);
        utilisateur.setMotDePasse(passwordEncoder.encode(motDePasse));
        utilisateur.setPrenom(prenom);
        utilisateur.setNom(nom);

        // Ajouter l'utilisateur
        Utilisateur nouvelUtilisateur = utilisateurService.ajouterUtilisateur(utilisateur);

        // Retourner l'utilisateur créé
        return ResponseEntity.ok(nouvelUtilisateur);
    }
}
