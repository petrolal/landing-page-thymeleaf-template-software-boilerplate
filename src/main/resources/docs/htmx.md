---
title: "HTMX Patterns"
description: "Leveraging HTMX with Thymeleaf for dynamic UI updates, partial swaps, and modern web UX."
category: "Features & Plugins"
order: 2
badge: "Interactive"
---

HTMX allows you to build rich, reactive user interfaces with standard HTML attributes instead of client-heavy single-page application (SPA) frameworks.

## Why HTMX + Spring Boot?

- **Zero Build Steps for UI logic**: No bundlers, hydration bugs, or complex state stores.
- **Server Authority**: Validation, security, and rendering remain securely on the server.
- **Micro-Fragments**: Thymeleaf can render and return individual fragments (e.g. `return "fragments/pricing :: tier-modal"`) directly to HTMX targets.

## Common HTMX Patterns

### 1. Active Search / Live Filter

Filter lists in real time as the user types:

```html
<input type="text"
       name="q"
       placeholder="Search..."
       hx-get="/api/search"
       hx-trigger="keyup changed delay:300ms, search"
       hx-target="#search-results"
       hx-indicator="#search-indicator" />
```

### 2. Click-to-Edit & In-Place Updates

Load and replace an edit form inline:

```html
<div id="contact-card">
  <p>Name: Petrolal</p>
  <button hx-get="/contact/1/edit" hx-target="#contact-card" hx-swap="outerHTML">
    Edit
  </button>
</div>
```

### 3. Polling and Real-Time Notifications

Poll a server endpoint every 5 seconds for status updates:

```html
<div hx-get="/api/status" hx-trigger="every 5s">
  <span class="badge">Checking status...</span>
</div>
```

:::tip HTMX Debugging
Open your browser developer console and run `htmx.logAll()` to observe all HTMX events and network lifecycle hooks.
:::
