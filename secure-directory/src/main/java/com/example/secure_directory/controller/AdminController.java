    package com.example.secure_directory.controller;

    import com.example.secure_directory.dto.UtilisateurDTO;
    import com.example.secure_directory.entity.Utilisateur;
    import com.example.secure_directory.service.UtilisateurService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import com.example.secure_directory.service.RoleService;
    import org.springframework.http.ResponseEntity;
    import com.example.secure_directory.entity.Role;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.security.core.Authentication;

    import java.util.List;
    import java.util.Map;
    import java.util.stream.Collectors;

    @RestController
    @RequestMapping("/api/admin")
    public class AdminController {

        @Autowired
        private UtilisateurService utilisateurService;
        @Autowired
        private PasswordEncoder passwordEncoder;
        


        @GetMapping("/utilisateurs")
        public ResponseEntity<List<UtilisateurDTO>> listerUtilisateurs() {
            List<UtilisateurDTO> utilisateurs = utilisateurService.findAll().stream()
                .map(UtilisateurDTO::new)  // Utilisation du DTO pour éviter la boucle infinie
                .collect(Collectors.toList());
            return ResponseEntity.ok(utilisateurs);
        }

        @Autowired
    private RoleService roleService;

    @PostMapping("/utilisateur/ajouter")
    public ResponseEntity<?> ajouterUtilisateur(@RequestBody Map<String, String> utilisateurDto) {
        try {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setEmail(utilisateurDto.get("email"));
            utilisateur.setMotDePasse(passwordEncoder.encode(utilisateurDto.get("motDePasse")));
            utilisateur.setNom(utilisateurDto.get("nom"));
            utilisateur.setPrenom(utilisateurDto.get("prenom"));

            // 🔹 Assigner le rôle à l'utilisateur
            String roleNom = utilisateurDto.get("role");
            Role role = roleService.findByNom(roleNom); // Récupère le rôle depuis la BDD
            utilisateur.ajouterRole(role);

            Utilisateur nouvelUtilisateur = utilisateurService.ajouterUtilisateur(utilisateur);
            return ResponseEntity.ok(Map.of("message", "Utilisateur ajouté avec succès", "id", nouvelUtilisateur.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Erreur lors de l'ajout de l'utilisateur : " + e.getMessage()));
        }
    }

    @GetMapping("/utilisateur/{id}")
    public ResponseEntity<?> getUtilisateur(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Non authentifié"));
        }

        Utilisateur utilisateur = utilisateurService.findById(id);
        if (utilisateur == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Utilisateur non trouvé"));
        }

        return ResponseEntity.ok(new UtilisateurDTO(utilisateur));
    }


        @PutMapping("/utilisateur/{id}")
        public ResponseEntity<?> modifierUtilisateur(
                @PathVariable Long id,
                @RequestBody Map<String, String> utilisateurDto) {
            try {
                System.out.println("🔹 Modification de l'utilisateur avec ID: " + id);
                Utilisateur utilisateur = utilisateurService.findById(id);
                if (utilisateur == null) {
                    return ResponseEntity.status(404).body(Map.of("error", "Utilisateur non trouvé"));
                }

                utilisateur.setEmail(utilisateurDto.get("email"));
                utilisateur.setNom(utilisateurDto.get("nom"));
                utilisateur.setPrenom(utilisateurDto.get("prenom"));

                if (utilisateurDto.containsKey("motDePasse") && utilisateurDto.get("motDePasse") != null && !utilisateurDto.get("motDePasse").isEmpty()) {
                    utilisateur.setMotDePasse(passwordEncoder.encode(utilisateurDto.get("motDePasse")));
                }

                // 🔹 Modifier le rôle uniquement s'il est fourni
                if (utilisateurDto.containsKey("role")) {
                    String roleNom = utilisateurDto.get("role");
                    Role role = roleService.findByNom(roleNom);
                    utilisateur.getRoles().clear(); // Supprime les anciens rôles
                    utilisateur.ajouterRole(role);
                }

                Utilisateur utilisateurMisAJour = utilisateurService.ajouterUtilisateur(utilisateur);
                return ResponseEntity.ok(Map.of("message", "Utilisateur modifié avec succès", "id", utilisateurMisAJour.getId()));

            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Erreur lors de la modification de l'utilisateur : " + e.getMessage()));
            }
        }

        @DeleteMapping("/utilisateur/{id}")
        public ResponseEntity<?> supprimerUtilisateur(@PathVariable Long id, Authentication authentication) {
            // Vérifier que l'authentification est valide
            if (authentication == null || authentication.getName() == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Non authentifié"));
            }

            // Récupérer l'utilisateur connecté
            Utilisateur adminUser = utilisateurService.findByEmail(authentication.getName());

            // Vérifier que l'utilisateur authentifié est ADMIN
            boolean isAdmin = adminUser.getRoles().stream()
                                .anyMatch(role -> "ADMIN".equals(role.getNom()));

            if (!isAdmin) {
                return ResponseEntity.status(403).body(Map.of("error", "Accès refusé. Vous n'avez pas les droits nécessaires."));
            }

            // Vérifier si l'utilisateur existe
            Utilisateur utilisateurASupprimer = utilisateurService.findById(id);
            if (utilisateurASupprimer == null) {
                return ResponseEntity.status(404).body(Map.of("error", "Utilisateur non trouvé"));
            }

            // Supprimer l'utilisateur
            utilisateurService.supprimerUtilisateur(id);

            return ResponseEntity.ok(Map.of("message", "Utilisateur supprimé avec succès"));
        }




    }
