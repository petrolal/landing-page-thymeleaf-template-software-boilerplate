package com.petrolal.templates.landingpagesoftwareboilerplate.docs

import com.fasterxml.jackson.databind.ObjectMapper
import com.petrolal.templates.landingpagesoftwareboilerplate.config.LandingPageProperties
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.server.ResponseStatusException

@Controller
@RequestMapping("/docs")
class DocController(
    private val docService: DocService,
    private val landingProperties: LandingPageProperties,
    private val objectMapper: ObjectMapper,
) {
    @GetMapping
    fun index(): String {
        val firstSlug = docService.getFirstPageSlug()
        return "redirect:/docs/$firstSlug"
    }

    @GetMapping("/{slug}")
    fun getDocPage(
        @PathVariable slug: String,
        model: Model,
    ): String {
        val page =
            docService.getPage(slug)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Documentation page '$slug' not found")

        val categories = docService.getCategories()
        val allSearchItems =
            docService.search("").ifEmpty {
                // Get all indexed items for instant client-side searching
                docService.getAllPages().map {
                    SearchItem(
                        title = it.title,
                        category = it.category,
                        snippet = it.description,
                        slug = it.slug,
                        url = it.url,
                    )
                }
            }

        model.addAttribute("page", page)
        model.addAttribute("categories", categories)
        model.addAttribute("activeSlug", slug)
        model.addAttribute("pageTitle", "${page.title} | ${landingProperties.docs.title}")
        model.addAttribute("pageDescription", page.description)
        model.addAttribute("docs", landingProperties.docs)
        model.addAttribute("searchIndexJson", objectMapper.writeValueAsString(allSearchItems))

        return "docs/page"
    }

    @GetMapping("/api/search")
    @ResponseBody
    fun search(
        @RequestParam(name = "q", defaultValue = "") query: String,
    ): List<SearchItem> = docService.search(query)
}
