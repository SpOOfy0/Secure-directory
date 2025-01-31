package com.example.secure_directory;

import com.example.secure_directory.entity.Role;
import com.example.secure_directory.entity.Utilisateur;
import com.example.secure_directory.service.RoleService;
import com.example.secure_directory.service.UtilisateurService;
import com.example.secure_directory.repository.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

@SpringBootApplication
@EntityScan("com.example.secure_directory.entity")
public class SecureDirectoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecureDirectoryApplication.class, args);
    }

    @Bean
    public CommandLineRunner initAdminAccount(UtilisateurService utilisateurService, RoleService roleService, PasswordEncoder passwordEncoder, UtilisateurRepository utilisateurRepository) {
        return args -> {
            // Vérifier si le rôle ADMIN existe, sinon le créer
            Role adminRole = roleService.findByNom("ADMIN");
            if (adminRole == null) {
                adminRole = new Role();
                adminRole.setNom("ADMIN");
                roleService.save(adminRole);
                System.out.println("✅ Rôle ADMIN créé.");
            }

            // Vérifier si un utilisateur admin existe déjà
            Optional<Utilisateur> adminUser = utilisateurRepository.findByEmail("admin");
            if (adminUser.isEmpty()) {
                // Créer l'utilisateur admin
                Utilisateur newAdmin = new Utilisateur();
                newAdmin.setEmail("admin@gmail.com");
                newAdmin.setMotDePasse(passwordEncoder.encode("admin")); // Mot de passe sécurisé
                newAdmin.setNom("Super");
                newAdmin.setPrenom("Admin");
                newAdmin.setRoles(Collections.singleton(adminRole)); // Assigner le rôle ADMIN
                
                utilisateurService.save(newAdmin);
                System.out.println("✅ Compte admin par défaut créé : admin / admin");
            } else {
                System.out.println("🔹 Un compte admin existe déjà.");
            }
        };
    }
}
