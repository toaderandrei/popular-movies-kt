plugins {
    alias(libs.plugins.popular.movies.android.library)
    alias(libs.plugins.popular.movies.hilt)
}

android {
    namespace = "com.ant.data"
}

dependencies {
    implementation(project(":core:models"))
    implementation(project(":core:network"))
    implementation(project(":core:tmdbApi"))
    implementation(project(":core:database"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}