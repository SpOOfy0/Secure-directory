// Fonction pour charger les informations de l'utilisateur
async function loadProfile() {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        window.location.href = 'login.html'; // Redirige si l'utilisateur n'est pas authentifié
        return;
    }

    try {
        const response = await fetch('http://localhost:8080/api/profil', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            const user = await response.json();

            // Remplit les informations dans la page
            document.getElementById('userEmail').innerText = user.email;
            document.getElementById('userNom').innerText = user.nom;
            document.getElementById('userPrenom').innerText = user.prenom;

            // Remplit le formulaire de mise à jour avec les valeurs actuelles
            document.getElementById('userId').value = user.id;
            document.getElementById('newNom').value = user.nom;
            document.getElementById('newPrenom').value = user.prenom;
        } else {
            alert('Erreur lors du chargement des informations du profil.');
        }
    } catch (err) {
        console.error('Erreur lors de la récupération des informations utilisateur :', err);
        alert('Une erreur est survenue.');
    }
}

// Fonction pour mettre à jour le profil
async function updateProfile(event) {
    event.preventDefault(); // Empêche le rechargement de la page

    const token = localStorage.getItem('jwtToken');
    if (!token) {
        window.location.href = 'login.html';
        return;
    }

    const newNom = document.getElementById('newNom').value;
    const newPrenom = document.getElementById('newPrenom').value;
    const newPassword = document.getElementById('newPassword').value;

    try {
        const response = await fetch('http://localhost:8080/api/profil/miseAJour', {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                nom: newNom || null,
                prenom: newPrenom || null,
                motDePasse: newPassword || null,
            }),
        });

        if (response.ok) {
            alert('Profil mis à jour avec succès.');
            loadProfile(); // Recharge le profil après la mise à jour
        } else {
            alert('Erreur lors de la mise à jour du profil.');
        }
    } catch (err) {
        console.error('Erreur lors de la mise à jour du profil :', err);
        alert('Une erreur est survenue.');
    }
}

// Fonction pour clôturer le compte
async function closeAccount(event) {
    event.preventDefault(); // Empêche le rechargement de la page

    const token = localStorage.getItem('jwtToken');
    if (!token) {
        window.location.href = 'login.html';
        return;
    }

    if (!confirm('Êtes-vous sûr de vouloir clôturer votre compte ?')) {
        return;
    }

    try {
        const response = await fetch('http://localhost:8080/api/profil/cloturer', {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        });

        if (response.ok) {
            alert('Votre compte a été clôturé.');
            localStorage.removeItem('jwtToken');
            window.location.href = 'login.html';
        } else {
            alert('Erreur lors de la clôture du compte.');
        }
    } catch (err) {
        console.error('Erreur lors de la clôture du compte :', err);
        alert('Une erreur est survenue.');
    }
}

// Fonction pour la déconnexion
async function logout() {
    localStorage.removeItem('jwtToken');
    window.location.href = 'login.html';
}

// Ajout des événements
document.addEventListener('DOMContentLoaded', () => {
    loadProfile();
    document.getElementById('updateProfileForm').addEventListener('submit', updateProfile);
    document.getElementById('closeAccountForm').addEventListener('submit', closeAccount);
    document.getElementById('logoutButton').addEventListener('click', logout);
});
