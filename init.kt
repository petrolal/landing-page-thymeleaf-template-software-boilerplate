// /usr/bin/env jbang "$0" "$@" ; exit $?
// KOTLIN 2.1.10
// DEPS info.picocli:picocli:4.7.6

import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.Parameters
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.concurrent.Callable
import kotlin.system.exitProcess

@Command(
    name = "init",
    mixinStandardHelpOptions = true,
    version = ["1.0.0"],
    description = ["One-shot generator for Landing Page Thymeleaf Template"],
)
class Init : Callable<Int> {
    @Parameters(index = "0", description = ["Target project directory name"], defaultValue = "my-landing-page")
    var projectName: String = "my-landing-page"

    @Option(names = ["-p", "--package"], description = ["Target base package"], defaultValue = "com.petrolal.landingpage")
    var targetPackage: String = "com.petrolal.landingpage"

    override fun call(): Int {
        val targetDir = File(projectName)
        if (targetDir.exists()) {
            System.err.println("Error: Directory '$projectName' already exists!")
            return 1
        }

        println("Scaffolding new project into: ${targetDir.absolutePath}")

        // 1. Download/extract archive from GitHub
        val repoOwner = "petrolal"
        val repoName = "landing-page-thymeleaf-template"
        val branch = "master"
        val tarUrl = "https://github.com/$repoOwner/$repoName/archive/refs/heads/$branch.tar.gz"

        val tempTar = File.createTempFile("template-", ".tar.gz")
        println("Fetching template from GitHub ($tarUrl)...")

        java.net.URI(tarUrl).toURL().openStream().use { input ->
            tempTar.outputStream().use { output -> input.copyTo(output) }
        }

        // 2. Extract using system tar
        val extractProcess =
            ProcessBuilder(
                "tar",
                "-xzf",
                tempTar.absolutePath,
                "--strip-components=1",
                "-C",
                ".",
            ).redirectErrorStream(true)

        targetDir.mkdirs()
        extractProcess.directory(targetDir)
        val proc = extractProcess.start()
        proc.inputStream.copyTo(System.out)
        proc.waitFor()
        tempTar.delete()

        // 3. Remove template artifacts
        File(targetDir, "init.kt").delete()
        File(targetDir, ".git").deleteRecursively()

        // 4. Update settings.gradle.kts
        val settingsFile = File(targetDir, "settings.gradle.kts")
        if (settingsFile.exists()) {
            settingsFile.writeText("rootProject.name = \"$projectName\"\n")
        }

        // 5. Update build.gradle.kts group
        val buildFile = File(targetDir, "build.gradle.kts")
        if (buildFile.exists()) {
            val content = buildFile.readText()
            val updated = content.replace(Regex("""group\s*=\s*"[^"]*""""), "group = \"$targetPackage\"")
            buildFile.writeText(updated)
        }

        // 6. Restructure Kotlin package directories
        val defaultPackage = "com.petrolal.templates.landingpage"
        if (defaultPackage != targetPackage) {
            restructureSourceDirectories(targetDir, "src/main/kotlin", defaultPackage, targetPackage)
            restructureSourceDirectories(targetDir, "src/test/kotlin", defaultPackage, targetPackage)
        }

        // 7. Make gradlew executable
        File(targetDir, "gradlew").setExecutable(true)

        println("\nSuccessfully initialized '$projectName'!")
        println("Run commands:")
        println("  cd $projectName")
        println("  git init")
        println("  ./gradlew bootRun\n")

        return 0
    }

    private fun restructureSourceDirectories(
        baseDir: File,
        sourceRoot: String,
        oldPkg: String,
        newPkg: String,
    ) {
        val root = File(baseDir, sourceRoot)
        val oldPath = File(root, oldPkg.replace('.', '/'))
        val newPath = File(root, newPkg.replace('.', '/'))

        if (!oldPath.exists()) return

        newPath.mkdirs()
        oldPath.listFiles()?.forEach { file ->
            val dest = File(newPath, file.name)
            Files.move(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING)

            // Update package declaration and imports inside files
            if (dest.isFile && dest.extension == "kt") {
                val updatedContent =
                    dest
                        .readText()
                        .replace("package $oldPkg", "package $newPkg")
                        .replace("import $oldPkg.", "import $newPkg.")
                dest.writeText(updatedContent)
            }
        }

        // Clean up empty parent directories
        var current: File? = oldPath
        while (current != null && current != root && (current.listFiles()?.isEmpty() == true)) {
            val parent = current.parentFile
            current.delete()
            current = parent
        }
    }
}

fun main(args: Array<String>) {
    val exitCode = CommandLine(Init()).execute(*args)
    exitProcess(exitCode)
}
