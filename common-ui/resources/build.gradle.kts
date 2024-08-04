plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.popular.movies.android.library)
}
android {
    namespace  = "com.ant.resources"
}

dependencies {
    // UI libs
    implementation(libs.coreKtx)
    implementation(libs.appcompat)
    implementation(libs.material)

    // tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}