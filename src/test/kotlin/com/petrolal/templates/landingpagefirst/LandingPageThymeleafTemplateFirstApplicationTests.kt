package com.petrolal.templates.landingpagefirst

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
class LandingPageThymeleafTemplateFirstApplicationTests {
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
            .andExpect(content().string(containsString("Pricing")))
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
}
