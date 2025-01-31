    package com.example.secure_directory.entity;

    import jakarta.persistence.*;
    import java.util.HashSet;
    import java.util.Set;

    @Entity
    @Table(name = "role")
    public class Role {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true)
        private String nom; // Nom du rôle (ex : ADMIN, USER)

        @ManyToMany(mappedBy = "roles")
        private Set<Utilisateur> utilisateurs = new HashSet<>();

        // Constructeur, Getters et Setters
        public Role() {}

        public Role(String nom) {
            this.nom = nom;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public Set<Utilisateur> getUtilisateurs() {
            return utilisateurs;
        }

        public void setUtilisateurs(Set<Utilisateur> utilisateurs) {
            this.utilisateurs = utilisateurs;
        }
    }