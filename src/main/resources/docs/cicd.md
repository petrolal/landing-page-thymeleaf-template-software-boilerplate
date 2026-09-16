---
title: "CI/CD Workflows"
description: "Automate code quality, tests, ktlint verification, and container publishing with GitHub Actions."
category: "Deployment & Guides"
order: 2
badge: "GitHub Actions"
---

The boilerplate comes with a pre-configured GitHub Actions CI workflow in `.github/workflows/ci.yml`.

## Pipeline Overview

Every Pull Request and Push to the `main` branch automatically triggers:

1. **JDK 21 Setup**: Configures Temurin JDK 21 and Gradle cache.
2. **Code Style & Linting**: Runs `ktlint` to enforce idiomatic Kotlin style.
3. **Automated Testing**: Executes unit tests and Spring Boot integration tests.
4. **Tailwind Verification**: Compiles CSS assets to verify styling integrity.
5. **Executable JAR Build**: Verifies that `bootJar` packages cleanly.

```yaml
name: CI Pipeline

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Set up JDK 21
        uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'
      - name: Grant execute permission for gradlew
        run: chmod +x gradlew
      - name: Build with Gradle & Run Checks
        run: ./gradlew check bootJar
```

:::tip Container Registry Publishing
You can add a CD step using `docker/build-push-action` to automatically push built images to GitHub Container Registry (`ghcr.io`) or Docker Hub on release tags!
:::
