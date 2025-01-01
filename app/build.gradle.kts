plugins {
    alias(libs.plugins.popular.movies.android.application)
    alias(libs.plugins.popular.movies.android.application.compose)
    alias(libs.plugins.popular.movies.android.firebase)
    alias(libs.plugins.gms)
    alias(libs.plugins.popular.movies.android.lint)
    alias(libs.plugins.popular.movies.android.config)
    alias(libs.plugins.popular.movies.hilt)
}

android {

    defaultConfig {
        versionCode = 2
        versionName = "0.1.0" // X.Y.Z; X = Major, Y = minor, Z = Patch level

        javaCompileOptions {
            annotationProcessorOptions {
                argument("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
        //noinspection DataBindingWithoutKapt
        dataBinding = true
    }

    // Add this line
    namespace = "com.ant.app"
}

dependencies {
    // module dependency
    implementation(project(":common"))
    implementation(project(":data:models"))
    implementation(project(":common-ui:adapters"))
    implementation(project(":core:ui"))
    implementation(project(":core:resources"))
    implementation(project(":common-ui:bindings"))
    implementation(project(":common-ui:layouts"))
    implementation(project(":common-ui:epoxy"))
    implementation(project(":data:source"))
    implementation(project(":domain"))
    implementation(project(":analytics"))
    implementation(project(":features:movies"))
    implementation(libs.circle.image)

    // UI libs
    implementation(libs.coreKtx)
    implementation(libs.appcompat)
    implementation(libs.recyclerview)
    implementation(libs.constraintLayout)
    implementation(libs.swipeRefresh)
    implementation(libs.cardview)
    implementation(libs.material)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.layout)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.navigation.compose)

    implementation(libs.androidx.window)
    implementation(libs.androidx.window.core)

    implementation(libs.accompanist.adaptive)

    implementation(libs.coil.kt.compose)

    // dagger/hilt
    implementation(libs.dagger)
    implementation(libs.daggerAndroidSupport)
    compileOnly(libs.annotationDagger2)

    // Dagger - Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Live data
    implementation(libs.livedata)

    // okhttp client
    implementation(libs.okhttp)
    implementation(libs.okhttpLoggingInterceptor)

    // navigation
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
