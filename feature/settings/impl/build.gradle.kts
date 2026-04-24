plugins {
    alias(libs.plugins.swahilib.android.feature.impl)
    alias(libs.plugins.swahilib.android.library.compose)
    alias(libs.plugins.swahilib.android.library.jacoco)
}

android {
    namespace = "com.swahilib.feature.settings.impl"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.google.oss.licenses)
    implementation(projects.core.data)

    testImplementation(projects.core.testing)

    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
}
