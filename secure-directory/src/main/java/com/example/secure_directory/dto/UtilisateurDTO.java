package com.example.secure_directory.dto;

import com.example.secure_directory.entity.Utilisateur;

import java.util.List;
import java.util.stream.Collectors;

public class UtilisateurDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private List<String> roles;

    public UtilisateurDTO(Utilisateur utilisateur) {
        this.id = utilisateur.getId();
        this.nom = utilisateur.getNom();
        this.prenom = utilisateur.getPrenom();
        this.email = utilisateur.getEmail();
        this.roles = utilisateur.getRoles().stream()
            .map(role -> role.getNom())  // On ne garde que le nom du rôle
            .collect(Collectors.toList());
    }

    // Getters et setters
    public Long getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public List<String> getRoles() { return roles; }
}
