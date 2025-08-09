import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Kapt is used by Glide for annotation processing. If you decide to remove Glide's annotation processor
    // you can omit the kapt plugin.
    id("kotlin-kapt")
}

android {
    namespace = "com.example.kioskapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.kioskapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        // Enable support for Java 8+ features
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            // Debug specific configuration can go here
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // AndroidX core libraries
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // Media3 ExoPlayer components for video playback
    val media3Version = "1.4.1"
    implementation("androidx.media3:media3-exoplayer:$media3Version")
    implementation("androidx.media3:media3-ui:$media3Version")
    implementation("androidx.media3:media3-common:$media3Version")

    // Glide for image and GIF loading
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    // DataStore for key–value storage.  Preferences are stored asynchronously as Flows
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Coroutines for asynchronous operations
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    // Embedded HTTP server (NanoHTTPD) for webhook triggers
    implementation("org.nanohttpd:nanohttpd:2.3.1")

    // MediaSessionCompat for headset media button triggers
    implementation("androidx.media:media:1.6.0")

    // Bluetooth and BLE support
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Material Components for nicer UI (used in SettingsActivity)
    implementation("com.google.android.material:material:1.9.0")
}