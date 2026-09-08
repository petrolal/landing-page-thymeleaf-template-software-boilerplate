///usr/bin/env jbang "$0" "$@" ; exit $?
//KOTLIN 2.1.10
//DEPS info.picocli:picocli:4.7.6

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

    @Option(names = ["-r", "--repo"], description = ["GitHub repository (owner/repo)"], defaultValue = "petrolal/landing-page-thymeleaf-template-first")
    var repo: String = "petrolal/landing-page-thymeleaf-template-first"

    @Option(names = ["-b", "--branch"], description = ["Git branch or tag to download from"], defaultValue = "main")
    var branch: String = "main"

    @Option(names = ["-a", "--archive"], description = ["Path to local tar.gz archive (for CI/offline use)"])
    var archivePath: String? = null

    override fun call(): Int {
        val targetDir = File(projectName)
        if (targetDir.exists()) {
            System.err.println("Error: Directory '$projectName' already exists!")
            return 1
        }

        println("Scaffolding new project into: ${targetDir.absolutePath}")

        val tempTar = File.createTempFile("template-", ".tar.gz")
        val archiveFile =
            if (archivePath != null) {
                val localFile = File(archivePath!!)
                if (!localFile.exists()) {
                    System.err.println("Error: Archive file '$archivePath' not found!")
                    return 1
                }
                localFile
            } else {
                val tarUrl = "https://github.com/$repo/archive/$branch.tar.gz"
                println("Fetching template from GitHub ($tarUrl)...")
                try {
                    val client =
                        java.net.http.HttpClient.newBuilder()
                            .followRedirects(java.net.http.HttpClient.Redirect.NORMAL)
                            .build()
                    val request =
                        java.net.http.HttpRequest.newBuilder()
                            .uri(java.net.URI.create(tarUrl))
                            .build()
                    val response =
                        client.send(
                            request,
                            java.net.http.HttpResponse.BodyHandlers.ofFile(
                                tempTar.toPath(),
                                java.nio.file.StandardOpenOption.CREATE,
                                java.nio.file.StandardOpenOption.WRITE,
                                java.nio.file.StandardOpenOption.TRUNCATE_EXISTING,
                            ),
                        )
                    if (response.statusCode() !in 200..299) {
                        System.err.println("Error: Failed to download template from $tarUrl (HTTP ${response.statusCode()})")
                        tempTar.delete()
                        return 1
                    }
                } catch (e: Exception) {
                    System.err.println("Error fetching template: ${e.message}")
                    tempTar.delete()
                    return 1
                }
                tempTar
            }

        // 2. Extract using system tar
        targetDir.mkdirs()
        val extractProcess =
            ProcessBuilder(
                "tar",
                "-xzf",
                archiveFile.absolutePath,
                "--strip-components=1",
                "-C",
                targetDir.absolutePath,
            ).redirectErrorStream(true)

        val proc = extractProcess.start()
        proc.inputStream.copyTo(System.out)
        val exitCode = proc.waitFor()
        if (archivePath == null) {
            tempTar.delete()
        }

        if (exitCode != 0) {
            System.err.println("Error: Failed to extract template archive (tar exit code: $exitCode)")
            return exitCode
        }

        // 3. Remove template artifacts
        File(targetDir, "init.kt").delete()
        File(targetDir, "jbang-catalog.json").delete()
        File(targetDir, ".git").deleteRecursively()
        File(targetDir, ".github").deleteRecursively()

        // 4. Update settings.gradle.kts
        val settingsFile = File(targetDir, "settings.gradle.kts")
        if (settingsFile.exists()) {
            settingsFile.writeText("rootProject.name = \"$projectName\"\n")
        }

        // 5. Update build.gradle.kts group and project references
        val buildFile = File(targetDir, "build.gradle.kts")
        if (buildFile.exists()) {
            val content = buildFile.readText()
            val updated =
                content
                    .replace(Regex("""group\s*=\s*"[^"]*""""), "group = \"$targetPackage\"")
                    .replace("landing-page-thymeleaf-template-first", projectName)
            buildFile.writeText(updated)
        }

        // 6. Update application.yaml
        val appYaml = File(targetDir, "src/main/resources/application.yaml")
        if (appYaml.exists()) {
            val content = appYaml.readText()
            val updated = content.replace("landing-page-thymeleaf-template-first", projectName)
            appYaml.writeText(updated)
        }

        // 7. Restructure Kotlin package directories
        val defaultPackage = "com.petrolal.templates.landingpagefirst"
        if (defaultPackage != targetPackage) {
            restructureSourceDirectories(targetDir, "src/main/kotlin", defaultPackage, targetPackage)
            restructureSourceDirectories(targetDir, "src/test/kotlin", defaultPackage, targetPackage)
        }

        // 8. Make gradlew executable
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

        // Recursively move contents from old package directory to new package directory
        moveDirectoryContents(oldPath, newPath)

        // Recursively update package declarations and imports in all Kotlin files under newPath
        newPath.walkTopDown().filter { it.isFile && it.extension == "kt" }.forEach { file ->
            val updated =
                file.readText()
                    .replace("package $oldPkg", "package $newPkg")
                    .replace("import $oldPkg", "import $newPkg")
            file.writeText(updated)
        }

        // Clean up empty parent directories from old package path
        cleanEmptyParents(oldPath, root)
    }

    private fun moveDirectoryContents(sourceDir: File, targetDir: File) {
        targetDir.mkdirs()
        sourceDir.listFiles()?.forEach { file ->
            val dest = File(targetDir, file.name)
            if (file.isDirectory) {
                moveDirectoryContents(file, dest)
                file.delete()
            } else {
                Files.move(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING)
            }
        }
    }

    private fun cleanEmptyParents(dir: File, stopAt: File) {
        var current: File? = dir
        while (current != null && current != stopAt && (current.listFiles()?.isEmpty() == true)) {
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
