plugins {
    alias(libs.plugins.popular.movies.android.library)
    alias(libs.plugins.popular.movies.hilt)
}

android {
    namespace = "com.ant.network"
}

dependencies {
    api(project(":core:common"))
    implementation(project(":core:models"))
    implementation(project(":core:tmdbApi"))
    implementation(project(":core:database"))

    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}