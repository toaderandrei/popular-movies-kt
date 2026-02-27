---
name: kmp-gradle-logic
description: Expert guidance on setting up scalable Gradle build logic for Kotlin Multiplatform projects using Convention Plugins and Version Catalogs. Covers KMP targets, Compose Multiplatform, Ktor, and Koin.
---

# KMP Gradle Build Logic & Convention Plugins

This skill helps you configure a scalable, maintainable build system for **Kotlin Multiplatform** projects using **Gradle Convention Plugins** and **Version Catalogs**.

## Goal

Centralize KMP-specific build logic (target configuration, Compose Multiplatform setup, Ktor, Koin, serialization, etc.) in reusable plugins — instead of duplicating `kotlin { ... }` blocks across every module.

## Project Structure

```text
root/
├── build-logic/
│   ├── convention/
│   │   ├── src/main/kotlin/
│   │   │   ├── KmpLibraryConventionPlugin.kt
│   │   │   ├── KmpApplicationConventionPlugin.kt
│   │   │   ├── KmpComposeConventionPlugin.kt
│   │   │   ├── KmpKtorConventionPlugin.kt
│   │   │   ├── KmpSerializationConventionPlugin.kt
│   │   │   └── KmpKoinConventionPlugin.kt
│   │   └── build.gradle.kts
│   └── settings.gradle.kts
├── gradle/
│   └── libs.versions.toml
├── composeApp/          # Android + iOS + Desktop entry point
│   └── build.gradle.kts
├── shared/              # Shared KMP logic
│   └── build.gradle.kts
├── server/              # Ktor backend module (optional)
│   └── build.gradle.kts
└── settings.gradle.kts
```

---

## Step 1: Root `settings.gradle.kts`

```kotlin
pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MyKmpProject"
include(":composeApp", ":shared", ":server")
```

### `build-logic/settings.gradle.kts`

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}
rootProject.name = "build-logic"
```

---

## Step 2: `libs.versions.toml`

```toml
[versions]
agp = "8.5.2"
kotlin = "2.1.0"
compose-multiplatform = "1.7.0"
ktor = "3.1.0"
koin = "4.0.0"
koin-compose = "4.0.0"
kotlinx-coroutines = "1.9.0"
kotlinx-serialization = "1.7.3"
kotlinx-datetime = "0.6.1"
android-minSdk = "26"
android-targetSdk = "35"
android-compileSdk = "35"

[libraries]
# Ktor client
ktor-client-core            = { module = "io.ktor:ktor-client-core", version.ref = "ktor" }
ktor-client-android         = { module = "io.ktor:ktor-client-android", version.ref = "ktor" }
ktor-client-darwin          = { module = "io.ktor:ktor-client-darwin", version.ref = "ktor" }
ktor-client-java            = { module = "io.ktor:ktor-client-java", version.ref = "ktor" }
ktor-client-content-negotiation = { module = "io.ktor:ktor-client-content-negotiation", version.ref = "ktor" }
ktor-client-logging         = { module = "io.ktor:ktor-client-logging", version.ref = "ktor" }
ktor-client-auth            = { module = "io.ktor:ktor-client-auth", version.ref = "ktor" }
# Ktor server (if using Ktor backend module)
ktor-server-core            = { module = "io.ktor:ktor-server-core", version.ref = "ktor" }
ktor-server-netty           = { module = "io.ktor:ktor-server-netty", version.ref = "ktor" }
ktor-server-content-negotiation = { module = "io.ktor:ktor-server-content-negotiation", version.ref = "ktor" }
ktor-serialization-kotlinx-json = { module = "io.ktor:ktor-serialization-kotlinx-json", version.ref = "ktor" }
# Koin
koin-core                   = { module = "io.insert-koin:koin-core", version.ref = "koin" }
koin-android                = { module = "io.insert-koin:koin-android", version.ref = "koin" }
koin-compose                = { module = "io.insert-koin:koin-compose", version.ref = "koin-compose" }
koin-compose-viewmodel      = { module = "io.insert-koin:koin-compose-viewmodel", version.ref = "koin-compose" }
# KotlinX
kotlinx-coroutines-core     = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-core", version.ref = "kotlinx-coroutines" }
kotlinx-coroutines-android  = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-android", version.ref = "kotlinx-coroutines" }
kotlinx-serialization-json  = { module = "org.jetbrains.kotlinx:kotlinx-serialization-json", version.ref = "kotlinx-serialization" }
kotlinx-datetime            = { module = "org.jetbrains.kotlinx:kotlinx-datetime", version.ref = "kotlinx-datetime" }

