plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kapt)
    alias(libs.plugins.popular.movies.android.library)
}

android {
    namespace  = "com.ant.epoxy"

}

dependencies {
    implementation(project(":common"))

    // Epoxy
    api(libs.epoxy)
    api(libs.epoxy.databinding)
    api(libs.epoxy.paging)
    kapt(libs.epoxy.processor)

    // Dagger/Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}