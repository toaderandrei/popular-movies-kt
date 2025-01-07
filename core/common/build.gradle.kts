plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.popular.movies.hilt)
    alias(libs.plugins.popular.movies.android.library)
}

android {
    // Add this line
    namespace  = "com.ant.common"
}

dependencies {
    implementation(project(":data:models"))
    // Circle ImageView

    //navigation
    implementation(libs.fragment)
    implementation(libs.fragmentKtx)
    implementation(libs.navigation.ui)
    implementation(libs.navigation.fragment.ktx)

    implementation(libs.timber)
    implementation(libs.material)
    ksp(libs.hilt.compiler)

    // retrofit
    implementation(libs.retrofit)

    // Dagger - Hilt
    implementation(libs.hilt.android)

    api(libs.room.runtime)
    implementation(libs.roomRxjava)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
