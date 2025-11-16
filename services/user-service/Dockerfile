# Stage 1: Build the application
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /workspace

# Copy the Maven wrapper files from the root
COPY .mvn/ .mvn
COPY mvnw mvnw.cmd ./

# Copy all pom.xml files to cache dependencies
COPY pom.xml ./
COPY infrastructure/config-server/pom.xml ./infrastructure/config-server/
COPY infrastructure/eureka-server/pom.xml ./infrastructure/eureka-server/
COPY services/user-service/pom.xml ./services/user-service/
# Add new services here as you create them

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy the source code for this service
COPY services/user-service/src ./services/user-service/src

# Make the mvnw script executable inside the container
RUN chmod +x mvnw

# Build only this specific service
RUN ./mvnw clean install -DskipTests -pl services/user-service -am

# Stage 2: Create the final lightweight image
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /workspace/services/user-service/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]