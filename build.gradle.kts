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

    // Database dependencies
    // Uncomment if you need to implement database persistence logic
    //
    // implementation(libs.spring.boot.starter.data.jpa)
    // runtimeOnly(libs.postgresql)
    // implementation(libs.flyway.core)
    // implementation(libs.flyway.database.postgresql)

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
 * Tailwind CSS Standalone CLI configuration
 * Compiles Tailwind CSS without Node.js / npm dependencies
 */
val tailwindVersion = "v4.0.9"

val osName = System.getProperty("os.name").lowercase()
val osArch = System.getProperty("os.arch").lowercase()

val tailwindOs =
    when {
        osName.contains("linux") -> "linux"
        osName.contains("mac") || osName.contains("darwin") -> "macos"
        osName.contains("win") -> "windows"
        else -> "linux"
    }

val tailwindArch =
    when {
        osArch.contains("aarch64") || osArch.contains("arm64") -> "arm64"
        else -> "x64"
    }

val tailwindBinaryName =
    if (tailwindOs ==
        "windows"
    ) {
        "tailwindcss-$tailwindOs-$tailwindArch.exe"
    } else {
        "tailwindcss-$tailwindOs-$tailwindArch"
    }
val tailwindBinaryFile = layout.projectDirectory.file(".bin/$tailwindBinaryName").asFile

val downloadTailwindCli =
    tasks.register("downloadTailwindCli") {
        group = "tailwind"
        description = "Downloads the standalone Tailwind CSS CLI binary for the host platform"
        outputs.file(tailwindBinaryFile)

        doLast {
            if (!tailwindBinaryFile.exists()) {
                tailwindBinaryFile.parentFile.mkdirs()
                val downloadUrl = "https://github.com/tailwindlabs/tailwindcss/releases/download/$tailwindVersion/$tailwindBinaryName"
                println("Downloading Tailwind CLI from $downloadUrl...")
                ant.invokeMethod("get", mapOf("src" to downloadUrl, "dest" to tailwindBinaryFile))
                tailwindBinaryFile.setExecutable(true)
                println("Tailwind CLI downloaded to ${tailwindBinaryFile.absolutePath}")
            }
        }
    }

val buildTailwind =
    tasks.register<Exec>("buildTailwind") {
        dependsOn(downloadTailwindCli)
        group = "tailwind"
        description = "Compiles Tailwind CSS styles into style.css"
        inputs.file("src/main/resources/static/css/input.css")
        inputs.files(fileTree("src/main/resources/templates") { include("**/*.html") })
        outputs.file("src/main/resources/static/css/style.css")

        commandLine(
            tailwindBinaryFile.absolutePath,
            "-i",
            layout.projectDirectory
                .file("src/main/resources/static/css/input.css")
                .asFile.absolutePath,
            "-o",
            layout.projectDirectory
                .file("src/main/resources/static/css/style.css")
                .asFile.absolutePath,
            "--minify",
        )
    }

val tailwindWatch =
    tasks.register<Exec>("tailwindWatch") {
        dependsOn(downloadTailwindCli)
        group = "tailwind"
        description = "Watches and recompiles Tailwind CSS styles continuously"

        commandLine(
            tailwindBinaryFile.absolutePath,
            "-i",
            layout.projectDirectory
                .file("src/main/resources/static/css/input.css")
                .asFile.absolutePath,
            "-o",
            layout.projectDirectory
                .file("src/main/resources/static/css/style.css")
                .asFile.absolutePath,
            "--watch",
        )
    }

tasks.processResources {
    dependsOn(buildTailwind)
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
