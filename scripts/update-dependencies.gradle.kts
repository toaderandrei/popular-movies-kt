import java.util.regex.Pattern


// todo fixme, now it updates without checking conflicts or de3pendencies. it should check what is releasable and what not.
tasks.register("updateDependencies") {
    doLast {
        val tomlFile = project.file("gradle/libs.versions.toml")
        val tomlContent = tomlFile.readText()

        // Define keys that should not be updated
        val excludeKeys = setOf("popular-movies-version", "epoxy", "androidGradlePlugin", "kotlin")

        // Regex to match version entries
        val regex = """(\w+)\s*=\s*"(\d+)\.(\d+)\.(\d+)"""".toRegex()

        // Function to increment version
        fun incrementVersion(version: String): String {
            val parts = version.split(".").map { it.toInt() }
            return "${parts[0]}.${parts[1]}.${parts[2] + 1}"
        }

        // Update versions
        val updatedContent = regex.replace(tomlContent) { matchResult ->
            val key = matchResult.groupValues[1]
            val currentVersion = matchResult.groupValues[2] + "." + matchResult.groupValues[3] + "." + matchResult.groupValues[4]
            if (excludeKeys.contains(key)) {
                matchResult.value  // Do not change excluded keys
            } else {
                val newVersion = incrementVersion(currentVersion)
                "$key = \"$newVersion\""
            }
        }

        // Write updated content back to the file
        tomlFile.writeText(updatedContent)

        println("Dependencies updated, excluding $excludeKeys")
    }
}
