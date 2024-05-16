plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.popular.movies.android.library)
}

android {
    buildFeatures {
        dataBinding = true
    }
    namespace  = "com.ant.adapters"
}

dependencies {
    api(project(":data:models"))
    implementation(libs.recyclerview)
    // Paging runtime
    implementation(libs.pagingRuntime)
}