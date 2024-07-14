import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.safe.args)
    alias(libs.plugins.popular.movies.android.application)
    alias(libs.plugins.popular.movies.android.firebase)
    alias(libs.plugins.gms)
}

android {

    defaultConfig {
        buildConfigField(
            "String",
            "TMDB_API_KEY",
            "\"" + getLocalProperty("API_KEY", "") + "\""
        )

        javaCompileOptions {
            annotationProcessorOptions {
                argument("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    // Add this line
    namespace = "com.ant.app"

    buildTypes {
        // todo fix proguard
        debug {
//            signingConfig = signingConfigs.getByName("debug")
//            versionNameSuffix = "-dev"
//            applicationIdSuffix = ".debug"
        }

        release {
//            signingConfig = signingConfigs.getByName("release")
//            isDebuggable = false
//            isZipAlignEnabled = true
//            isMinifyEnabled = true
//            proguardFile(getDefaultProguardFile("proguard-android.txt"))
//            // global proguard settings
//            proguardFile(file("proguard-rules.pro"))
//            // library proguard settings
//            val files = rootProject.file("proguard")
//                .listFiles()
//                .filter { it.name.startsWith("proguard") }
//                .toTypedArray()
//            proguardFiles(*files)
        }
    }
    lint {
        // Eliminates UnusedResources false positives for resources used in DataBinding layouts
        checkGeneratedSources = true
        // Running lint over the debug variant is enough
        checkReleaseBuilds = false
        abortOnError = false
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
        //noinspection DataBindingWithoutKapt
        dataBinding = true
    }
}

dependencies {
    // module dependency
    implementation(project(":common"))
    implementation(project(":data:models"))
    implementation(project(":common-ui:adapters"))
    implementation(project(":common-ui:resources"))
    implementation(project(":common-ui:bindings"))
    implementation(project(":common-ui:layouts"))
    implementation(project(":common-ui:epoxy"))
    implementation(project(":data:source"))
    implementation(project(":domain"))
    implementation(project(":analytics"))
    implementation(libs.circle.image)

    // UI libs
    implementation(libs.coreKtx)
    implementation(libs.appcompat)
    implementation(libs.recyclerview)
    implementation(libs.constraintLayout)
    implementation(libs.swipeRefresh)
    implementation(libs.cardview)
    implementation(libs.material)

    // dagger/hilt
    implementation(libs.dagger)
    implementation(libs.daggerAndroidSupport)
    compileOnly(libs.annotationDagger2)

    // Dagger - Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Live data
    implementation(libs.livedata)

    //okhttp client
    implementation(libs.okhttp)
    implementation(libs.okhttpLoggingInterceptor)

    //navigation
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui)
    implementation(libs.fragmentKtx)
    implementation(libs.fragment)

    // image loading
    implementation(libs.coil)

    // firebase
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    // check if still needed after plugin
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.data.store)
    implementation(libs.data.store.preferences)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

fun getLocalProperty(propertyName: String, defaultValue: String): String {
    val properties = Properties()
    val localPropertiesFile = project.rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        properties.load(localPropertiesFile.inputStream())
    }
    return properties.getProperty(propertyName, defaultValue)
}