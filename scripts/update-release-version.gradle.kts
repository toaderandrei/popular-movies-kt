tasks.register("updateAppVersion") {
    doLast {
        val tomlFile = project.file("gradle/libs.versions.toml")
        val tomlContent = tomlFile.readText()

        val versionKey = "popular-movies-version"
        val versionLineRegex = """^(\s*${Regex.escape(versionKey)}\s*=\s*")(\d+)\.(\d+)\.(\d+)(".*)$""".toRegex(RegexOption.MULTILINE)

        val match = versionLineRegex.find(tomlContent)
        if (match != null) {
            val prefix = match.groupValues[1]
            val major = match.groupValues[2]
            val minor = match.groupValues[3]
            val patch = match.groupValues[4].toInt() + 1
            val suffix = match.groupValues[5]

            val newVersion = "$major.$minor.$patch"
            val updatedContent = tomlContent.replaceFirst(versionLineRegex, "$prefix$newVersion$suffix")

            tomlFile.writeText(updatedContent)
            println("Updated $versionKey to version $newVersion")
        } else {
            println("Version key '$versionKey' not found in libs.versions.toml")
        }
    }
}
