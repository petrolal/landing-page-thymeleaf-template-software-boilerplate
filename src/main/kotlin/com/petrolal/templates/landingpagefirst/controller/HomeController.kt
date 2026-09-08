package com.petrolal.templates.landingpagefirst.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {
    @GetMapping
    fun index(model: Model): String {
        model.addAttribute("title", "Home Page")
        return "index"
    }
}
