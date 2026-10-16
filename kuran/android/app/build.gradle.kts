import java.util.Properties

plugins {
    id("com.android.application")
}

android {
    namespace = "com.jn.kuran"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.jn.kuran"
        minSdk = 24                      // Android 7.0 ve üstü
        targetSdk = 36
        versionCode = 3
        versionName = "1.1.1"
    }

    // Web uygulaması (kuran/www) tek kaynaktır: olduğu gibi APK içine konur
    sourceSets {
        getByName("main") {
            assets.srcDirs("../../www")
        }
    }

    signingConfigs {
        create("release") {
            val p = Properties()
            val f = rootProject.file("keystore.properties")
            if (f.exists()) {
                p.load(f.inputStream())
                storeFile = rootProject.file(p.getProperty("storeFile"))
                storePassword = p.getProperty("storePassword")
                keyAlias = p.getProperty("keyAlias")
                keyPassword = p.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            if (rootProject.file("keystore.properties").exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }

    lint {
        checkReleaseBuilds = false      // lint'in kendi bağımlılığı bu ağda indirilemiyor; uygulama kodu etkilenmez
        abortOnError = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("androidx.webkit:webkit:1.13.0")      // WebViewAssetLoader: https://appassets... altından yerel dosya sunar
    implementation("androidx.activity:activity:1.10.1")  // kenardan kenara ekran + yeni geri hareketi (predictive back)
    implementation("androidx.core:core:1.16.0")          // WindowInsets uyumluluğu
}
