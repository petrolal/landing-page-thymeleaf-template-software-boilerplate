package com.petrolal.templates.landingpagefirst.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "landing")
data class LandingPageProperties(
    var site: SiteProperties = SiteProperties(),
    var social: SocialProperties = SocialProperties(),
    var hero: HeroProperties = HeroProperties(),
    var analytics: AnalyticsProperties = AnalyticsProperties(),
)

data class SiteProperties(
    var name: String = "AuraLaunch",
    var title: String = "AuraLaunch - Modern Landing Page Template",
    var tagline: String = "Build, launch, and convert visitors at lightning speed.",
    var description: String = "Production-ready Spring Boot 3 & Kotlin landing page template.",
    var url: String = "https://example.com",
    var author: String = "Petrolal",
    var ogImage: String = "/images/og-preview.png",
)

data class SocialProperties(
    var twitter: String = "",
    var github: String = "",
    var linkedin: String = "",
    var discord: String = "",
)

data class HeroProperties(
    var badge: String = "✨ Ready for Spring Boot 3 & Kotlin 2.1",
    var ctaText: String = "Start Building",
    var ctaLink: String = "#pricing",
    var secondaryCtaText: String = "Explore Features",
    var secondaryCtaLink: String = "#features",
)

data class AnalyticsProperties(
    var googleAnalyticsId: String = "",
    var googleTagManagerId: String = "",
    var plausibleDomain: String = "",
    var posthogKey: String = "",
    var posthogHost: String = "https://us.i.posthog.com",
)
