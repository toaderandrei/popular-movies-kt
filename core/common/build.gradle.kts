plugins {
    alias(libs.plugins.popular.movies.android.library)
    alias(libs.plugins.popular.movies.hilt)
}


android {
    // Add this line
    namespace  = "com.ant.common"
}

dependencies {
    implementation(project(":core:models"))
    implementation(libs.retrofit)

    //navigation
    implementation(libs.timber)

    // Dagger - Hilt
//    implementation(libs.hilt.android)
//    ksp(libs.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
