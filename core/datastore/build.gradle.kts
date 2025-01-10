plugins {
    alias(libs.plugins.popular.movies.android.library)
    alias(libs.plugins.popular.movies.hilt)
}

android {
    namespace = "com.ant.datastore"
}

dependencies {
    implementation(project(":core:models"))
    implementation(libs.data.store.preferences)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}