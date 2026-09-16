---
title: "Docker & Production"
description: "Containerizing the application, multi-stage builds, and deployment strategies for cloud providers."
category: "Deployment & Guides"
order: 1
badge: "DevOps"
---

AuraLaunch is architected for frictionless containerized deployment anywhere Docker runs.

## Multi-Stage Dockerfile

The included `Dockerfile` uses a two-stage build to keep runtime images lean and secure:

```dockerfile
# Stage 1: Build stage with JDK 21
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle gradle
COPY src src
RUN ./gradlew bootJar --no-daemon

# Stage 2: Minimal JRE 21 runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## Building & Running with Docker

Build and run the container locally:

```bash
# Using Gradle tasks
./gradlew dockerBuild
./gradlew dockerRun

# Or standard Docker commands
docker build -t auralaunch:latest .
docker run -p 8080:8080 auralaunch:latest
```

## Docker Compose Setup

A `docker-compose.yml` file is provided for local multi-container development or production deployment:

```yaml
version: '3.8'
services:
  web:
    build: .
    ports:
      - "8080:8080"
    environment:
      - PORT=8080
      - SPRING_PROFILES_ACTIVE=prod
    restart: unless-stopped
```

:::tip Deployment Targets
This container image can be deployed effortlessly to:
- **Cloud PaaS**: Fly.io, Railway, Render, DigitalOcean App Platform.
- **Self-Hosted / VPS**: Coolify, Dokku, Portainer, Kubernetes.
:::