[plugins]
android-application         = { id = "com.android.application", version.ref = "agp" }
android-library             = { id = "com.android.library", version.ref = "agp" }
kotlin-multiplatform        = { id = "org.jetbrains.kotlin.multiplatform", version.ref = "kotlin" }
kotlin-android              = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-jvm                  = { id = "org.jetbrains.kotlin.jvm", version.ref = "kotlin" }
kotlin-serialization        = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
compose-multiplatform       = { id = "org.jetbrains.compose", version.ref = "compose-multiplatform" }
compose-compiler            = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
# Convention plugins — registered in build-logic
kmp-library                 = { id = "myapp.kmp.library", version = "unspecified" }
kmp-application             = { id = "myapp.kmp.application", version = "unspecified" }
kmp-compose                 = { id = "myapp.kmp.compose", version = "unspecified" }
kmp-ktor                    = { id = "myapp.kmp.ktor", version = "unspecified" }
kmp-serialization           = { id = "myapp.kmp.serialization", version = "unspecified" }
kmp-koin                    = { id = "myapp.kmp.koin", version = "unspecified" }
```

---

## Step 3: `build-logic/convention/build.gradle.kts`

```kotlin
plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.plugins.android.application.toDependency())
    compileOnly(libs.plugins.android.library.toDependency())
    compileOnly(libs.plugins.kotlin.multiplatform.toDependency())
    compileOnly(libs.plugins.compose.multiplatform.toDependency())
    compileOnly(libs.plugins.compose.compiler.toDependency())
    compileOnly(libs.plugins.kotlin.serialization.toDependency())
}

// Helper to convert plugin alias to compileOnly dependency
fun Provider<PluginDependency>.toDependency() =
    map { "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}" }

gradlePlugin {
    plugins {
        register("kmpLibrary") {
            id = "myapp.kmp.library"
            implementationClass = "KmpLibraryConventionPlugin"
        }
        register("kmpApplication") {
            id = "myapp.kmp.application"
            implementationClass = "KmpApplicationConventionPlugin"
        }
        register("kmpCompose") {
            id = "myapp.kmp.compose"
            implementationClass = "KmpComposeConventionPlugin"
        }
        register("kmpKtor") {
            id = "myapp.kmp.ktor"
            implementationClass = "KmpKtorConventionPlugin"
        }
        register("kmpSerialization") {
            id = "myapp.kmp.serialization"
            implementationClass = "KmpSerializationConventionPlugin"
        }
        register("kmpKoin") {
            id = "myapp.kmp.koin"
            implementationClass = "KmpKoinConventionPlugin"
        }
    }
}
```

---

## Step 4: Convention Plugins

### `KmpLibraryConventionPlugin.kt`

The **base plugin** — applied by all shared KMP library modules. Configures targets and common source sets.

```kotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.library")
            }

            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget {
                    compilations.all {
                        kotlinOptions {
                            jvmTarget = "17"
                        }
                    }
                }
                iosX64()
                iosArm64()
                iosSimulatorArm64()
                jvm("desktop")

                // Opt-ins shared across all source sets
                sourceSets.all {
                    languageSettings.optIn("kotlin.RequiresOptIn")
                    languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                }
            }

            // Configure android {} block (required even for library targets)
            extensions.configure<com.android.build.gradle.LibraryExtension> {
                compileSdk = 35
                defaultConfig {
                    minSdk = 26
                }
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
            }
        }
    }
}
```

---

### `KmpApplicationConventionPlugin.kt`

For the `:composeApp` module (has an Android `applicationId`).

```kotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.application")
            }

            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget {
                    compilations.all {
                        kotlinOptions { jvmTarget = "17" }
                    }
                }
                iosX64()
                iosArm64()
                iosSimulatorArm64()
                jvm("desktop")
            }

            extensions.configure<com.android.build.gradle.AppExtension> {
                compileSdkVersion(35)
                defaultConfig {
                    minSdk = 26
                    targetSdk = 35
                }
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
            }
        }
    }
}
```

---

### `KmpComposeConventionPlugin.kt`

Applies **Compose Multiplatform** and the Compose compiler plugin. Assumes `KmpLibrary` or `KmpApplication` is already applied.

```kotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            val compose = extensions.getByType<ComposeExtension>()

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.apply {
                    commonMain.dependencies {
                        implementation(compose.dependencies.runtime)
                        implementation(compose.dependencies.foundation)
                        implementation(compose.dependencies.material3)
                        implementation(compose.dependencies.ui)
                        implementation(compose.dependencies.components.resources)
                        implementation(compose.dependencies.components.uiToolingPreview)
                    }
                    androidMain.dependencies {
                        implementation(compose.dependencies.preview)
                        implementation("androidx.activity:activity-compose:1.9.0")
                    }
                    // Desktop
                    val desktopMain = getByName("desktopMain")
                    desktopMain.dependencies {
                        implementation(compose.dependencies.desktop.currentOs)
                    }
                }
            }
        }
    }
}
```

---

### `KmpKtorConventionPlugin.kt`

Adds Ktor client with platform-specific engines and shared config (content negotiation, logging, auth).

```kotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpKtorConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Serialization is a peer dependency of Ktor content negotiation
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.apply {
                    commonMain.dependencies {
                        implementation("io.ktor:ktor-client-core:3.1.0")
                        implementation("io.ktor:ktor-client-content-negotiation:3.1.0")
                        implementation("io.ktor:ktor-client-logging:3.1.0")
                        implementation("io.ktor:ktor-client-auth:3.1.0")
                        implementation("io.ktor:ktor-serialization-kotlinx-json:3.1.0")
                    }
                    androidMain.dependencies {
                        implementation("io.ktor:ktor-client-android:3.1.0")
                    }
                    iosMain.dependencies {
                        implementation("io.ktor:ktor-client-darwin:3.1.0")
                    }
                    val desktopMain = getByName("desktopMain")
                    desktopMain.dependencies {
                        implementation("io.ktor:ktor-client-java:3.1.0")
                    }
                }
            }
        }
    }
}
```

> **Tip:** Prefer reading versions from the catalog via `project.extensions.getByType<VersionCatalogsExtension>()` rather than hardcoding strings, to keep everything in sync with `libs.versions.toml`.

---

### `KmpSerializationConventionPlugin.kt`

```kotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpSerializationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.commonMain.dependencies {
                    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
                    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
                }
            }
        }
    }
}
```

---

### `KmpKoinConventionPlugin.kt`

```kotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpKoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.apply {
                    commonMain.dependencies {
                        implementation("io.insert-koin:koin-core:4.0.0")
                        implementation("io.insert-koin:koin-compose:4.0.0")
                        implementation("io.insert-koin:koin-compose-viewmodel:4.0.0")
                    }
                    androidMain.dependencies {
                        implementation("io.insert-koin:koin-android:4.0.0")
                    }
                }
            }
        }
    }
}
```

---

## Step 5: Usage in Modules

### `shared/build.gradle.kts` — network + DI library module

```kotlin
plugins {
    alias(libs.plugins.kmp.library)        // KMP targets + Android library
    alias(libs.plugins.kmp.serialization)  // kotlinx-serialization
    alias(libs.plugins.kmp.ktor)           // Ktor client per platform
    alias(libs.plugins.kmp.koin)           // Koin DI
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // module-specific extras only
        }
    }
}
```

### `composeApp/build.gradle.kts` — UI module

```kotlin
plugins {
    alias(libs.plugins.kmp.application)   // KMP targets + Android app
    alias(libs.plugins.kmp.compose)       // Compose Multiplatform
    alias(libs.plugins.kmp.koin)          // Koin DI
}

