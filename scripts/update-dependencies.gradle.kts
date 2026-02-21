import groovy.json.JsonSlurper

/** Version keys that should never be auto-updated. */
val excludeKeys = setOf("popular-movies-version", "epoxy", "androidGradlePlugin", "kotlin")

/**
 * Parses the ben-manes JSON report and returns a map of maven coordinates (group:name)
 * to the latest stable version available.
 */
fun parseAvailableUpdates(reportFile: java.io.File): Map<String, String> {
    if (!reportFile.exists()) return emptyMap()

    @Suppress("UNCHECKED_CAST")
    val json = JsonSlurper().parseText(reportFile.readText()) as Map<String, Any>
    @Suppress("UNCHECKED_CAST")
    val deps = (json["outdated"] as? Map<String, Any>)
        ?.get("dependencies") as? List<Map<String, Any>> ?: return emptyMap()

    val updates = mutableMapOf<String, String>()
    for (dep in deps) {
        val group = dep["group"] as? String ?: continue
        val name = dep["name"] as? String ?: continue
        @Suppress("UNCHECKED_CAST")
        val available = dep["available"] as? Map<String, Any> ?: continue
        // Prefer stable milestone, then fall back through minor/patch/major
        val version = (available["milestone"] ?: available["minor"]
            ?: available["patch"] ?: available["major"]) as? String ?: continue
        updates["$group:$name"] = version
    }
    return updates
}

/**
 * Parses the [libraries] section of a TOML version catalog and builds a mapping
 * from version.ref keys to their maven coordinates (group:name).
 */
fun parseVersionRefMap(tomlLines: List<String>): Map<String, Set<String>> {
    val refRegex = """^\s*[\w\-]+\s*=\s*\{.*version\.ref\s*=\s*"([\w\-]+)".*\}""".toRegex()
    val coordRegex = """(?:module\s*=\s*"([^"]+:[^"]+)"|group\s*=\s*"([^"]+)".*name\s*=\s*"([^"]+)")""".toRegex()

    val result = mutableMapOf<String, MutableSet<String>>()
    for (line in tomlLines) {
        val versionRef = refRegex.find(line)?.groupValues?.get(1) ?: continue
        val coordMatch = coordRegex.find(line) ?: continue
        val coordinate = if (coordMatch.groupValues[1].isNotEmpty()) {
            coordMatch.groupValues[1]
        } else {
            "${coordMatch.groupValues[2]}:${coordMatch.groupValues[3]}"
        }
        result.getOrPut(versionRef) { mutableSetOf() }.add(coordinate)
    }
    return result
}

/**
 * Resolves which TOML version keys should be updated by cross-referencing
 * the version.ref -> coordinates map with available maven updates.
 */
fun resolveVersionUpdates(
    versionRefMap: Map<String, Set<String>>,
    availableUpdates: Map<String, String>
): Map<String, String> {
    val updates = mutableMapOf<String, String>()
    for ((versionKey, coordinates) in versionRefMap) {
        if (versionKey in excludeKeys) continue
        for (coord in coordinates) {
            val newVersion = availableUpdates[coord]
            if (newVersion != null) {
                updates[versionKey] = newVersion
                break
            }
        }
    }
    return updates
}

/**
 * Applies version updates to the [versions] section of the TOML file lines.
 * Returns the updated lines.
 */
fun applyUpdates(tomlLines: List<String>, versionUpdates: Map<String, String>): List<String> {
    val versionLineRegex = """^(\s*)([\w\-]+)(\s*=\s*")([^"]+)(".*)$""".toRegex()
    return tomlLines.map { line ->
        val match = versionLineRegex.matchEntire(line) ?: return@map line
        val key = match.groupValues[2]
        val newVersion = versionUpdates[key] ?: return@map line
        val oldVersion = match.groupValues[4]
        println("  $key: $oldVersion -> $newVersion")
        "${match.groupValues[1]}${key}${match.groupValues[3]}${newVersion}${match.groupValues[5]}"
    }
}

/**
 * Generates a markdown changelog summarizing all version changes.
 */
fun generateChangelog(tomlLines: List<String>, versionUpdates: Map<String, String>): String {
    return buildString {
        appendLine("## Dependency Updates")
        appendLine()
        appendLine("| Dependency | Old | New |")
        appendLine("|------------|-----|-----|")
        for ((key, newVersion) in versionUpdates.toSortedMap()) {
            val oldVersion = tomlLines.firstNotNullOfOrNull { line ->
                """^\s*${Regex.escape(key)}\s*=\s*"([^"]+)"""".toRegex().find(line)?.groupValues?.get(1)
            } ?: "?"
            appendLine("| `$key` | $oldVersion | $newVersion |")
        }
        appendLine()
        appendLine("**${versionUpdates.size} version(s) updated.**")
    }
}

// -- Task registration --

tasks.register("updateDependencies") {
    dependsOn("dependencyUpdates")

    val reportPath = project.file("build/dependencyUpdates/report.json").absolutePath
    val tomlPath = project.file("gradle/libs.versions.toml").absolutePath
    val changelogPath = project.file("build/dependencyUpdates/changelog.md").absolutePath

    doLast {
        val changelog = java.io.File(changelogPath).also { it.parentFile.mkdirs() }
        val availableUpdates = parseAvailableUpdates(java.io.File(reportPath))

        if (availableUpdates.isEmpty()) {
            println("All dependencies are up to date")
            changelog.writeText("No dependency updates available.")
            return@doLast
        }

        val toml = java.io.File(tomlPath)
        val tomlLines = toml.readText().lines()
        val versionRefMap = parseVersionRefMap(tomlLines)
        val versionUpdates = resolveVersionUpdates(versionRefMap, availableUpdates)

        if (versionUpdates.isEmpty()) {
            println("No version updates to apply (after exclusions)")
            changelog.writeText("No dependency updates available (all outdated dependencies are in the exclude list).")
            return@doLast
        }

        val updatedLines = applyUpdates(tomlLines, versionUpdates)
        toml.writeText(updatedLines.joinToString("\n"))
        changelog.writeText(generateChangelog(tomlLines, versionUpdates))

        println("Dependencies updated successfully (${versionUpdates.size} versions changed)")
    }
}
