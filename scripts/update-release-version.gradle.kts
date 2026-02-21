import java.util.regex.Pattern

tasks.register("updateAppVersion") {
    doLast {
        val tomlFile = project.file("gradle/libs.versions.toml")  // Ensure 'project.file' is used
        val tomlContent = tomlFile.readText()

        // Define the key of the version you want to update
        val versionKey = "popular-movies-version"
        val regex = """(${Pattern.quote(versionKey)}\s*=\s*")(\d+)\.(\d+)\.(\d+)"""".toRegex()

        val matchResult = regex.find(tomlContent)
        if (matchResult != null) {
            val (key, major, minor, patch) = matchResult.destructured
            val newPatch = patch.toInt() + 1 // Increment patch version

            val newVersion = "$major.$minor.$newPatch"

            // Replace the old version with the new version
            val updatedContent = tomlContent.replaceFirst(regex, "$key$newVersion\"")

            // Write back to the file
            tomlFile.writeText(updatedContent)

            println("Updated $versionKey to version $newVersion")
        } else {
            println("Version key '$versionKey' not found in libs.versions.toml")
        }
    }
}
