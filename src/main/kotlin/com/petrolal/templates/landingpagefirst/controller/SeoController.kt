package com.petrolal.templates.landingpagefirst.controller

import com.petrolal.templates.landingpagefirst.config.LandingPageProperties
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class SeoController(
    private val landingPageProperties: LandingPageProperties,
) {
    @GetMapping(value = ["/robots.txt"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun robotsTxt(): String {
        val siteUrl = landingPageProperties.site.url.trimEnd('/')
        return """
            User-agent: *
            Allow: /
            
            Sitemap: $siteUrl/sitemap.xml
            """.trimIndent()
    }

    @GetMapping(value = ["/sitemap.xml"], produces = [MediaType.APPLICATION_XML_VALUE])
    fun sitemapXml(): String {
        val siteUrl = landingPageProperties.site.url.trimEnd('/')
        val today = LocalDate.now().toString()
        return """
            <?xml version="1.0" encoding="UTF-8"?>
            <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
                <url>
                    <loc>$siteUrl/</loc>
                    <lastmod>$today</lastmod>
                    <changefreq>weekly</changefreq>
                    <priority>1.0</priority>
                </url>
            </urlset>
            """.trimIndent()
    }
}
