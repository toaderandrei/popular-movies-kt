plugins {
    alias(libs.plugins.popular.movies.android.library)
    alias(libs.plugins.popular.movies.hilt)
    alias(libs.plugins.popular.movies.room)
}

android {
    namespace = "com.ant.database"
}

dependencies {
    api(libs.room.runtime)
    implementation(project(":core:models"))
    implementation(libs.kotlinSerialization)
    implementation(libs.gsonConverter)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}