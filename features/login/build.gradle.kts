plugins {
    alias(libs.plugins.popular.movies.android.feature)
    alias(libs.plugins.popular.movies.android.library.compose)
}

android {
    namespace = "com.ant.feature.movies"
}

dependencies {
    // Projects
    implementation(project(":core:analytics"))
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:models"))
    implementation(project(":core:domain"))
    implementation(project(":core:resources"))

    // Libraries
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.accompanist.permissions)
}
