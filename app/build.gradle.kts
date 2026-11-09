plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

import java.util.Properties

// collection-ktx was merged into androidx.collection:collection; the stale
// 1.2.0 copy duplicates classes in collection 1.4.x -> exclude it.
configurations.all {
    exclude(group = "androidx.collection", module = "collection-ktx")
}

android {
    namespace = "com.smogcheck.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.smogcheck.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "1.1"

        vectorDrawables { useSupportLibrary = true }
    }

    lint {
        // Lint runs its own dependency resolution; disabled for offline release builds.
        checkReleaseBuilds = false
    }

    packaging {
        resources {
            excludes += "**/*.knm"
            excludes += "**/linkdata/**"
            excludes += "**/manifest"
            excludes += "META-INF/*.kotlin_module"
            excludes += "META-INF/kotlin-project-structure-metadata.json"
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    signingConfigs {
        // Release signing configured from signing.properties (NOT committed).
        create("release") {
            val props = Properties()
            val f = rootProject.file("signing.properties")
            if (f.exists()) {
                props.load(f.inputStream())
                storeFile = file(props.getProperty("storeFile"))
                storePassword = props.getProperty("storePassword")
                keyAlias = props.getProperty("keyAlias")
                keyPassword = props.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Falls back to debug signing if signing.properties is absent (local dev).
            signingConfig = signingConfigs.getByName(
                if (rootProject.file("signing.properties").exists()) "release" else "debug"
            )
        }
        debug {
            applicationIdSuffix = ".debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }

    buildFeatures { compose = true }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.10.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Core
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")

    // Compose UI + Material 3
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // EXIF rotation handling for camera/gallery photos
    implementation("androidx.exifinterface:exifinterface:1.3.7")
}
