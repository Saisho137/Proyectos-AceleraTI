# Stage 1: Build the application
FROM amazoncorretto:21-alpine AS builder
VOLUME /tmp
# Bancolombia Scaffold throws error with /app or /build
WORKDIR /home/work

# Copy just gradle files
COPY build.gradle settings.gradle main.gradle gradle.properties lombok.config gradlew ./
COPY gradle ./gradle
COPY applications ./applications
COPY domain ./domain
COPY infrastructure ./infrastructure

# Build the application
RUN ./gradlew bootJar --info


# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp
WORKDIR /app
COPY --from=builder /home/work/applications/app-service/build/libs/*.jar docker-example.jar

# Replace with a non-root user to avoid running the container with excessive privileges
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8080

RUN adduser -D -u 1001 appuser
USER appuser

ENTRYPOINT [ "/bin/sh", "-c", "/opt/java/openjdk/bin/java $JAVA_OPTS  -jar ./docker-example.jar" ]
