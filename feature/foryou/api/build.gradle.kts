plugins {
    alias(libs.plugins.swahilib.android.feature.api)
}

android {
    namespace = "com.swahilib.feature.foryou.api"
}

dependencies {
    api(projects.core.navigation)
}
