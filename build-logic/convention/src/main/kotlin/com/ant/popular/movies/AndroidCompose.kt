package com.ant.popular.movies

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Configure Compose-specific options for Application modules
 */
internal fun Project.configureAndroidCompose(
    extension: ApplicationExtension,
) {
    extension.apply {
        buildFeatures {
            compose = true
        }
    }

    dependencies {
        val bom = libs.findLibrary("androidx-compose-bom").get()
        add("implementation", platform(bom))
    }
}

/**
 * Configure Compose-specific options for Library modules
 */
internal fun Project.configureAndroidCompose(
    extension: LibraryExtension,
) {
    extension.apply {
        buildFeatures {
            compose = true
        }
    }

    dependencies {
        val bom = libs.findLibrary("androidx-compose-bom").get()
        add("implementation", platform(bom))
    }
}
