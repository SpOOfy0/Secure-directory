document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("jwtToken");

    if (!token) {
        window.location.href = "login.html"; // Redirection si non authentifié
        return;
    }

    const form = document.getElementById("addUserForm");
    const messageDiv = document.getElementById("message");

    form.addEventListener("submit", async (event) => {
        event.preventDefault(); // Empêche le rechargement de la page

        const email = document.getElementById("email").value.trim();
        const motDePasse = document.getElementById("motDePasse").value.trim();
        const nom = document.getElementById("nom").value.trim();
        const prenom = document.getElementById("prenom").value.trim();
        const role = document.getElementById("role").value;

        if (!email || !motDePasse || !nom || !prenom) {
            showMessage("Veuillez remplir tous les champs.", "error");
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/api/admin/utilisateur/ajouter", {
                method: "POST",
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    email,
                    motDePasse,
                    nom,
                    prenom,
                    role
                })
            });

            const result = await response.json();

            if (response.ok) {
                showMessage("Utilisateur ajouté avec succès !", "success");
                form.reset(); // Réinitialise le formulaire après succès
            } else {
                showMessage(result.error || "Erreur lors de l'ajout de l'utilisateur.", "error");
            }
        } catch (error) {
            console.error("❌ Erreur lors de l'ajout de l'utilisateur :", error);
            showMessage("Une erreur est survenue. Veuillez réessayer.", "error");
        }
    });

    function showMessage(message, type) {
        messageDiv.textContent = message;
        messageDiv.className = `message ${type}`; // Applique une classe CSS pour le style
    }
});
