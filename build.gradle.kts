import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kapt) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.gms) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.safe.args) apply false
    id("com.github.ben-manes.versions") version "0.46.0"
}

tasks.withType<DependencyUpdatesTask> {
    outputFormatter = "json"
    rejectVersionIf {
        val dominated = listOf("alpha", "beta", "rc", "cr", "m", "preview", "dev")
        dominated.any { qualifier ->
            candidate.version.lowercase().contains(qualifier)
        }
    }
}

apply(from = "scripts/update-release-version.gradle.kts")
apply(from = "scripts/update-dependencies.gradle.kts")