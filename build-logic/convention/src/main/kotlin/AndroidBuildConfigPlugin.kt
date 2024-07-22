import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.util.Properties

class AndroidBuildConfigPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.configure<ApplicationExtension> {
            configureSigningConfigs(target)
            configureDefaultConfig(target)
            configureBuildTypes(target)
        }
    }

    private fun ApplicationExtension.configureSigningConfigs(project: Project) {
        signingConfigs {
            create("release") {
                keyAlias = System.getenv("SIGNING_KEY_ALIAS")
                keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
                storeFile = project.file("${System.getenv("HOME")}/.android/release.keystore")
                println("Configured release keystore path: ${storeFile?.absolutePath}")
                storePassword = System.getenv("SIGNING_STORE_PASSWORD")
            }
        }
    }

    private fun ApplicationExtension.configureDefaultConfig(project: Project) {
        defaultConfig {
            val tmdbApiKey = getApiKey(project)
            buildConfigField("String", "TMDB_API_KEY", "\"$tmdbApiKey\"")
        }
    }

    private fun ApplicationExtension.configureBuildTypes(project: Project) {
        buildTypes {
            getByName("debug") {
                val tmdbApiKey = getApiKey(project)
                buildConfigField("String", "TMDB_API_KEY", "\"$tmdbApiKey\"")
                versionNameSuffix = "-dev"
                applicationIdSuffix = ".debug"
            }

            getByName("release") {
                signingConfig = signingConfigs.getByName("release")
                isDebuggable = false
                isMinifyEnabled = true
                proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")

                val tmdbApiKey = getApiKey(project)
                buildConfigField("String", "TMDB_API_KEY", "\"$tmdbApiKey\"")
            }
        }
    }

    private fun getApiKey(project: Project): String {
        val localPropertiesFile = project.rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            val properties = Properties().apply {
                load(localPropertiesFile.inputStream())
            }
            return properties.getProperty("TMDB_API_KEY", "")
        } else {
            return System.getenv("TMDB_API_KEY")
                ?: throw GradleException("TMDB_API_KEY environment variable is not set")
        }
    }
}
