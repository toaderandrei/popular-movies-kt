plugins {
    alias(libs.plugins.popular.movies.android.library)
    alias(libs.plugins.popular.movies.hilt)
}

android {
    namespace  = "com.ant.models"
}

dependencies {
    implementation(project(":core:tmdbApi"))
    implementation(libs.kotlinSerialization)
    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.retrofit)
    implementation(libs.kotlin.stdlib)

    implementation(libs.gsonConverter)
    testImplementation(libs.junit)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
}
