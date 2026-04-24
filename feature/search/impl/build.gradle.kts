plugins {
    alias(libs.plugins.swahilib.android.feature.impl)
    alias(libs.plugins.swahilib.android.library.compose)
    alias(libs.plugins.swahilib.android.library.jacoco)
}

android {
    namespace = "com.swahilib.feature.search.impl"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.feature.interests.api)
    implementation(projects.feature.search.api)
    implementation(projects.feature.topic.api)

    testImplementation(projects.core.testing)

    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    androidTestImplementation(projects.core.testing)
}
