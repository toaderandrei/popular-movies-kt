plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt)
    alias(libs.plugins.popular.movies.android.library)
    alias(libs.plugins.popular.movies.hilt)

}

android {
    namespace  = "com.ant.tmdb.api"
}

dependencies {

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // tmdb Api
    api(libs.tmdb)

    // tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}