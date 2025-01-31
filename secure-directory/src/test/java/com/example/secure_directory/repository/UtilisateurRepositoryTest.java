// package com.example.secure_directory.repository;

// import com.example.secure_directory.entity.Utilisateur;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

// import java.util.Optional;

// import static org.junit.jupiter.api.Assertions.*;

// @DataJpaTest
// class UtilisateurRepositoryTest {

//     @Autowired
//     private UtilisateurRepository utilisateurRepository;

//     @Test
//     void testFindByEmail() {
//         // Créer un utilisateur fictif
//         Utilisateur utilisateur = new Utilisateur();
//         utilisateur.setEmail("test@example.com");
//         utilisateur.setNom("TestNom");
//         utilisateur.setPrenom("TestPrenom");
//         utilisateur.setMotDePasse("password123");

//         // Sauvegarder l'utilisateur
//         utilisateurRepository.save(utilisateur);

//         // Rechercher l'utilisateur par email
//         Optional<Utilisateur> result = utilisateurRepository.findByEmail("test@example.com");

//         // Vérifier que l'utilisateur est trouvé
//         assertTrue(result.isPresent());
//         assertEquals("test@example.com", result.get().getEmail());
//     }
// }
