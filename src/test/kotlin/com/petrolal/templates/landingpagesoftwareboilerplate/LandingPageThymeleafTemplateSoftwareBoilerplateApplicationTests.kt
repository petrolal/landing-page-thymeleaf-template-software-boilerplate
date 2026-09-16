package com.petrolal.templates.landingpagesoftwareboilerplate

import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class LandingPageThymeleafTemplateSoftwareBoilerplateApplicationTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun contextLoads() {
    }

    @Test
    fun `homepage should render successfully with landing content`() {
        mockMvc
            .perform(get("/"))
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("AuraLaunch")))
            .andExpect(content().string(containsString("Features")))
            .andExpect(content().string(containsString("Documentation")))
            .andExpect(content().string(containsString("Buy Me a Coffee")))
            .andExpect(content().string(containsString("buymeacoffee.com/petrolal")))
            .andExpect(content().string(containsString("title=\"LinkedIn\"")))
            .andExpect(content().string(containsString("title=\"Medium\"")))
            .andExpect(content().string(containsString("title=\"Discord Community\"")))
    }

    @Test
    fun `robots txt should return search engine directives`() {
        mockMvc
            .perform(get("/robots.txt"))
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("User-agent: *")))
            .andExpect(content().string(containsString("Sitemap:")))
    }

    @Test
    fun `sitemap xml should return xml urlset`() {
        mockMvc
            .perform(get("/sitemap.xml"))
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("<urlset")))
    }

    @Test
    fun `newsletter subscription should return success fragment on valid email`() {
        mockMvc
            .perform(
                post("/api/leads/newsletter")
                    .param("email", "test@example.com"),
            ).andExpect(status().isOk)
            .andExpect(content().string(containsString("Thank you")))
    }

    @Test
    fun `newsletter subscription should reject invalid email`() {
        mockMvc
            .perform(
                post("/api/leads/newsletter")
                    .param("email", "invalid-email"),
            ).andExpect(status().isOk)
            .andExpect(content().string(containsString("Please provide a valid email")))
    }

    @Test
    fun `contact inquiry should return success fragment on valid input`() {
        mockMvc
            .perform(
                post("/api/leads/contact")
                    .param("name", "Alice")
                    .param("email", "alice@example.com")
                    .param("subject", "General Inquiry")
                    .param("message", "Hello from automated test!"),
            ).andExpect(status().isOk)
            .andExpect(content().string(containsString("Thank you for reaching out")))
    }

    @Test
    fun `docs index should redirect to first doc page`() {
        mockMvc
            .perform(get("/docs"))
            .andExpect(status().is3xxRedirection)
    }

    @Test
    fun `doc page overview should render successfully`() {
        mockMvc
            .perform(get("/docs/overview"))
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("AuraLaunch Documentation")))
            .andExpect(content().string(containsString("Getting Started")))
            .andExpect(content().string(containsString("On this page")))
            .andExpect(content().string(containsString("search-modal")))
    }

    @Test
    fun `doc page keymaps should render cheatsheet shortcuts`() {
        mockMvc
            .perform(get("/docs/keymaps"))
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("Keymaps &amp; Cheatsheet")))
            .andExpect(content().string(containsString("Ctrl")))
    }

    @Test
    fun `doc search api should return matching results`() {
        mockMvc
            .perform(get("/docs/api/search").param("q", "installation"))
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("Installation")))
    }

    @Test
    fun `doc page should return 404 for unknown slug`() {
        mockMvc
            .perform(get("/docs/non-existent-slug-xyz"))
            .andExpect(status().isNotFound)
    }
}
