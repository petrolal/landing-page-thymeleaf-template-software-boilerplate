package com.petrolal.templates.landingpagefirst

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class LandingPageThymeleafTemplateFirstApplication

fun main(args: Array<String>) {
    runApplication<LandingPageThymeleafTemplateFirstApplication>(*args)
}
