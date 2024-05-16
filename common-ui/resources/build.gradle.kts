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
}