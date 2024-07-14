plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kapt)
    alias(libs.plugins.popular.movies.android.library)
}

android {
    namespace  = "com.ant.models.source"
}

dependencies {
    api(project(":data:models"))
    api(project(":data:tmdbApi"))
    api(project(":common"))
    implementation(libs.kotlinSerialization)

    // image loading
    implementation(libs.coil)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.gsonConverter)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    implementation(libs.firebase.auth)
    implementation(libs.data.store.preferences)

    // tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}