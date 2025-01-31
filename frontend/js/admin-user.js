document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        window.location.href = 'login.html'; // Redirige si l'utilisateur n'est pas authentifié
        return;
    }

    loadUsers();
    loadUserInfo();

    document.getElementById('logoutButton').addEventListener('click', logout);
});

// Fonction pour charger les informations de l'utilisateur connecté
async function loadUserInfo() {
    const token = localStorage.getItem('jwtToken');
    try {
        const response = await fetch('http://localhost:8080/api/auth/userinfo', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            const data = await response.json();
            console.log("✅ Informations utilisateur :", data);

            const roleElement = document.getElementById('userRole');
            if (roleElement) {
                roleElement.textContent = `Rôle: ${data.role || 'Utilisateur'}`;
            }
        } else {
            console.error("❌ Erreur lors de la récupération des infos utilisateur");
            localStorage.removeItem('jwtToken');
            window.location.href = 'login.html';
        }
    } catch (err) {
        console.error("❌ Erreur lors de la récupération des infos utilisateur :", err);
    }
}

// Fonction pour charger la liste des utilisateurs
async function loadUsers() {
    const token = localStorage.getItem('jwtToken');

    try {
        const response = await fetch('http://localhost:8080/api/admin/utilisateurs', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            const users = await response.json();
            renderUserTable(users);
        } else {
            alert('❌ Erreur lors du chargement des utilisateurs.');
        }
    } catch (err) {
        console.error('❌ Erreur lors de la récupération des utilisateurs :', err);
        alert('Une erreur est survenue.');
    }
}

// Fonction pour afficher les utilisateurs dans le tableau
function renderUserTable(users) {
    const tableBody = document.getElementById('userTableBody');
    tableBody.innerHTML = '';

    users.forEach((user) => {
        const row = document.createElement('tr');

        row.innerHTML = `
            <td>${user.id}</td>
            <td>${user.nom || 'Non défini'}</td>
            <td>${user.email}</td>
            <td>${user.roles ? user.roles.join(', ') : 'Aucun rôle assigné'}</td>
            <td class="actions">
                <button class="btn-edit" onclick="editUser(${user.id})">Modifier</button>
                <button class="btn-delete" onclick="deleteUser(${user.id})">Supprimer</button>
            </td>
        `;

        tableBody.appendChild(row);
    });
}

// Fonction pour supprimer un utilisateur
async function deleteUser(userId) {
    const token = localStorage.getItem('jwtToken');

    if (!token) {
        alert('❌ Vous devez être connecté pour effectuer cette action.');
        window.location.href = 'login.html';
        return;
    }

    if (!confirm('⚠️ Êtes-vous sûr de vouloir supprimer cet utilisateur ?')) {
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/api/admin/utilisateur/${userId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            alert('✅ Utilisateur supprimé avec succès.');
            loadUsers(); // Recharge la liste après suppression
        } else {
            const error = await response.json();
            alert(`❌ Erreur : ${error.error || 'Suppression impossible'}`);
        }
    } catch (err) {
        console.error('❌ Erreur lors de la suppression :', err);
        alert('Une erreur est survenue. Veuillez réessayer.');
    }
}


// Fonction pour rediriger vers la page de modification
function editUser(userId) {
    window.location.href = `edit-user.html?id=${userId}`;
}

// Fonction pour la déconnexion
function logout() {
    localStorage.removeItem('jwtToken');
    alert("Vous avez été déconnecté.");
    window.location.href = 'login.html';
}
