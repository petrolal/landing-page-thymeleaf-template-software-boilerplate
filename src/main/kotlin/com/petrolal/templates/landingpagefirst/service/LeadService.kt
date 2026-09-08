package com.petrolal.templates.landingpagefirst.service

import com.petrolal.templates.landingpagefirst.dto.ContactRequest
import com.petrolal.templates.landingpagefirst.dto.NewsletterRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.CopyOnWriteArrayList

sealed class LeadResult {
    data class Success(
        val message: String,
    ) : LeadResult()

    data class Error(
        val message: String,
    ) : LeadResult()
}

@Service
class LeadService {
    private val logger = LoggerFactory.getLogger(LeadService::class.java)

    // In-memory store for demonstration. Switch to JPA Repository when database is enabled.
    private val newsletterSubscribers = CopyOnWriteArrayList<String>()
    private val contactMessages = CopyOnWriteArrayList<ContactRequest>()

    fun subscribeNewsletter(request: NewsletterRequest): LeadResult {
        // Honeypot anti-spam check: bots fill hidden inputs
        if (!request.honeypot.isNullOrBlank()) {
            logger.warn("Bot submission detected via honeypot: ${request.email}")
            return LeadResult.Success("Thank you for subscribing!") // Deceive bot
        }

        val email = request.email.trim()
        if (email.isBlank() || !isValidEmail(email)) {
            return LeadResult.Error("Please provide a valid email address.")
        }

        if (newsletterSubscribers.contains(email)) {
            return LeadResult.Success("You are already subscribed to our updates!")
        }

        newsletterSubscribers.add(email)
        logger.info("New newsletter subscriber registered: $email (total: ${newsletterSubscribers.size})")

        return LeadResult.Success("Thank you! You've been successfully subscribed.")
    }

    fun submitContact(request: ContactRequest): LeadResult {
        // Honeypot anti-spam check
        if (!request.honeypot.isNullOrBlank()) {
            logger.warn("Bot contact submission detected via honeypot: ${request.email}")
            return LeadResult.Success("Thank you for reaching out!")
        }

        val name = request.name.trim()
        val email = request.email.trim()
        val message = request.message.trim()

        if (name.isBlank()) {
            return LeadResult.Error("Please provide your name.")
        }
        if (email.isBlank() || !isValidEmail(email)) {
            return LeadResult.Error("Please provide a valid email address.")
        }
        if (message.isBlank() || message.length < 5) {
            return LeadResult.Error("Please provide a message with at least 5 characters.")
        }

        val cleaned = request.copy(name = name, email = email, message = message)
        contactMessages.add(cleaned)
        logger.info("New contact inquiry from $name <$email>: ${request.subject} (total: ${contactMessages.size})")

        return LeadResult.Success("Thank you for reaching out! We'll get back to you shortly.")
    }

    private fun isValidEmail(email: String): Boolean = email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"))
}
