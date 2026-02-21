
plugins {
    alias(libs.plugins.popular.movies.android.library)
    alias(libs.plugins.popular.movies.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace  = "com.ant.domain"

}

dependencies {
    implementation(project(":core:models"))
    implementation(project(":core:common"))
    implementation(project(":core:data"))

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}