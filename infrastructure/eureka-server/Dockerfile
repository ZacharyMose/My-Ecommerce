FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

COPY . .

RUN mvn -B -DskipTests -pl infrastructure/eureka-server -am package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/infrastructure/eureka-server/target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
