FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

COPY . .

RUN mvn -B -DskipTests -pl infrastructure/config-server -am package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/infrastructure/config-server/target/*.jar app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
