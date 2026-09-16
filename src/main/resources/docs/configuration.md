---
title: "Application Settings"
description: "Configure site metadata, documentation properties, analytics integrations, and environment variables."
category: "Configuration"
order: 1
badge: "Core"
---

All configuration options are defined in `src/main/resources/application.yaml` and bound into type-safe Kotlin data classes via `@ConfigurationProperties(prefix = "landing")`.

## Configuration Schema

```yaml
landing:
  site:
    name: "AuraLaunch"
    title: "AuraLaunch — Open Source Developer Platform & Boilerplate"
    tagline: "High-performance open-source software boilerplate powered by Spring Boot 3, Kotlin, and HTMX."
    description: "100% Free and Open Source developer-first template with built-in LazyVim-style docs, Tailwind CSS v4, and automated CI/CD."
    url: "https://example.com"
    author: "Petrolal"
    og-image: "/images/og-preview.png"
    license: "GNU General Public License v3.0"

  hero:
    badge: "⚡ 100% Free & Open Source • GNU GPL v3.0"
    cta-text: "Explore Documentation"
    cta-link: "/docs"
    secondary-cta-text: "Buy Me a Coffee ☕"
    secondary-cta-link: "https://buymeacoffee.com/petrolal"

  sponsor:
    buy-me-a-coffee: "https://buymeacoffee.com/petrolal"
    github-sponsors: "https://github.com/sponsors/petrolal"
    target-goal: "Support Open Source Tooling & Boilerplates"

  docs:
    enabled: true
    title: "AuraLaunch Docs"
    version: "v1.0.0"
    github-repo: "https://github.com/petrolal/landing-page-thymeleaf-template-software-boilerplate"
    branch: "main"

  social:
    twitter: "https://twitter.com"
    github: "https://github.com/petrolal/landing-page-thymeleaf-template-software-boilerplate"
    linkedin: "https://linkedin.com"
    medium: "https://medium.com"
    discord: "https://discord.com"

  analytics:
    google-analytics-id: "${GA_ID:}"
    google-tag-manager-id: "${GTM_ID:}"
    plausible-domain: "${PLAUSIBLE_DOMAIN:}"
    posthog-key: "${POSTHOG_KEY:}"
    posthog-host: "https://us.i.posthog.com"
```

## Environment Variables

You can override any property using environment variables at runtime:

| Variable | Property | Description |
| :--- | :--- | :--- |
| `PORT` | `server.port` | HTTP server listening port (default `8080`) |
| `DB_HOST` | `spring.datasource.url` | PostgreSQL database hostname |
| `DB_PORT` | `spring.datasource.url` | PostgreSQL database port (default `5432`) |
| `DB_NAME` | `spring.datasource.url` | Database name |
| `DB_USERNAME` | `spring.datasource.username` | Database user credentials |
| `DB_PASSWORD` | `spring.datasource.password` | Database password |
| `GA_ID` | `landing.analytics.google-analytics-id` | Google Analytics Measurement ID |
| `POSTHOG_KEY` | `landing.analytics.posthog-key` | PostHog Project API key |

:::tip Profile-Specific Configuration
You can create `application-prod.yaml` or `application-dev.yaml` to specify profile-specific overrides. Activate profiles with `SPRING_PROFILES_ACTIVE=prod`.
:::
