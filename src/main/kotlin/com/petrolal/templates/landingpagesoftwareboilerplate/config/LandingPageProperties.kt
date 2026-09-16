package com.petrolal.templates.landingpagesoftwareboilerplate.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "landing")
data class LandingPageProperties(
    var site: SiteProperties = SiteProperties(),
    var social: SocialProperties = SocialProperties(),
    var hero: HeroProperties = HeroProperties(),
    var analytics: AnalyticsProperties = AnalyticsProperties(),
    var docs: DocsProperties = DocsProperties(),
    var sponsor: SponsorProperties = SponsorProperties(),
)

data class SiteProperties(
    var name: String = "AuraLaunch",
    var title: String = "AuraLaunch — Open Source Developer Platform & Boilerplate",
    var tagline: String = "High-performance open source software development powered by Spring Boot, Kotlin, and HTMX.",
    var description: String =
        "100% Free and Open Source Spring Boot 3 & Kotlin boilerplate with built-in LazyVim-style docs and Tailwind v4.",
    var url: String = "https://example.com",
    var author: String = "Petrolal",
    var ogImage: String = "/images/og-preview.png",
    var license: String = "GNU General Public License v3.0",
)

data class SocialProperties(
    var twitter: String = "",
    var github: String = "",
    var linkedin: String = "",
    var medium: String = "",
    var discord: String = "",
)

data class HeroProperties(
    var badge: String = "⚡ 100% Free & Open Source • GNU GPL v3.0",
    var ctaText: String = "Read Documentation",
    var ctaLink: String = "/docs",
    var secondaryCtaText: String = "Star on GitHub",
    var secondaryCtaLink: String = "https://github.com/petrolal/landing-page-thymeleaf-template-software-boilerplate",
)

data class SponsorProperties(
    var buyMeACoffee: String = "https://buymeacoffee.com/petrolal",
    var githubSponsors: String = "https://github.com/sponsors/petrolal",
    var targetGoal: String = "Support Open Source Tooling & Boilerplates",
)

data class AnalyticsProperties(
    var googleAnalyticsId: String = "",
    var googleTagManagerId: String = "",
    var plausibleDomain: String = "",
    var posthogKey: String = "",
    var posthogHost: String = "https://us.i.posthog.com",
)

data class DocsProperties(
    var enabled: Boolean = true,
    var title: String = "Documentation",
    var version: String = "v1.0.0",
    var githubRepo: String = "https://github.com/petrolal/landing-page-thymeleaf-template-software-boilerplate",
    var branch: String = "main",
)
