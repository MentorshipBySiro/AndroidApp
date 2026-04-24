plugins {
    alias(libs.plugins.swahilib.jvm.library)
    alias(libs.plugins.swahilib.hilt)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
}
