# AuraLaunch — Open Source Developer Platform & Boilerplate

A modern, production-ready developer platform and software landing page template built with **Spring Boot 3.4**, **Kotlin 2.1**, **Thymeleaf**, **HTMX 2.0**, and **Tailwind CSS v4** (Zero Node.js). 

Featuring a built-in **LazyVim-style documentation engine** (`/docs`), automated CI/CD pipelines, dark mode color theme, and one-command scaffolding via **[JBang](https://www.jbang.dev/)**.

---

## ⚡ Quick Start: 3 Ways to Download & Edit

### Option 1: One-Command Scaffolding with JBang (Recommended)

You can generate a fresh, customized copy of this boilerplate without manually cloning or renaming packages:

```bash
# 1. Install JBang (if not already installed)
curl -Ls https://sh.jbang.dev | bash -s - app setup   # Linux / macOS
# or: sdk install jbang / brew install jbangdev/tap/jbang

# 2. Scaffold your project
jbang init@petrolal/landing-page-thymeleaf-template-software-boilerplate my-project

# Or specify a custom target package:
jbang init@petrolal/landing-page-thymeleaf-template-software-boilerplate \
  --package com.mycompany.app \
  my-company-app
```

### Option 2: Clone with Git

```bash
git clone https://github.com/petrolal/landing-page-thymeleaf-template-software-boilerplate.git my-project
cd my-project
```

### Option 3: GitHub Template Repository

Click the **"Use this template"** button at the top of the GitHub repository to create a new repository under your account.

---

## 🚀 Running Your Project Locally

```bash
cd my-project

# Run the development server
./gradlew bootRun
```

- **Landing Page**: [http://localhost:8080](http://localhost:8080)
- **Documentation**: [http://localhost:8080/docs](http://localhost:8080/docs)
- **Swagger OpenAPI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 📚 Built-in LazyVim-Style Documentation (`/docs`)

Ship documentation directly alongside your code:

- **Adding Documentation Pages**: Add Markdown files (`.md`) to `src/main/resources/docs/`.
- **Frontmatter Support**: Set title, category, description, order, and badges in YAML frontmatter.
- **Instant Search Dialog**: Press <kbd>Ctrl</kbd> + <kbd>K</kbd> or <kbd>/</kbd> to open instant full-text search.
- **Callouts / Admonitions**: Use `:::tip`, `:::warning`, `:::danger`, or `:::info` blocks.
- **TOC & Scrollspy**: Dynamic table of contents with automatic scroll position tracking.

```markdown
---
title: "Custom Module"
description: "How to configure and build custom modules."
category: "Guide"
order: 2
badge: "New"
---

# Custom Module

Here is how you initialize the module:

:::tip Performance Tip
Compile Tailwind CSS using `./gradlew buildTailwind` for fast asset builds.
:::
```

---

## ⚙️ Configuration & Customization

All site branding, social links, hero text, and doc settings are customized in `src/main/resources/application.yaml`:

```yaml
landing:
  site:
    name: "MyProduct"
    title: "MyProduct — Open Source Developer Platform"
    tagline: "High-performance software built with Kotlin & Spring Boot."
    license: "GNU General Public License v3.0"
  social:
    github: "https://github.com/yourname/repo"
    linkedin: "https://linkedin.com/in/yourprofile"
    medium: "https://medium.com/@yourprofile"
    twitter: "https://twitter.com/yourhandle"
    discord: "https://discord.gg/yourserver"
  sponsor:
    buy-me-a-coffee: "https://buymeacoffee.com/youraccount"
  docs:
    enabled: true
    title: "MyProduct Docs"
    version: "v1.0.0"
```

---

## 🛠️ Local Development & Build Commands

| Command | Description |
|---|---|
| `./gradlew bootRun` | Start local Spring Boot application |
| `./gradlew buildTailwind` | Compile Tailwind CSS v4 standalone binary |
| `./gradlew tailwindWatch` | Watch and live recompile Tailwind CSS changes |
| `./gradlew check` | Run unit tests and ktlint verification |
| `./gradlew ktlintFormat` | Auto-format Kotlin source code |
| `./gradlew bootJar` | Package production executable JAR |
| `./gradlew dockerBuild` | Build Docker container image |
| `docker compose up -d postgres` | Start local PostgreSQL database container |

---

## 🎨 Tech Stack

- **Kotlin 2.1** with strict null-safety and type-safe configuration.
- **Spring Boot 3.4** (Web, Thymeleaf, DevTools, Validation).
- **HTMX 2.0** for reactive AJAX form swaps without frontend framework bloat.
- **Tailwind CSS v4 Standalone** (zero Node.js or npm dependencies).
- **CommonMark & GFM Tables** for dynamic markdown documentation rendering.
- **Ktlint** plugin configured for code styling consistency.
- **PostgreSQL & Flyway** migrations ready out-of-the-box.
- **Docker & Docker Compose** production configuration included.

---

## ☕ Support & Sponsoring

If this template saved you hours of boilerplate configuration, consider supporting ongoing open-source maintenance:

- **Buy Me a Coffee**: [buymeacoffee.com/petrolal](https://buymeacoffee.com/petrolal)
- **GitHub Sponsors**: [github.com/sponsors/petrolal](https://github.com/sponsors/petrolal)

---

## ⚖️ License

Distributed under the **[GNU General Public License v3.0](LICENSE)** (GNU GPLv3).
