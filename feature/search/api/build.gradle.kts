plugins {
    alias(libs.plugins.swahilib.android.feature.api)
}

android {
    namespace = "com.swahilib.feature.search.api"
}

dependencies {
    implementation(projects.core.domain)
}
