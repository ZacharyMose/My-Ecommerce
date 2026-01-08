from maven:3.9.6-eclipse-temurin-21 as builder

WORKDIR /app

# Copy only the microservice
COPY pom.xml .
COPY src ./src

# Build the service
RUN mvn clean package -DskipTests

# Run
from eclipse-temurin:21-jre
WORKDIR /app

# copy the built jar file
copy --from=builder /app/target/*.jar app.jar

EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]