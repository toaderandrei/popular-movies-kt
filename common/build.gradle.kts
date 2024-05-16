plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kapt)
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
    implementation(libs.navigationFragment)

    implementation(libs.timber)
    implementation(libs.material)

    // retrofit
    implementation(libs.retrofit)

    // Dagger - Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    api(libs.room.runtime)
    implementation(libs.roomRxjava)
    kapt(libs.room.compiler)
}
