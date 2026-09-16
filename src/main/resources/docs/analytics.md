---
title: "Analytics & Tracking"
description: "Connecting privacy-first and enterprise analytics platforms with zero code modification."
category: "Features & Plugins"
order: 4
badge: "Integrations"
---

AuraLaunch provides clean, modular integrations with major web analytics tools. Snippets are rendered only when the corresponding ID is configured in `application.yaml`.

## Supported Platforms

### 1. Google Analytics 4 (GA4)

Add your Measurement ID (format: `G-XXXXXXXXXX`):

```yaml
landing:
  analytics:
    google-analytics-id: "G-XXXXXXXXXX"
```

### 2. Google Tag Manager (GTM)

Add your GTM Container ID (format: `GTM-XXXXXXX`):

```yaml
landing:
  analytics:
    google-tag-manager-id: "GTM-XXXXXXX"
```

### 3. Plausible Analytics (Privacy-First)

Add your registered custom domain:

```yaml
landing:
  analytics:
    plausible-domain: "yourdomain.com"
```

### 4. PostHog (Product Analytics & Session Replay)

Add your Project API Key and optional custom ingestion host:

```yaml
landing:
  analytics:
    posthog-key: "phc_your_key_here"
    posthog-host: "https://us.i.posthog.com"
```

:::tip GDPR & Cookie Consent
AuraLaunch includes a GDPR cookie consent banner in `src/main/resources/templates/fragments/cookie-banner.html` that automatically remembers user preferences in `localStorage`.
:::
