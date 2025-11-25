#Build
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

#copy only the microservice
COPY pom.xml .
COPY src ./src

#Build the service
RUN mvn clean package -DskipTests

#Run
FROM eclipse-temurin:21-jre
WORKDIR /app

#copy built jar file
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java","-jar","app.jar"]