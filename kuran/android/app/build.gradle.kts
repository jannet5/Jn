import java.util.Properties

plugins {
    id("com.android.application")
}

android {
    namespace = "com.jn.kuran"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.jn.kuran"
        minSdk = 24                      // Android 7.0 ve üstü
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
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
    implementation("androidx.webkit:webkit:1.11.0")   // WebViewAssetLoader: https://appassets... altından yerel dosya sunar
}
