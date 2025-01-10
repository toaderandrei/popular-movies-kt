plugins {
    alias(libs.plugins.popular.movies.android.library)
    alias(libs.plugins.popular.movies.hilt)
}

android {
    namespace  = "com.ant.epoxy"

}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:ui"))

    // Epoxy
    api(libs.epoxy)
    api(libs.epoxy.databinding)
    api(libs.epoxy.paging)

    // Dagger/Hilt
    implementation(libs.hilt.android)

    // tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}