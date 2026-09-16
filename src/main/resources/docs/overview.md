---
title: "Overview"
description: "Welcome to AuraLaunch — the modern, developer-first Spring Boot 3 & Kotlin boilerplate inspired by high-performance developer tools."
category: "Getting Started"
order: 1
badge: "Intro"
---

Welcome to **AuraLaunch Documentation**! This template provides a production-grade foundation for developers looking to build modern, high-converting web applications and landing pages using the JVM ecosystem without JavaScript framework fatigue.

:::tip Modern Stack, Zero Clutter
Built with Spring Boot 3.4+, Kotlin 2.1+, Thymeleaf, Tailwind CSS v4 Standalone, and HTMX — giving you the snappy responsiveness of modern SPAs with the rock-solid reliability and developer velocity of server-side rendering.
:::

## Key Features

- **⚡ Blazing Fast Architecture**: Server-side rendered with Spring Boot 3 and Thymeleaf for optimal Time-To-First-Byte (TTFB) and SEO.
- **🎨 Tailwind CSS v4 Standalone**: Integrated directly into Gradle with zero Node.js / npm dependencies.
- **⚡ HTMX Interactivity**: Dynamic UI updates, modal dialogs, and asynchronous form submissions without complex client-side state.
- **🌗 Dark Mode & Theming**: Automatic system preference detection with anti-flash script and instant theme toggle.
- **🔍 Built-in Search & Cheatsheets**: LazyVim-inspired keyboard shortcuts (<kbd>Ctrl</kbd> + <kbd>K</kbd>), table of contents scrollspy, and quick navigation.
- **🛡️ Production Ready**: Multi-stage Dockerfile, CI/CD GitHub Actions workflow, ktlint code formatting, and OpenTelemetry / Springdoc OpenAPI support.

## Architecture Philosophy

AuraLaunch follows the **Simplicity First** approach:

```
┌──────────────────────────────────────────────────────────┐
│                   Browser / Client                       │
│  (Tailwind CSS v4 + HTMX + Anti-Flash Dark Mode Script)  │
└────────────────────────────┬─────────────────────────────┘
                             │ HTTP / SSE / Forms
┌────────────────────────────▼─────────────────────────────┐
│                 Spring Boot 3 + Kotlin                   │
│  Controllers ──► Services ──► Thymeleaf Template Engine  │
└────────────────────────────┬─────────────────────────────┘
                             │ Optional JPA / SQL
┌────────────────────────────▼─────────────────────────────┐
│                  PostgreSQL / Flyway                     │
└──────────────────────────────────────────────────────────┘
```

## Quick Links

Get up and running in minutes:

- [Installation](/docs/installation) — Prerequisites, cloning, and local environment setup.
- [Quick Start](/docs/quickstart) — Launch your application and customize your brand in 5 minutes.
- [Project Structure](/docs/structure) — Understand the directory layout and Kotlin architecture.
- [Keymaps & Cheatsheet](/docs/keymaps) — Keyboard shortcuts and CLI productivity commands.
