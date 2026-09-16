---
title: "Styling & Themes"
description: "How Tailwind CSS v4 is compiled, dark mode implementation, and customizing design tokens."
category: "Configuration"
order: 2
badge: "Tailwind v4"
---

AuraLaunch uses the latest **Tailwind CSS v4** styling engine compiled via Gradle's automated standalone binary download.

## Tailwind CSS v4 Configuration

Tailwind v4 replaces the legacy `tailwind.config.js` with simple CSS `@import` directives and theme extensions in `src/main/resources/static/css/input.css`:

```css
@import "tailwindcss";

@layer base {
  html {
    scroll-behavior: smooth;
  }
}
```

## Gradle Compilation Tasks

The Gradle build script defines three main Tailwind tasks:

- `downloadTailwindCli`: Automatically fetches the correct binary for Linux, macOS, or Windows into `.bin/`.
- `buildTailwind`: Compiles and minifies `input.css` into `style.css`.
- `tailwindWatch`: Watches all `.html` and `.css` files and triggers fast incremental rebuilds.

```bash
# Rebuild CSS once
./gradlew buildTailwind

# Start watch mode during development
./gradlew tailwindWatch
```

## Dark Mode & Anti-Flash Script

Dark mode uses the `dark` class on the `<html>` root element. An inline script in `src/main/resources/templates/fragments/layout.html` executes synchronously before the DOM renders to prevent Flash of Unstyled Content (FOUC):

```javascript
(function() {
  try {
    const theme = localStorage.getItem('theme');
    const isDark = theme === 'dark' || (!theme && window.matchMedia('(prefers-color-scheme: dark)').matches);
    if (isDark) {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  } catch (_) {}
})();
```

:::tip Palette Customization
Classes like `bg-slate-900`, `text-indigo-600`, and `border-slate-800` are used across the template. You can customize them by tweaking the Tailwind classes in your fragments or adding custom CSS variables.
:::
