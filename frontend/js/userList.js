// Vérifie si l'utilisateur est connecté avant de charger les utilisateurs
document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        window.location.href = 'login.html'; // Redirige si l'utilisateur n'est pas authentifié
    } else {
        loadUsers();
        loadUserInfo(); // Charge les infos utilisateur
    }

    document.getElementById('logoutButton').addEventListener('click', logout);
});

// Fonction pour charger la liste des utilisateurs
async function loadUsers() {
    const token = localStorage.getItem('jwtToken');

    try {
        const response = await fetch('http://localhost:8080/api/user/utilisateurs', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        const responseText = await response.text(); // Capture la réponse brute
        console.log("📡 Réponse brute du serveur :", responseText);

        try {
            const users = JSON.parse(responseText); // Convertit la réponse JSON

            if (Array.isArray(users)) {
                console.log("✅ Utilisateurs récupérés :", users);
                renderUserTable(users);
            } else {
                console.error("❌ ERREUR : La réponse n'est pas un tableau valide", users);
                alert("Erreur inattendue du serveur.");
            }
        } catch (jsonError) {
            console.error("❌ Erreur lors de l'analyse JSON :", jsonError);
            alert("Erreur de format de réponse du serveur.");
        }

    } catch (err) {
        console.error('❌ Erreur lors de la récupération des utilisateurs :', err);
        alert('Une erreur est survenue. Veuillez réessayer.');
    }
}

// Fonction pour afficher les utilisateurs dans le tableau
function renderUserTable(users) {
    const tableBody = document.getElementById('userTableBody');
    tableBody.innerHTML = ''; // Vide le tableau avant de le remplir

    users.forEach((user) => {
        const row = document.createElement('tr');

        // Vérification du format des rôles
        let roles = 'Utilisateur';
        if (user.roles && Array.isArray(user.roles)) {
            roles = user.roles.join(', '); // ✅ Assure que roles est une liste de string
        }

        row.innerHTML = `
            <td>${user.id}</td>
            <td>${user.nom}</td>
            <td>${user.prenom}</td>
            <td>${user.email}</td>
            <td>${roles}</td>
        `;

        tableBody.appendChild(row);
    });
}

// Fonction pour charger les informations de l'utilisateur connecté
async function loadUserInfo() {
    try {
        const token = localStorage.getItem('jwtToken');
        if (!token) {
            window.location.href = 'login.html';
            return;
        }

        const response = await fetch('http://localhost:8080/api/auth/userinfo', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            const data = await response.json();
            console.log("✅ Informations utilisateur récupérées :", data);

            // Vérifier si #role existe avant de l'utiliser
            const roleElement = document.getElementById('role');
            if (roleElement) {
                roleElement.textContent = data.role || 'Utilisateur';
            } else {
                console.warn("⚠️ Élément HTML #role introuvable.");
            }

        } else {
            console.error("❌ Erreur lors de la récupération des informations utilisateur");
            alert('Erreur : accès refusé. Veuillez vous reconnecter.');
            localStorage.removeItem('jwtToken');
            window.location.href = 'login.html';
        }
    } catch (err) {
        console.error("❌ Erreur lors de la récupération des informations utilisateur :", err);
        alert('Une erreur est survenue. Veuillez réessayer.');
    }
}

// Fonction pour gérer la déconnexion
function logout() {
    localStorage.removeItem('jwtToken');
    alert("Vous avez été déconnecté.");
    window.location.href = 'login.html';
}
