package com.petrolal.templates.landingpagefirst.controller

import com.petrolal.templates.landingpagefirst.dto.ContactRequest
import com.petrolal.templates.landingpagefirst.dto.NewsletterRequest
import com.petrolal.templates.landingpagefirst.service.LeadResult
import com.petrolal.templates.landingpagefirst.service.LeadService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api/leads")
class LeadController(
    private val leadService: LeadService,
) {
    @PostMapping("/newsletter")
    fun handleNewsletter(
        @ModelAttribute request: NewsletterRequest,
        model: Model,
    ): String =
        when (val result = leadService.subscribeNewsletter(request)) {
            is LeadResult.Success -> {
                model.addAttribute("message", result.message)
                "fragments/forms :: newsletter-success"
            }
            is LeadResult.Error -> {
                model.addAttribute("error", result.message)
                model.addAttribute("request", request)
                "fragments/forms :: newsletter-error"
            }
        }

    @PostMapping("/contact")
    fun handleContact(
        @ModelAttribute request: ContactRequest,
        model: Model,
    ): String =
        when (val result = leadService.submitContact(request)) {
            is LeadResult.Success -> {
                model.addAttribute("message", result.message)
                "fragments/forms :: contact-success"
            }
            is LeadResult.Error -> {
                model.addAttribute("error", result.message)
                model.addAttribute("contactRequest", request)
                "fragments/forms :: contact-error"
            }
        }
}
