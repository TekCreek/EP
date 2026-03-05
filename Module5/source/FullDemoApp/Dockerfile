# ── Build stage ───────────────────────────────────────────────
#FROM eclipse-temurin:17-jdk-alpine AS builder
#WORKDIR /build
#
#COPY mvnw pom.xml ./
#COPY .mvn .mvn
## Pre-fetch dependencies (layer-cached until pom.xml changes)
#RUN ./mvnw dependency:go-offline -q
#
#COPY src ./src
#RUN ./mvnw package -DskipTests -q

# ── Runtime stage ─────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy built application JAR from Maven target/
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

#COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-DHOSTNAME=host.docker.internal", \
  "-jar", "app.jar"]