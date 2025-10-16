plugins {
    alias(libs.plugins.popular.movies.android.feature)
    alias(libs.plugins.popular.movies.android.library.compose)
}

android {
    namespace = "com.ant.feature.search"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:domain"))
    implementation(project(":core:models"))
    implementation(project(":features:movies"))
    implementation(project(":features:tvshow"))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.accompanist.permissions)
}
