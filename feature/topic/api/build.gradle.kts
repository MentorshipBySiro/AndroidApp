plugins {
    alias(libs.plugins.swahilib.android.feature.api)
    alias(libs.plugins.swahilib.android.feature.impl)
    alias(libs.plugins.swahilib.android.library.compose)
}

android {
    namespace = "com.swahilib.feature.topic.api"
}
