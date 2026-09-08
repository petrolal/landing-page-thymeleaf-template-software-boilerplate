# Landing Page Thymeleaf Template

A modern, production-ready template for building high-performance landing pages and web applications using **Spring Boot 3**, **Kotlin**, **Thymeleaf**, and **HTMX**.

Configured as a general template that can be instantiated instantly using **[JBang](https://www.jbang.dev/)** and continuously validated with **GitHub Actions**.

---

## Quickstart with JBang

You can scaffold a fresh project from this template without cloning or manual setup using JBang.

### 1. Install JBang (if not already installed)

```bash
# Via SDKMAN!
sdk install jbang

# Or via Homebrew (macOS / Linux)
brew install jbangdev/tap/jbang

# Or via curl / bash
curl -Ls https://sh.jbang.dev | bash -s - app setup
```

### 2. Scaffold a New Project

Run the generator directly referencing this GitHub repository:

```bash
# Generate with defaults (directory: my-landing-page, package: com.petrolal.landingpage)
jbang init@petrolal/landing-page-thymeleaf-template-first my-project

# Or specify a custom target package
jbang init@petrolal/landing-page-thymeleaf-template-first \
  --package com.mycompany.webapp \
  my-company-landing

# Or run using the repository name directly
jbang petrolal/landing-page-thymeleaf-template-first my-company-landing
```

### Generator Options

| Option | Flag | Description | Default |
|---|---|---|---|
| `<projectName>` | *positional* | Target directory & project name | `my-landing-page` |
| `--package` | `-p` | Base Kotlin package for the new project | `com.petrolal.landingpage` |
| `--branch` | `-b` | Git branch or release tag to pull from | `main` |
| `--repo` | `-r` | GitHub repository to fetch template from | `petrolal/landing-page-thymeleaf-template-first` |
| `--archive` | `-a` | Local `.tar.gz` archive path (for offline/CI) | *none* |

### 3. Run Your New Project

```bash
cd my-project
git init
./gradlew bootRun
```

Visit [http://localhost:8080](http://localhost:8080) in your browser.

---

## Tech Stack & Features

- **Kotlin 2.1** with strict null-safety and idiomatic patterns.
- **Spring Boot 3.4** (Web, Thymeleaf, DevTools).
- **HTMX 2.0** integrated via WebJars.
- **SpringDoc OpenAPI** with Swagger UI support (`/swagger-ui.html`).
- **Ktlint** plugin configured for code formatting & verification (`./gradlew ktlintCheck`).
- **PostgreSQL & Flyway** configuration ready out-of-the-box (commented in `build.gradle.kts` and `application.yaml`).
- **Docker & Docker Compose** ready (`Dockerfile`, `docker-compose.yml`, and Gradle tasks `dockerBuild` / `dockerRun`).

---

## Local Development Commands

- **Run development server:**
  ```bash
  ./gradlew bootRun
  ```
- **Run tests and ktlint checks:**
  ```bash
  ./gradlew check
  ```
- **Format code with ktlint:**
  ```bash
  ./gradlew ktlintFormat
  ```
- **Build executable JAR:**
  ```bash
  ./gradlew bootJar
  ```
- **Build Docker image:**
  ```bash
  ./gradlew dockerBuild
  ```
- **Start Postgres database container:**
  ```bash
  docker compose up -d postgres
  ```

---

## CI / CD Pipelines (GitHub Actions)

This repository includes automated GitHub Actions workflows:

1. **Continuous Integration (`.github/workflows/ci.yml`)**:
   - Runs on every `push` and `pull_request` to `main`.
   - Sets up Java 21 and caches Gradle dependencies.
   - Executes `./gradlew check` (ktlint, tests, compiler verification).
   - Verifies `./gradlew bootJar` and Docker image packaging.
   - Validates `jbang-catalog.json` and `init.kt`.
   - Runs an end-to-end scaffolding test using JBang and verifies `./gradlew check` passes on the newly generated application.

2. **Template Release / Deployment (`.github/workflows/release.yml`)**:
   - Triggers on tag pushes (`v*`) or manual `workflow_dispatch`.
   - Runs validation builds.
   - Bundles clean `.tar.gz` and `.zip` distribution packages.
   - Publishes GitHub Releases with ready-to-use JBang commands.
