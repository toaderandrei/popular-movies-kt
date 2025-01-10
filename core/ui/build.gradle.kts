plugins {
    alias(libs.plugins.popular.movies.android.feature)
    alias(libs.plugins.popular.movies.android.library.compose)
}

android {
    namespace = "com.ant.feature.movies"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:resources"))
    implementation(project(":core:models"))
    implementation(project(":core:tmdbApi"))


    // Coil compose
    implementation(libs.coil.kt.compose)
    implementation(libs.coil)

    implementation(libs.coreKtx)
    implementation(libs.navigation.ui)
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.navigation.compose)
}
