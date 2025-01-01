plugins {
    alias(libs.plugins.popular.movies.android.library)
    alias(libs.plugins.popular.movies.hilt)
    alias(libs.plugins.kapt)

}
android {
    buildFeatures {
        //noinspection DataBindingWithoutKapt
        dataBinding = true
    }
    namespace  = "com.ant.bindings"
}

dependencies {
    implementation(project(":data:source"))

    // UI libs
    implementation(libs.coreKtx)
    implementation(libs.coil)
    implementation(libs.material)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}