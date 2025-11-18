FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy entire project (needed because parent pom exists at root)
COPY . .

# Build only user-service (but also required modules)
RUN mvn -B -DskipTests -pl services/user-service -am package

# --- FINAL IMAGE ---
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/services/user-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]