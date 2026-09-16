package com.petrolal.templates.landingpagesoftwareboilerplate.controller

import com.petrolal.templates.landingpagesoftwareboilerplate.config.LandingPageProperties
import com.petrolal.templates.landingpagesoftwareboilerplate.docs.DocService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class SeoController(
    private val landingPageProperties: LandingPageProperties,
    private val docService: DocService,
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

        val docUrls =
            docService.getAllPages().joinToString("\n") { page ->
                """
                <url>
                    <loc>$siteUrl/docs/${page.slug}</loc>
                    <lastmod>$today</lastmod>
                    <changefreq>weekly</changefreq>
                    <priority>0.8</priority>
                </url>
                """.trimIndent()
            }

        return """
            <?xml version="1.0" encoding="UTF-8"?>
            <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
                <url>
                    <loc>$siteUrl/</loc>
                    <lastmod>$today</lastmod>
                    <changefreq>weekly</changefreq>
                    <priority>1.0</priority>
                </url>
                <url>
                    <loc>$siteUrl/docs</loc>
                    <lastmod>$today</lastmod>
                    <changefreq>weekly</changefreq>
                    <priority>0.9</priority>
                </url>
                $docUrls
            </urlset>
            """.trimIndent()
    }
}
