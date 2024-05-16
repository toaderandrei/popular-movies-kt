plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kapt)
    alias(libs.plugins.popular.movies.android.library)
}
android {
    buildFeatures {
        dataBinding = true
    }
    namespace  = "com.ant.layouts"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":common-ui:resources"))
    implementation(project(":common-ui:bindings"))
    implementation(project(":data:models"))

    // UI libs
    implementation(libs.coreKtx)
    implementation(libs.constraintLayout)
    implementation(libs.circle.image)
    implementation(libs.material)

    // Epoxy
    implementation(libs.epoxy)
    implementation(libs.epoxy.paging)
    implementation(libs.epoxy.databinding)
    kapt(libs.epoxy.processor)
}