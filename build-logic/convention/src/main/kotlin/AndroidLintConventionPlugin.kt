import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.Lint
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.io.File


class AndroidLintConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            when {
                pluginManager.hasPlugin("com.android.application") ->
                    configure<ApplicationExtension> { lint.configure(target) }

                pluginManager.hasPlugin("com.android.library") ->
                    configure<LibraryExtension> { lint.configure(target) }

                else -> {
                    pluginManager.apply("com.android.lint")
                    configure<Lint> { configure(target) }
                }
            }
        }
    }
}

private fun Lint.configure(project: Project) {
    xmlReport = true
    checkDependencies = true
    lintConfig = File("${project.rootDir}/lint.xml")
}
