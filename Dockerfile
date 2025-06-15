# Usa una imagen oficial de Maven como base para la construcción, se utiliza la mas reciente ya que es la que tiene menos vulnerabilidades
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Actualiza los paquetes para reducir vulnerabilidades y elimina cachés
RUN apt-get update && apt-get upgrade -y && apt-get clean

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo pom.xml primero para aprovechar la caché de capas de Docker
COPY pom.xml .

# Descarga las dependencias (esto se ejecutará solo si el pom.xml cambia)
RUN mvn dependency:go-offline -B

# Copia el código fuente
COPY src ./src

# Empaqueta la aplicación
RUN mvn package -DskipTests

# Etapa de ejecución usando una imagen más ligera
FROM eclipse-temurin:17-jre-jammy

# Establece el directorio de trabajo en la etapa de ejecución
WORKDIR /app

# Copia el JAR específico desde la etapa de construcción
COPY --from=build /app/target/bytestournament-0.0.1-SNAPSHOT-jar-with-dependencies.jar app.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]