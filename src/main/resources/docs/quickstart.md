---
title: "Quick Start"
description: "Customize your branding, configure marketing copy, and launch your application in under 5 minutes."
category: "Getting Started"
order: 3
badge: "5 min"
---

This 5-minute tutorial will guide you through customizing the template for your own SaaS or software product.

## 1. Update Brand & Site Metadata

Open `src/main/resources/application.yaml` and update the site configuration:

```yaml
landing:
  site:
    name: "MyProduct"
    title: "MyProduct — The Fastest Way to Ship"
    tagline: "Supercharge your development workflow today."
    description: "Modern developer tool designed for high-performance engineering teams."
    url: "https://myproduct.io"
    author: "Your Name or Company"
    og-image: "/images/og-preview.png"
  social:
    twitter: "https://twitter.com/myproduct"
    github: "https://github.com/myorg/myproduct"
    linkedin: "https://linkedin.com/company/myproduct"
    medium: "https://medium.com/@myproduct"
    discord: "https://discord.gg/myproduct"
```

These values automatically populate the Navbar, Hero banner, Footer, OpenGraph cards, Twitter preview cards, and JSON-LD schema!

## 2. Customize the Hero Section

Edit `src/main/resources/templates/fragments/hero.html` or adjust the hero properties in `application.yaml`:

```yaml
landing:
  hero:
    badge: "⚡ 100% Free & Open Source • GNU GPL v3.0"
    cta-text: "Explore Documentation"
    cta-link: "/docs"
    secondary-cta-text: "Buy Me a Coffee ☕"
    secondary-cta-link: "https://buymeacoffee.com/petrolal"
```

## 3. Adding Documentation Pages

Creating a new documentation page is as simple as adding a `.md` file to `src/main/resources/docs/`:

```markdown
---
title: "API Reference"
description: "Comprehensive guide to our REST and Webhook APIs."
category: "Configuration"
order: 5
badge: "API"
---

# API Reference

Here is how you authenticate and interact with our endpoints:

:::tip Authentication Header
Pass your Bearer token in the `Authorization` header.
:::

```bash
curl -X GET "https://api.myproduct.io/v1/status" \
  -H "Authorization: Bearer YOUR_API_KEY"
```
```

The documentation engine will automatically parse the frontmatter, register the page, insert it into the navigation tree, generate table-of-contents links, and update the <kbd>Ctrl</kbd> + <kbd>K</kbd> search index!

## 4. Run Code Quality Checks

Verify that your project adheres to ktlint rules and passes all tests:

```bash
./gradlew check
```

:::tip Auto-format Code
Run ktlint format task to automatically format any Kotlin files according to official Kotlin style guidelines:
```bash
./gradlew ktlintFormat
```
:::
