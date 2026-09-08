package com.petrolal.templates.landingpagesoftwareboilerplate.controller

import com.petrolal.templates.landingpagesoftwareboilerplate.config.LandingPageProperties
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute
import java.time.Year

@ControllerAdvice
class GlobalModelAdvice(
    private val landingPageProperties: LandingPageProperties,
) {
    @ModelAttribute("landing")
    fun landingProperties(): LandingPageProperties = landingPageProperties

    @ModelAttribute("currentYear")
    fun currentYear(): Int = Year.now().value
}
