plugins {
    alias(libs.plugins.swahilib.android.feature.impl)
    alias(libs.plugins.swahilib.android.library.compose)
}

android {
    namespace = "com.swahilib.feature.bookmarks.impl"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.feature.bookmarks.api)
    implementation(projects.feature.topic.api)

    testImplementation(projects.core.testing)

    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    androidTestImplementation(projects.core.testing)
}
