# Utilisation de Nginx pour servir des fichiers statiques
FROM nginx:alpine

# Définition du répertoire de travail
WORKDIR /usr/share/nginx/html

# Suppression des fichiers par défaut de Nginx
RUN rm -rf /usr/share/nginx/html/*

# Copie de tous les fichiers statiques vers Nginx
COPY . /usr/share/nginx/html

# Exposer le port 80 pour accéder au frontend
EXPOSE 80

# Lancer Nginx
CMD ["nginx", "-g", "daemon off;"]
