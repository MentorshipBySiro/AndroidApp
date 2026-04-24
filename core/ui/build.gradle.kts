plugins {
    alias(libs.plugins.swahilib.android.library)
    alias(libs.plugins.swahilib.android.library.compose)
    alias(libs.plugins.swahilib.android.library.jacoco)
}

android {
    namespace = "com.swahilib.core.ui"
}

dependencies {
    api(libs.androidx.metrics)
    api(projects.core.analytics)
    api(projects.core.designsystem)
    api(projects.core.model)

    implementation(libs.androidx.browser)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)

    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    androidTestImplementation(projects.core.testing)
}