android {
    namespace = "com.myapp"
    defaultConfig {
        applicationId = "com.myapp"
        versionCode = 1
        versionName = "1.0"
    }
}
```

### `server/build.gradle.kts` — Ktor server (JVM only)

```kotlin
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    // Koin for Ktor
    implementation("io.insert-koin:koin-ktor:4.0.0")
}
```

> The server module uses a plain `kotlin-jvm` plugin — no need for a convention plugin unless you have multiple server modules with shared config.

---

## Plugin Composition Cheatsheet

| Module type                         | Plugins to apply                              |
|-------------------------------------|-----------------------------------------------|
| Pure shared logic (no UI, no network) | `kmp.library`                               |
| Shared with network calls           | `kmp.library` + `kmp.ktor` + `kmp.serialization` |
| Shared with DI                      | `kmp.library` + `kmp.koin`                   |
| UI feature module                   | `kmp.library` + `kmp.compose` + `kmp.koin`   |
| App entry point                     | `kmp.application` + `kmp.compose` + `kmp.koin` |
| Ktor backend                        | `kotlin-jvm` + `kotlin-serialization` (plain) |

---

## Key Differences from Android-only Convention Plugins

- **No `android { }` block monopoly** — KMP modules configure targets via `kotlin { androidTarget() }` and a minimal `android { }` / `libraryExtension` block for SDK versions only.
- **Platform-specific source sets** — dependencies like Ktor engines belong in `androidMain`, `iosMain`, `desktopMain`, not in `commonMain`.
- **No Hilt** — Koin is the DI framework of choice in KMP (Hilt is Android JVM-only). `koin-compose-viewmodel` replaces Hilt's ViewModel injection.
- **Compose plugin split** — KMP requires *two* Compose plugins: `org.jetbrains.compose` (Compose Multiplatform runtime) and `org.jetbrains.kotlin.plugin.compose` (compiler plugin, replaces the old `kotlinCompilerExtensionVersion`).
- **`jvm("desktop")` naming** — using a named JVM target (`"desktop"`) instead of plain `jvm()` avoids classpath conflicts when both `jvm` and `android` targets are present.
