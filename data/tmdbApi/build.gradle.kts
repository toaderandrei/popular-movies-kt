plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kapt)
    alias(libs.plugins.popular.movies.android.library)
}

android {
    namespace  = "com.ant.tmdb.api"
}

dependencies {

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // tmdb Api
    api(libs.tmdb)
}