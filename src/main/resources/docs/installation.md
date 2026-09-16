---
title: "Installation"
description: "System requirements, cloning the repository, and starting the local development environment."
category: "Getting Started"
order: 2
badge: "Guide"
---

Follow this guide to get AuraLaunch cloned, configured, and running on your local development machine.

## Prerequisites

Ensure you have the following software installed:

| Requirement | Minimum Version | Recommended | Notes |
| :--- | :--- | :--- | :--- |
| **JDK** | 21 | Eclipse Temurin 21+ | Required for Spring Boot 3.4 & Kotlin 2.1 |
| **Gradle** | 8.x | Wrapper included (`./gradlew`) | No manual Gradle installation needed |
| **Docker** | 24+ | Docker Desktop / OrbStack | Optional, for containerized deployments |
| **Node.js** | *None* | **Not needed** | Tailwind CLI is downloaded automatically by Gradle |

:::info Zero Node.js Dependency
Unlike standard web projects, AuraLaunch uses the standalone Tailwind CSS v4 CLI binary. Gradle downloads the correct native binary for your OS (Linux, macOS Apple Silicon/Intel, Windows) during the build.
:::

## Cloning the Repository

Clone the boilerplate repository from GitHub:

```bash
git clone https://github.com/petrolal/landing-page-thymeleaf-template-software-boilerplate.git my-app
cd my-app
```

## Running the Application

Start the Spring Boot application using the Gradle wrapper:

```bash
./gradlew bootRun
```

:::tip Continuous Tailwind Watching
If you are actively modifying HTML templates and CSS styles, run the Tailwind watch task in a separate terminal:
```bash
./gradlew tailwindWatch
```
This re-compiles `style.css` instantaneously whenever any template file changes.
:::

## Verifying the Setup

Once the server finishes starting up, open your browser and navigate to:

- **Landing Page**: [http://localhost:8080](http://localhost:8080)
- **Documentation**: [http://localhost:8080/docs](http://localhost:8080/docs)
- **Swagger / OpenAPI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Live Health Check**: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health) (if actuator is enabled)

:::warning Port Conflicts
If port `8080` is already in use by another application on your system, you can pass a custom port:
```bash
PORT=8090 ./gradlew bootRun
```
Or define `server.port: 8090` in your `application.yaml`.
:::
