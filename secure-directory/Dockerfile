# Étape 1 : Build avec Maven et OpenJDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Étape 2 : Exécuter le backend avec OpenJDK 21
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar backend.jar
EXPOSE 8080
CMD ["java", "-jar", "backend.jar"]
