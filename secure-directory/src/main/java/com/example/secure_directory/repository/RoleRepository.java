package com.example.secure_directory.repository;

import com.example.secure_directory.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Recherche un rôle par son nom (exemple : "ROLE_ADMIN", "ROLE_USER")
    Optional<Role> findByNom(String nom);
}
