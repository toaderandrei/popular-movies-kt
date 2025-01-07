
plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.popular.movies.hilt)
    alias(libs.plugins.popular.movies.android.library)
}

android {
    namespace  = "com.ant.domain"

}

dependencies {
    implementation(project(":data:models"))
    implementation(project(":data:source"))
    implementation(project(":core:common"))

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}