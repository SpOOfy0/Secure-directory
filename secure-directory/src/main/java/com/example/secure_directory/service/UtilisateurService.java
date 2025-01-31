package com.example.secure_directory.service;

import com.example.secure_directory.entity.Role;
import com.example.secure_directory.entity.Utilisateur;
import com.example.secure_directory.repository.RoleRepository;
import com.example.secure_directory.repository.UtilisateurRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor Injection
    public UtilisateurService(UtilisateurRepository utilisateurRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authentifier(String email, String password) {
        return utilisateurRepository.findByEmail(email)
                .map(utilisateur -> {
                    System.out.println("🔍 Utilisateur trouvé : " + utilisateur.getEmail());
                    System.out.println("🗝️ Mot de passe en base : " + utilisateur.getMotDePasse());
                    System.out.println("🔑 Mot de passe fourni : " + password);

                    boolean isMatch = passwordEncoder.matches(password, utilisateur.getMotDePasse());
                    System.out.println("✅ Mot de passe correct ? " + isMatch);
                    return isMatch;
                })
                .orElseGet(() -> {
                    System.out.println("❌ Utilisateur non trouvé pour l'email : " + email);
                    return false;
                });
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Tentative de connexion avec l'email : " + email);

        return utilisateurRepository.findByEmail(email)
                .map(utilisateur -> {
                    System.out.println("Utilisateur trouvé : " + utilisateur.getEmail());
                    return User.builder()
                            .username(utilisateur.getEmail())
                            .password(utilisateur.getMotDePasse())
                            .roles(utilisateur.getRoles().stream()
                                    .map(Role::getNom)
                                    .toArray(String[]::new))
                            .build();
                })
                .orElseThrow(() -> {
                    System.out.println("Utilisateur non trouvé pour l'email : " + email);
                    return new UsernameNotFoundException("Utilisateur non trouvé");
                });
    }

    // Méthode pour sauvegarder un utilisateur (création ou mise à jour)
    public Utilisateur save(Utilisateur utilisateur) {
        // Vous pouvez ajouter des validations ici (par exemple vérifier si l'email est unique)
        // utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        Optional<Utilisateur> existingUser = utilisateurRepository.findByEmail(utilisateur.getEmail());
        if (existingUser.isPresent()) {
            return existingUser.get();  // Retourner l'existant au lieu d'insérer un doublon
        }
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur findById(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID : " + id));
    }

    public Utilisateur findByEmail(String email) {
        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'email : " + email));
    }

    // Ajouter un utilisateur
    public Utilisateur ajouterUtilisateur(Utilisateur utilisateur) {
        // Encodage du mot de passe avant la sauvegarde
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        utilisateur.setActif(true); // Définit l'utilisateur comme actif par défaut

        // Attribution d'un rôle par défaut (USER) si aucun rôle n'est fourni
        if (utilisateur.getRoles().isEmpty()) {
            Role roleUser = roleRepository.findByNom("USER")
                    .orElseThrow(() -> new RuntimeException("Rôle USER introuvable"));
            utilisateur.getRoles().add(roleUser);
        }

        return utilisateurRepository.save(utilisateur);
    }

    // Lister tous les utilisateurs
    public List<Utilisateur> findAll() {
        return utilisateurRepository.findAll();
    }

    // Lister uniquement les utilisateurs actifs
    public List<Utilisateur> findAllActifs() {
        return utilisateurRepository.findByActifTrue();
    }

    public boolean existeParEmail(String email) {
        return utilisateurRepository.findByEmail(email).isPresent();
    }

    // Rechercher des utilisateurs par nom ou email
    public List<Utilisateur> rechercherUtilisateurs(String query) {
        return utilisateurRepository.findByNomContainingOrEmailContaining(query, query);
    }

    // Modifier les rôles d'un utilisateur
    public void modifierRole(Long id, String role) {
        if (role == null || role.isEmpty()) {
            throw new IllegalArgumentException("Le rôle ne peut pas être vide.");
        }

        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID : " + id));

        Role nouveauRole = roleRepository.findByNom(role.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Rôle introuvable : ROLE_" + role.toUpperCase()));

        utilisateur.getRoles().clear(); // Supprime les anciens rôles
        utilisateur.getRoles().add(nouveauRole);
        utilisateurRepository.save(utilisateur);

        System.out.println("Le rôle de l'utilisateur avec l'ID " + id + " a été mis à jour vers " + role);
    }

    // Changer le mot de passe d'un utilisateur
    public void changerMotDePasse(Long id, String nouveauMotDePasse) {
        if (nouveauMotDePasse == null || nouveauMotDePasse.isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être vide.");
        }

        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID : " + id));

        utilisateur.setMotDePasse(passwordEncoder.encode(nouveauMotDePasse)); // Encodage du nouveau mot de passe
        utilisateurRepository.save(utilisateur);

        System.out.println("Mot de passe mis à jour pour l'utilisateur avec l'ID : " + id);
    }

    // Supprimer un utilisateur
    public void supprimerUtilisateur(Long id) {
        if (!utilisateurRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur introuvable avec l'ID : " + id);
        }
        utilisateurRepository.deleteById(id);
        System.out.println("Utilisateur supprimé avec l'ID : " + id);
    }

    public Utilisateur modifierUtilisateur(Long id, String email, String nom, String prenom, String motDePasse, String role) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID : " + id));

        // Modifier les champs si des valeurs sont fournies
        if (email != null && !email.isEmpty()) {
            utilisateur.setEmail(email);
        }
        if (nom != null && !nom.isEmpty()) {
            utilisateur.setNom(nom);
        }
        if (prenom != null && !prenom.isEmpty()) {
            utilisateur.setPrenom(prenom);
        }
        if (motDePasse != null && !motDePasse.isEmpty()) {
            utilisateur.setMotDePasse(passwordEncoder.encode(motDePasse));
        }

        // Modifier le rôle si un rôle est fourni
        if (role != null && !role.isEmpty()) {
            Role nouveauRole = roleRepository.findByNom(role.toUpperCase())
                    .orElseThrow(() -> new RuntimeException("Rôle introuvable : " + role));
            utilisateur.getRoles().clear(); // Supprime les anciens rôles
            utilisateur.getRoles().add(nouveauRole); // Ajoute le nouveau rôle
        }

        // Sauvegarder les modifications
        utilisateurRepository.save(utilisateur);
        System.out.println("Les données de l'utilisateur avec l'ID " + id + " ont été mises à jour.");
        return utilisateurRepository.save(utilisateur);
    }

    public void mettreAJourProfil(Long id, String nom, String prenom, String nouveauMotDePasse) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID : " + id));

        if (nom != null && !nom.isEmpty()) {
            utilisateur.setNom(nom);
        }
        if (prenom != null && !prenom.isEmpty()) {
            utilisateur.setPrenom(prenom);
        }
        if (nouveauMotDePasse != null && !nouveauMotDePasse.isEmpty()) {
            utilisateur.setMotDePasse(passwordEncoder.encode(nouveauMotDePasse));
        }

        utilisateurRepository.save(utilisateur);
        System.out.println("Profil mis à jour pour l'utilisateur : " + utilisateur.getEmail());
    }

    public void supprimerUtilisateurParEmail(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'email : " + email));

        utilisateurRepository.delete(utilisateur); // Suppression complète de l'utilisateur

        System.out.println("Compte supprimé pour l'utilisateur : " + email);
    }

}
