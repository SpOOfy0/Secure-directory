CREATE DATABASE IF NOT EXISTS secure_directory;
USE secure_directory;

-- Création de la table roles si elle n'existe pas (optionnel si elle est déjà créée par JPA)
CREATE TABLE IF NOT EXISTS role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50) UNIQUE NOT NULL
);

-- Insère les rôles ADMIN et USER seulement s'ils n'existent pas déjà
INSERT INTO role (nom) SELECT 'ADMIN' WHERE NOT EXISTS (SELECT 1 FROM role WHERE nom = 'ADMIN');
INSERT INTO role (nom) SELECT 'USER' WHERE NOT EXISTS (SELECT 1 FROM role WHERE nom = 'USER');

-- Vérifie que les rôles ont bien été créés
SELECT * FROM role;