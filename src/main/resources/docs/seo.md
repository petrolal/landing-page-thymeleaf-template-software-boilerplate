---
title: "SEO & Metadata"
description: "Optimizing search engine indexation, social sharing preview cards, sitemap, and robots.txt."
category: "Configuration"
order: 3
badge: "Optimization"
---

Search engine optimization and high-quality social sharing cards are pre-configured out of the box.

## OpenGraph & Twitter Cards

In `src/main/resources/templates/fragments/seo.html`, dynamic meta tags are injected on every page request:

- Standard HTML meta tags (`title`, `description`, `author`, `robots`, `viewport`).
- Open Graph tags (`og:title`, `og:description`, `og:url`, `og:image`, `og:type`).
- Twitter Summary Large Image cards (`twitter:card`, `twitter:site`, `twitter:creator`).
- Canonical URL link.

## Dynamic Robots.txt and Sitemap.xml

The `SeoController` automatically serves dynamically generated search engine directives:

- `/robots.txt`: Allows search engines and points to the sitemap.
- `/sitemap.xml`: Generates an XML sitemap referencing all routes including landing sections and documentation URLs.

```kotlin
@Controller
class SeoController(
    private val landingProperties: LandingPageProperties,
) {
    @GetMapping("/robots.txt", produces = [MediaType.TEXT_PLAIN_VALUE])
    @ResponseBody
    fun robots(): String = """
        User-agent: *
        Allow: /
        Sitemap: ${landingProperties.site.url}/sitemap.xml
    """.trimIndent()
}
```

:::tip Structured Data / JSON-LD
To enhance search snippet rankings, you can embed Schema.org JSON-LD structured data in the `<head>` of your layout fragments.
:::
