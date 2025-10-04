# Dockerfile
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# aquí debemos asegurarnos del nombre de nuestro jar coincida
COPY target/salescore-0.0.1-SNAPSHOT.jar app.jar

# ubicación y nombre del wallet descomprimido
COPY src/main/resources/wallet /app/oracle_wallet/

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]