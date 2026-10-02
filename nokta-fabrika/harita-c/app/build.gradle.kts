plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "app.nokta.c"
    compileSdk = 34

    defaultConfig {
        applicationId = "app.nokta.c"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }

    // Şifre dosyada değil, ortam değişkeninde (NOKTA_C_KS_PASS).
    val ksPass: String? = System.getenv("NOKTA_C_KS_PASS")
    signingConfigs {
        if (ksPass != null) {
            create("release") {
                storeFile = rootProject.file("keystore/nokta-c.jks")
                storePassword = ksPass
                keyAlias = "nokta"
                keyPassword = ksPass
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            if (ksPass != null) signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf("-Xno-param-assertions", "-Xno-call-assertions", "-Xno-receiver-assertions")
    }
    packaging {
        resources.excludes += setOf("/META-INF/**", "/kotlin/**", "**.kotlin_builtins", "DebugProbesKt.bin", "kotlin-tooling-metadata.json")
    }
    dependenciesInfo { includeInApk = false; includeInBundle = false }
    lint { abortOnError = false }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.json:json:20240303")
}
