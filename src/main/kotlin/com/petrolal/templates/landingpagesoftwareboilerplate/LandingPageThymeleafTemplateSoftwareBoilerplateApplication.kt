package com.petrolal.templates.landingpagesoftwareboilerplate

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class LandingPageThymeleafTemplateSoftwareBoilerplateApplication

fun main(args: Array<String>) {
    runApplication<LandingPageThymeleafTemplateSoftwareBoilerplateApplication>(*args)
}
