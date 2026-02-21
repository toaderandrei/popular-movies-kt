import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import com.android.build.api.dsl.LibraryExtension

import com.ant.popular.movies.configureKotlinAndroid
import com.ant.popular.movies.libs

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
            }

            dependencies {
                add("testImplementation", libs.findLibrary("junit").get())
                add("testImplementation", libs.findLibrary("mockK").get())
                add("testImplementation", libs.findLibrary("kotlinx.coroutines.test").get())
            }
        }
    }
}
