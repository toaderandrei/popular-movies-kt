plugins {
    alias(libs.plugins.kapt)
    alias(libs.plugins.popular.movies.android.library)
}
android {
    buildFeatures {
        //noinspection DataBindingWithoutKapt
        dataBinding = true
    }
    namespace  = "com.ant.layouts"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:resources"))
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