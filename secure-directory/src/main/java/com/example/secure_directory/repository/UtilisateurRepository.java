package com.example.secure_directory.repository;

import com.example.secure_directory.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    // Recherche par email (utilisé pour l'authentification)
    Optional<Utilisateur> findByEmail(String email);

    // Recherche par nom ou email (utilisé dans l'annuaire)
    List<Utilisateur> findByNomContainingOrEmailContaining(String query, String queryAlt);

    // Liste des utilisateurs actifs
    List<Utilisateur> findByActifTrue();
}
    
