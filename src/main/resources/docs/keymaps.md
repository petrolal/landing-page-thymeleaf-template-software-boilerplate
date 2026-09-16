---
title: "Keymaps & Cheatsheet"
description: "LazyVim-inspired keyboard shortcuts, documentation hotkeys, and development CLI commands."
category: "Features & Plugins"
order: 3
badge: "Cheatsheet"
---

AuraLaunch incorporates keyboard-first navigation principles inspired by tools like **LazyVim**, **Vim**, and modern developer documentation sites.

## Documentation Navigation Hotkeys

Use these keyboard shortcuts anywhere across the documentation to navigate effortlessly:

| Key Combination | Action | Description |
| :--- | :--- | :--- |
| <kbd>Ctrl</kbd> + <kbd>K</kbd> / <kbd>Cmd</kbd> + <kbd>K</kbd> | Open Search Dialog | Opens fuzzy quick-search across all docs pages & sections |
| <kbd>/</kbd> | Quick Search | Focuses and opens the documentation search modal |
| <kbd>Esc</kbd> | Close Modal / Drawer | Dismisses search modal or mobile navigation drawer |
| <kbd>↑</kbd> / <kbd>↓</kbd> | Navigate Search | Move selection up and down in search results |
| <kbd>Enter</kbd> | Select Item | Open the highlighted search result |
| <kbd>T</kbd> | Toggle Theme | Switches between Dark Mode and Light Mode |

## Gradle Development CLI Cheatsheet

| Command | Shortcut / Description |
| :--- | :--- |
| `./gradlew bootRun` | Starts the local Spring Boot development server on port 8080 |
| `./gradlew tailwindWatch` | Starts continuous Tailwind CSS v4 compiler in watch mode |
| `./gradlew buildTailwind` | Compiles and minifies `style.css` once |
| `./gradlew check` | Runs all unit tests, integration tests, and ktlint style checks |
| `./gradlew ktlintFormat` | Automatically fixes all Kotlin lint and formatting discrepancies |
| `./gradlew bootJar` | Packages standalone production executable JAR in `build/libs/` |
| `./gradlew dockerBuild` | Builds local Docker container image |
| `./gradlew dockerRun` | Executes the containerized application on port 8080 |

:::tip Terminal Aliases
Add these aliases to your `~/.zshrc` or `~/.bashrc` for maximum velocity:
```bash
alias gbr="./gradlew bootRun"
alias gtw="./gradlew tailwindWatch"
alias gchk="./gradlew check"
alias gfmt="./gradlew ktlintFormat"
```
:::
