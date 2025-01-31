package com.example.secure_directory.service;

import com.example.secure_directory.entity.Role;
import com.example.secure_directory.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role findByNom(String nom) {
        Optional<Role> role = roleRepository.findByNom(nom);
        return role.orElseThrow(() -> new RuntimeException("Rôle non trouvé : " + nom));
    }

    public Role save(Role role) {
        // Vérifie si le rôle existe déjà
        Optional<Role> existingRole = roleRepository.findByNom(role.getNom());
        if (existingRole.isPresent()) {
            return existingRole.get(); // Si le rôle existe déjà, on le retourne
        }

        // Sinon, on enregistre le nouveau rôle
        return roleRepository.save(role);
    }
}
