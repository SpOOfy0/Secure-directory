// Fonction pour charger les données de l'utilisateur
async function loadUserData() {
    const params = new URLSearchParams(window.location.search);
    const userId = params.get('id');

    if (!userId) {
        alert('ID utilisateur manquant. Redirection...');
        window.location.href = 'admin-users.html';
        return;
    }

    const token = localStorage.getItem('jwtToken');
    console.log('Token :', token);
    if (!token) {
        alert('Session expirée. Veuillez vous reconnecter.');
        window.location.href = 'login.html';
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/api/admin/utilisateur/${userId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            const user = await response.json();

            // Remplit le formulaire avec les données de l'utilisateur
            document.getElementById('userId').value = user.id;
            document.getElementById('email').value = user.email;
            document.getElementById('nom').value = user.nom;
            document.getElementById('prenom').value = user.prenom;
            document.getElementById('role').value = user.role || "USER";
        } else {
            alert('Erreur lors du chargement des données utilisateur.');
            window.location.href = 'admin-users.html';
        }
    } catch (err) {
        console.error('Erreur lors du chargement des données utilisateur :', err);
        alert('Une erreur est survenue.');
        window.location.href = 'admin-users.html';
    }
}

// Fonction pour modifier un utilisateur
async function editUser(event) {
    event.preventDefault(); // Empêche le rechargement de la page

    const userId = document.getElementById('userId').value;
    const email = document.getElementById('email').value;
    const nom = document.getElementById('nom').value;
    const prenom = document.getElementById('prenom').value;
    const motDePasse = document.getElementById('motDePasse').value;
    const role = document.getElementById('role').value;

    const token = localStorage.getItem('jwtToken');
    if (!token) {
        window.location.href = 'login.html';
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/api/admin/utilisateur/${userId}`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                email,
                nom,
                prenom,
                motDePasse: motDePasse || null,
                role,
            }),
        });

        if (response.ok) {
            document.getElementById('message').innerText = 'Utilisateur modifié avec succès.';
            document.getElementById('message').classList.add('success');
        } else {
            const error = await response.json();
            document.getElementById('message').innerText = error.error || 'Une erreur est survenue.';
            document.getElementById('message').classList.add('error');
        }
    } catch (err) {
        console.error('Erreur lors de la modification de l\'utilisateur :', err);
        document.getElementById('message').innerText = 'Une erreur est survenue. Veuillez réessayer.';
        document.getElementById('message').classList.add('error');
    }
}

// Attache les événements et charge les données utilisateur
document.addEventListener('DOMContentLoaded', () => {
    loadUserData();
    document.getElementById('editUserForm').addEventListener('submit', editUser);
});
