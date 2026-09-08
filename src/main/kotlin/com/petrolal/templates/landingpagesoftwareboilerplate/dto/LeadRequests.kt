package com.petrolal.templates.landingpagesoftwareboilerplate.dto

data class NewsletterRequest(
    var email: String = "",
    var honeypot: String? = null,
)

data class ContactRequest(
    var name: String = "",
    var email: String = "",
    var subject: String = "General Inquiry",
    var message: String = "",
    var honeypot: String? = null,
)
