// Vérifie si la session est active et charge les infos utilisateur
async function loadUserInfo() {
    try {
        const token = localStorage.getItem('jwtToken');
        if (!token) {
            console.error("⚠️ Aucun token trouvé. Redirection vers login.");
            window.location.href = 'login.html';
            return;
        }

        console.log("📢 Token existant : ", token);
        alert(token); // 🔹 Affiche le token pour debug

        const response = await fetch('http://localhost:8080/api/auth/userinfo', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`, // Envoi du JWT dans le header
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            const data = await response.json();
            console.log("✅ Informations utilisateur récupérées :", data);

            // Met à jour l'affichage du rôle
            document.getElementById('role').textContent = data.role;

            // 🔹 Afficher le bon lien en fonction du rôle
            if (data.role === 'ADMIN') {
                document.getElementById('adminLink').style.display = 'block';
            } else if (data.role === 'USER') {
                document.getElementById('userLink').style.display = 'block';
            }
        } else {
            const error = await response.json();
            console.error("❌ Erreur lors de la récupération des informations utilisateur :", error);
            alert('Erreur : ' + (error.error || 'Une erreur est survenue.'));

            // 🔹 Redirection en cas d'erreur 401 (non autorisé)
            if (response.status === 401) {
                localStorage.removeItem('jwtToken');
                window.location.href = 'login.html';
            }
        }
    } catch (err) {
        console.error("❌ Erreur lors de la récupération des informations utilisateur :", err);
        alert('Une erreur est survenue. Veuillez réessayer.');
    }
}

// Gestion de la déconnexion
function setupLogoutButton() {
    const logoutButton = document.getElementById('logoutButton');
    if (logoutButton) {
        logoutButton.addEventListener('click', async () => {
            try {
                // 🔹 Envoie une requête au backend pour la déconnexion
                await fetch('http://localhost:8080/api/auth/logout', {
                    method: 'POST',
                    credentials: 'include' // ✅ Important pour envoyer la session
                });

                alert('Vous avez été déconnecté.');
                localStorage.removeItem('jwtToken'); // Supprime le token du stockage local
                window.location.href = 'login.html'; // Redirige vers la page de connexion
            } catch (error) {
                console.error("❌ Erreur lors de la déconnexion :", error);
                alert('Une erreur est survenue lors de la déconnexion.');
            }
        });
    }
}

// Charge les informations utilisateur et configure les événements
document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        window.location.href = 'login.html'; // Redirection vers login si non connecté
    } else {
        loadUserInfo(); // Charge les informations utilisateur
        setupLogoutButton(); // Active le bouton de déconnexion
    }
});
