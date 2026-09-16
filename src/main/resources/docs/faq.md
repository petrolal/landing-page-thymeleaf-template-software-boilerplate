---
title: "FAQ & Troubleshooting"
description: "Answers to frequently asked questions, common troubleshooting steps, and community support."
category: "Reference & FAQ"
order: 1
badge: "Help"
---

Find quick solutions to common questions and edge cases below.

## Frequently Asked Questions

### Why choose Thymeleaf + HTMX over Next.js / Nuxt?

- **Zero JavaScript Build Complexities**: You get server-rendered speed and simplified debugging without hydration errors or massive `node_modules` folders.
- **Single Runtime**: Kotlin and the JVM manage both backend services and page templates cleanly.
- **SEO First**: Pure SSR HTML guarantees optimal indexation across all search engines without extra SSR server nodes.

### How do I connect a PostgreSQL database?

1. Uncomment the JPA and PostgreSQL dependencies in `build.gradle.kts`:
```kotlin
implementation(libs.spring.boot.starter.data.jpa)
runtimeOnly(libs.postgresql)
implementation(libs.flyway.core)
implementation(libs.flyway.database.postgresql)
```
2. Configure database credentials in `src/main/resources/application.yaml`.
3. Add migration scripts to `src/main/resources/db/migration/`.

### Can I deploy this on a $5/month VPS?

**Yes!** With Eclipse Temurin Alpine JRE, the base memory footprint of the running Spring Boot app is approximately 150MB–200MB, easily fitting on standard low-cost VPS instances.

## Troubleshooting

:::warning Port 8080 already allocated
If you encounter `Web server failed to start. Port 8080 was already in use`:
- Set `PORT=8090 ./gradlew bootRun`
- Or terminate the conflicting process with `lsof -i :8080 | awk 'NR>1 {print $2}' | xargs kill -9`.
:::

:::warning Tailwind CLI download fails on restricted networks
If you are behind a strict corporate proxy and Gradle cannot reach GitHub Releases:
- Download the Tailwind binary manually and place it inside `.bin/tailwindcss-linux-x64` (or corresponding OS name).
- Mark it executable with `chmod +x .bin/tailwindcss-*`.
:::
