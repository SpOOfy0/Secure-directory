    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', async (event) => {
            event.preventDefault(); // Prevent page reload

            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;

            console.log("📩 Envoi des identifiants :", { username: email, password: password });

            try {
                const response = await fetch('http://localhost:8080/api/auth/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ username: email, password: password }),
                    // Remove credentials: "include" as JWT doesn't require cookies
                });

                console.log("📡 Réponse serveur :", response);

                if (response.ok) {
                    const data = await response.json();
                    console.log("✅ Connexion réussie :", data);
                    const token = data.token;

                    // Stockage du token
                    localStorage.setItem('jwtToken', token);
                    alert(localStorage.getItem('jwtToken'));
                    // Récupération des infos utilisateur immédiatement
                    try {
                        const userResponse = await fetch('http://localhost:8080/api/auth/userinfo', {
                            method: 'GET',
                            headers: {
                                'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`,
                                'Content-Type': 'application/json'
                            }
                        });

                        if (userResponse.ok) {
                            const userData = await userResponse.json();
                            console.log("👤 Informations utilisateur :", userData);

                            // Stockage des infos utilisateur dans localStorage
                            localStorage.setItem('userData', JSON.stringify(userData));

                            // Redirection vers la page d'accueil
                            window.location.href = 'home.html';
                        } else {
                            throw new Error('Erreur lors de la récupération des informations utilisateur');
                        }
                    } catch (error) {
                        console.error("❌ Erreur récupération infos :", error);
                        localStorage.removeItem('jwtToken');
                        alert('Connexion réussie mais erreur de chargement du profil');
                    }
                } else {
                    const error = await response.json();
                    console.error("❌ Erreur serveur :", error);
                    alert('Erreur : ' + (error.error || 'Une erreur est survenue.'));
                }
            } catch (err) {
                console.error('🚨 Erreur lors de la connexion :', err);
                alert('Une erreur est survenue. Veuillez réessayer.');
            }
        });
    }


    // Gestion de l'inscription
    const signupForm = document.getElementById('signupForm');
    if (signupForm) {
        signupForm.addEventListener('submit', async (event) => {
            event.preventDefault(); // Prevent page reload

            // Retrieve form data
            const prenom = document.getElementById('prenom').value;
            const nom = document.getElementById('nom').value;
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;

            try {
                // Send POST request to backend for registration
                const response = await fetch('http://localhost:8080/api/auth/inscription', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ prenom, nom, email, motDePasse: password }),
                });

                if (response.ok) {
                    alert('Inscription réussie ! Vous pouvez maintenant vous connecter.');
                    window.location.href = 'login.html'; // Redirect to login page
                } else {
                    const error = await response.json();
                    alert('Erreur : ' + (error.error || 'Une erreur est survenue.'));
                }
            } catch (err) {
                console.error('Erreur lors de l\'inscription :', err);
                alert('Une erreur est survenue. Veuillez réessayer.');
            }
        });
    }

    // Déconnexion
    const logoutButton = document.getElementById('logoutButton');
    if (logoutButton) {
        logoutButton.addEventListener('click', async () => {
            const token = localStorage.getItem('jwtToken');
            try {
                const response = await fetch('http://localhost:8080/api/auth/logout', {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });

                if (response.ok) {
                    localStorage.removeItem('jwtToken'); // Remove the JWT token
                    alert('Vous avez été déconnecté.');
                    window.location.href = 'login.html'; // Redirect to login page
                } else {
                    const error = await response.json();
                    alert('Erreur : ' + (error.error || 'Une erreur est survenue.'));
                }
            } catch (err) {
                console.error('Erreur lors de la déconnexion :', err);
                alert('Une erreur est survenue. Veuillez réessayer.');
            }
        });
    }

    // Vérification de l'authentification
    // async function checkAuthentication() {
    //     const token = localStorage.getItem('token');
    //     if (!token) {
    //         // Si aucun token n'est trouvé, redirige vers la page de connexion
    //         window.location.href = 'login.html';
    //     }

    //     try {
    //         // Vérifie si le token est valide en appelant un endpoint du backend
    //         const response = await fetch('http://localhost:8080/api/auth/verify', {
    //             method: 'GET',
    //             headers: {
    //                 'Authorization': `Bearer ${token}`,
    //             },
    //         });

    //         if (!response.ok) {
    //             throw new Error('Token invalide ou expiré');
    //         }
    //     } catch (err) {
    //         console.error('Erreur d\'authentification :', err);
    //         localStorage.removeItem('token'); // Supprime le token invalide
    //         alert('Votre session a expiré. Veuillez vous reconnecter.');
    //         window.location.href = 'login.html'; // Redirige vers la page de connexion
    //     }
    // }

    console.log("aoaded2254221111");

    // Appelle `checkAuthentication` sur les pages nécessitant une authentification
