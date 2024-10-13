plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kapt)
    alias(libs.plugins.popular.movies.android.library)
}

android {
    buildFeatures {
        //noinspection DataBindingWithoutKapt
        dataBinding = true
    }
    namespace  = "com.ant.core"
}

dependencies {
    api(project(":data:models"))
    implementation(project(":common"))
    //navigation
    implementation(libs.appcompat)

    implementation(libs.recyclerview)
    // Paging runtime
    implementation(libs.pagingRuntime)

    // Dagger - Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}