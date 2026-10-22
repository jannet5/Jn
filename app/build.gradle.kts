import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"
    id("io.github.takahirom.roborazzi")
}

// Release signing reads from release/keystore.properties, which is git-ignored and never
// committed (see docs/RELEASE.md "İmzalama"). Falls back to debug signing when absent so
// `assembleRelease` still works for local/CI builds before a real release key exists.
val releaseKeystoreProperties = Properties().apply {
    val propsFile = rootProject.file("release/keystore.properties")
    if (propsFile.exists()) propsFile.inputStream().use { load(it) }
}
val hasReleaseSigningConfig = releaseKeystoreProperties.getProperty("storeFile") != null

android {
    namespace = "com.sende.app"
    compileSdk = 36

    signingConfigs {
        // Public, intentionally non-secret key shared by every machine so a sideloaded
        // debug/preview APK can be updated in place no matter who built it.
        create("sideload") {
            storeFile = file("sideload-test.jks")
            storePassword = "sende-test"
            keyAlias = "sideload"
            keyPassword = "sende-test"
        }
    }

    if (hasReleaseSigningConfig) {
        signingConfigs {
            create("release") {
                storeFile = rootProject.file(releaseKeystoreProperties.getProperty("storeFile"))
                storePassword = releaseKeystoreProperties.getProperty("storePassword")
                keyAlias = releaseKeystoreProperties.getProperty("keyAlias")
                keyPassword = releaseKeystoreProperties.getProperty("keyPassword")
            }
        }
    }

    defaultConfig {
        applicationId = "com.sende.app"
        minSdk = 26
        targetSdk = 36
        // CI passes its run number, so every published APK is a valid in-place update over the last one
        // (Android only accepts an update whose versionCode is not lower). Play uploads: -PversionCode=N.
        versionCode = (findProperty("versionCode") as String?)?.toIntOrNull()
            ?: System.getenv("GITHUB_RUN_NUMBER")?.toIntOrNull()
            ?: 1
        versionName = "0.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true

        // How the app asks for money: donate (default, nothing is ever locked) | trial_silent |
        // trial_warn (the two A/B test variants). Chosen per build: -Pmonetization=trial_warn
        buildConfigField("String", "MONETIZATION", "\"${providers.gradleProperty("monetization").getOrElse("donate")}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            if (hasReleaseSigningConfig) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("sideload")
        }
        // Minified like release (~2.5 MB vs ~21 MB debug) but signed with the debug key so it
        // can be sideloaded for testing. Separate applicationId so it never collides with a
        // future Play-signed com.sende.app install.
        create("preview") {
            initWith(getByName("release"))
            signingConfig = signingConfigs.getByName("sideload")
            applicationIdSuffix = ".preview"
            matchingFallbacks += listOf("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf(
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
        )
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    // "sideload": the APK you can hand to anyone. No billing, no update library, and no network
    // code at all, so the "your data never leaves the phone" promise is literally true.
    // "play": the Google Play build. Adds Play Billing (supporter plans). Updates: Google Play updates
    // the app by itself; the in-app update library is deliberately NOT used because it drags in the
    // INTERNET permission (via Google Data Transport) and would break the "no internet" promise.
    flavorDimensions += "dist"
    productFlavors {
        create("sideload") { dimension = "dist" }
        create("play") { dimension = "dist" }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.09.03")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.6")
    implementation("androidx.activity:activity-compose:1.9.2")

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    implementation("androidx.datastore:datastore-preferences:1.1.1")

    implementation("io.insert-koin:koin-android:3.5.6")
    implementation("io.insert-koin:koin-androidx-compose:3.5.6")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")

    "playImplementation"("com.android.billingclient:billing-ktx:8.0.0")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
    testImplementation("app.cash.turbine:turbine:1.1.0")
    testImplementation("com.google.truth:truth:1.4.4")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.robolectric:robolectric:4.13")
    testImplementation("androidx.test:core:1.6.1")
    testImplementation("androidx.compose.ui:ui-test-junit4")
    testImplementation("io.github.takahirom.roborazzi:roborazzi:1.32.2")
    testImplementation("io.github.takahirom.roborazzi:roborazzi-compose:1.32.2")
    testImplementation("io.github.takahirom.roborazzi:roborazzi-junit-rule:1.32.2")

    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("com.google.truth:truth:1.4.4")
}
