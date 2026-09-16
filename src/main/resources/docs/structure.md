---
title: "Project Structure"
description: "A comprehensive tour of the directory layout, backend Kotlin packages, and frontend templates."
category: "Getting Started"
order: 4
badge: "Reference"
---

A clean, modular layout makes it effortless to navigate and scale the codebase.

## Directory Tree

```
├── .bin/                        # Native standalone Tailwind CLI binary (auto-downloaded)
├── .github/workflows/           # GitHub Actions CI/CD workflows
├── gradle/                      # Gradle wrapper and version catalog (libs.versions.toml)
├── src/
│   ├── main/
│   │   ├── kotlin/com/petrolal/templates/landingpagesoftwareboilerplate/
│   │   │   ├── config/          # Spring configuration & LandingPageProperties
│   │   │   ├── controller/      # Web MVC controllers (HomeController, LeadController, SeoController)
│   │   │   ├── docs/            # Documentation engine (DocService, DocController, DocMarkdownParser)
│   │   │   ├── dto/             # Data Transfer Objects & form validation models
│   │   │   └── service/         # Business logic & lead management
│   │   └── resources/
│   │       ├── application.yaml # Unified configuration for site, hero, analytics, and docs
│   │       ├── docs/            # Markdown (.md) documentation files
│   │       ├── static/          # Static assets: css (input.css, style.css), images, icons
│   │       └── templates/       # Thymeleaf HTML templates
│   │           ├── fragments/   # Landing page modular fragments (navbar, hero, pricing, etc.)
│   │           ├── docs/        # Documentation layout and fragments (sidebar, toc, modal)
│   │           └── index.html   # Main landing page view
│   └── test/                    # Unit and integration test suite
├── Dockerfile                   # Multi-stage production container build
├── docker-compose.yml           # Local multi-service orchestration
└── build.gradle.kts             # Gradle build script with Tailwind compilation tasks
```

## Backend Architecture

The Kotlin backend is organized cleanly into domain-driven packages:

- `config`: Handles type-safe `@ConfigurationProperties` binding from `application.yaml`.
- `controller`: Exposes endpoints for rendering views and handling HTMX AJAX requests.
- `docs`: Embedded documentation engine with Markdown parsing, GFM tables, frontmatter, and search indexing.
- `dto`: Request objects with Jakarta validation annotations (`@NotBlank`, `@Email`).
- `service`: Business layer responsible for processing inquiries, sending webhooks, and interacting with databases.

## Frontend Layout

The frontend leverages Thymeleaf fragments for maximum reusability:

- `layout.html`: Common HTML head with SEO tags, Tailwind CSS link, and dark-mode initialization script.
- `fragments/`: Individual sections of the landing page:
  - `navbar.html`: Sticky navigation bar with dark mode toggle and responsive mobile drawer.
  - `hero.html`: Conversion-focused hero header with CTA buttons and social badges.
  - `features.html`: Modern Bento-grid layout highlighting key capabilities.
  - `pricing.html`: Interactive pricing cards with tier comparison.
  - `faq.html`: Accessible FAQ accordion list.
  - `contact.html`: Async lead capture form powered by HTMX.
  - `footer.html`: Multi-column footer with social links and copyright.
- `docs/`: Dedicated, full-featured documentation templates with sidebar navigation, TOC scrollspy, and quick search.
