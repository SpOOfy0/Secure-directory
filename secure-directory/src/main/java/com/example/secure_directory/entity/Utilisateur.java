package com.example.secure_directory.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "utilisateur")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String motDePasse;

    private String nom;
    private String prenom;
    private boolean actif = true; // Indique si le compte est actif

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "utilisateur_role",
        joinColumns = @JoinColumn(name = "utilisateur_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // Getters et setters...


    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    // public Role getRole() {
    //     return role;
    // }

    // public void setRole(Role role) {
    //     this.role = role;
    // }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    // public LocalDateTime getDateInscription() {
    //     return dateInscription;
    // }

    // public void setDateInscription(LocalDateTime dateInscription) {
    //     this.dateInscription = dateInscription;
    // }

    // public LocalDateTime getDateDerniereConnexion() {
    //     return dateDerniereConnexion;
    // }

    // public void setDateDerniereConnexion(LocalDateTime dateDerniereConnexion) {
    //     this.dateDerniereConnexion = dateDerniereConnexion;
    // }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void ajouterRole(Role role) {
        this.roles.add(role);
    }
}
