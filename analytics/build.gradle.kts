plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kapt)
    alias(libs.plugins.popular.movies.android.library)
}

android {
    // Add this line
    namespace  = "com.ant.analytics"
}

dependencies {
    implementation(project(":data:models"))
    implementation(libs.coreKtx)
    implementation(libs.appcompat)
    implementation(libs.material)

    // Dagger - Hilt
    implementation(libs.hilt.android)
    implementation(project(":common"))
    kapt(libs.hilt.compiler)

    // Firebase
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}