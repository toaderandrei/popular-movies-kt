import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
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
        val alias = System.getenv("SIGNING_KEY_ALIAS")
        val keyPass = System.getenv("SIGNING_KEY_PASSWORD")
        val storePass = System.getenv("SIGNING_STORE_PASSWORD")
        val homePath = System.getenv("HOME") ?: ""
        val keystorePath = "$homePath/.android/release.keystore"
        val keystoreFile = project.file(keystorePath)

        val signingReady = alias != null && keyPass != null && storePass != null && keystoreFile.exists()

        if (signingReady) {
            project.logger.lifecycle("Release signing configured with keystore: $keystorePath")
            signingConfigs {
                create("release") {
                    keyAlias = alias
                    keyPassword = keyPass
                    storeFile = keystoreFile
                    storePassword = storePass
                }
            }
        } else {
            project.logger.warn("Release signing NOT configured — APK will be unsigned")
            if (alias == null) project.logger.warn("  Missing SIGNING_KEY_ALIAS")
            if (keyPass == null) project.logger.warn("  Missing SIGNING_KEY_PASSWORD")
            if (storePass == null) project.logger.warn("  Missing SIGNING_STORE_PASSWORD")
            if (!keystoreFile.exists()) project.logger.warn("  Keystore not found at $keystorePath")
        }

        project.extra.set("signingReady", signingReady)
    }

    private fun ApplicationExtension.configureDefaultConfig(project: Project) {
        defaultConfig {
            val tmdbApiKey = getApiKey(project)
            buildConfigField("String", "TMDB_API_KEY", "\"$tmdbApiKey\"")
        }
    }

    private fun ApplicationExtension.configureBuildTypes(project: Project) {
        val signingReady = project.extra.get("signingReady") as Boolean

        buildTypes {
            getByName("debug") {
                val tmdbApiKey = getApiKey(project)
                buildConfigField("String", "TMDB_API_KEY", "\"$tmdbApiKey\"")
                versionNameSuffix = "-dev"
                applicationIdSuffix = ".debug"
            }

            getByName("release") {
                if (signingReady) {
                    signingConfig = signingConfigs.getByName("release")
                }
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
