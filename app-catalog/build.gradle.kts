import com.swahilib.FlavorDimension
import com.swahilib.AppFlavor

plugins {
    alias(libs.plugins.swahilib.android.application)
    alias(libs.plugins.swahilib.android.application.compose)
}

android {
    defaultConfig {
        applicationId = "com.appcatalog"
        versionCode = 1
        versionName = "0.0.1"

        missingDimensionStrategy(FlavorDimension.contentType.name, AppFlavor.demo.name)
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    namespace = "com.appcatalog"

    buildTypes {
        release {
            // To publish on the Play store a private signing key is required, but to allow anyone
            // who clones the code to sign and run the release variant, use the debug signing key.
            // TODO: Abstract the signing configuration to a separate file to avoid hardcoding this.
            signingConfig = signingConfigs.named("debug").get()
        }
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)

    implementation(projects.core.designsystem)
    implementation(projects.core.ui)
}

dependencyGuard {
    configuration("releaseRuntimeClasspath")
}
