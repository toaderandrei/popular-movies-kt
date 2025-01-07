plugins {
    alias(libs.plugins.popular.movies.android.library)
    alias(libs.plugins.popular.movies.hilt)
    alias(libs.plugins.popular.movies.room)
}

android {
    namespace  = "com.ant.models.source"
}

dependencies {
    api(project(":data:models"))
    api(project(":data:tmdbApi"))
    api(project(":core:common"))
    implementation(libs.kotlinSerialization)

    // image loading
    implementation(libs.coil)
    implementation(libs.gsonConverter)

    implementation(libs.firebase.auth)
    implementation(libs.data.store.preferences)

    // tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}