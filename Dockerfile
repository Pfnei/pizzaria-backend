# -----------------------------
# 1️⃣ Build Stage
# -----------------------------
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# -----------------------------
# 2️⃣ Run Stage
# -----------------------------
FROM eclipse-temurin:21-jdk

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Spring Boot läuft auf 8081
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
