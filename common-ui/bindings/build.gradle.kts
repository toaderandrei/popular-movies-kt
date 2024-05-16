plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kapt)
    alias(libs.plugins.popular.movies.android.library)
}
android {
    buildFeatures {
        dataBinding = true
    }
    namespace  = "com.ant.bindings"
}

dependencies {
    implementation(project(":data:source"))

    // UI libs
    implementation(libs.coreKtx)
    implementation(libs.coil)
    implementation(libs.material)
}