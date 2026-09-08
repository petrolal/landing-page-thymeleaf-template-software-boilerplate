plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.framework.boot)
    alias(libs.plugins.spring.dependency.manager)
    alias(libs.plugins.ktlint)
}

group = "com.petrolal.templates"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.spring.boot.starter)
    implementation(libs.bundles.kotlin)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    // Database
    implementation(libs.spring.boot.starter.data.jpa)
    runtimeOnly(libs.postgresql)
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)

    // Tests
    testImplementation(libs.bundles.test)
    testCompileOnly(libs.lombok)
    testRuntimeOnly(libs.junit.platform.launcher)
    testAnnotationProcessor(libs.lombok)

    // Frontend
    implementation(libs.htmx)
    implementation(libs.webjars.locator)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

/**
 * Docker configuration
 * Configure Docker for gradlew execution
 */
val dockerBuild =
    tasks.register<Exec>("dockerBuild") {
        dependsOn(tasks.bootJar)
        group = "docker"
        description = "Builds the Docker image using local Docker daemon"

        commandLine(
            "docker",
            "build",
            "-t",
            "landing-page-thymeleaf-template-first:latest",
            ".",
        )
    }

val dockerRun =
    tasks.register<Exec>("dockerRun") {
        dependsOn("dockerBuild")
        group = "docker"
        description = "Runs the Docker container locally"

        commandLine(
            "docker",
            "run",
            "--rm",
            "-p",
            "8080:8080",
            "--name",
            "landing-page-thymeleaf-template-first",
            "landing-page-thymeleaf-template-first:latest",
        )
    }

tasks.withType<Test> {
    useJUnitPlatform()
}
