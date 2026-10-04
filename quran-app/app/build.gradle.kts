import java.util.Properties // imza bilgilerini okumak için

plugins {
    id("com.android.application") // Android uygulaması
    id("org.jetbrains.kotlin.android") // Kotlin
    id("org.jetbrains.kotlin.plugin.compose") // Compose derleyicisi
}

// keystore.properties varsa imza bilgileri oradan okunur (dosya git'e girmez)
val imzaDosyasi = rootProject.file("keystore.properties")
val imza = Properties().apply { if (imzaDosyasi.exists()) imzaDosyasi.inputStream().use { load(it) } }

android {
    namespace = "app.kuranoku" // kod paket adı
    compileSdk = 36 // derleme SDK'sı

    defaultConfig {
        applicationId = "app.kuranoku" // mağaza paket adı
        minSdk = 26 // Android 8.0 ve üstü (uyarlanabilir ikon)
        targetSdk = 36 // hedef SDK
        versionCode = 1 // sürüm kodu
        versionName = "1.0.0" // görünen sürüm
        resourceConfigurations += listOf("tr") // sadece Türkçe kaynaklar (APK küçülsün)
    }

    signingConfigs {
        create("release") {
            if (imzaDosyasi.exists()) { // imza dosyası varsa
                storeFile = rootProject.file(imza.getProperty("storeFile")) // anahtar deposu
                storePassword = imza.getProperty("storePassword") // depo şifresi
                keyAlias = imza.getProperty("keyAlias") // anahtar adı
                keyPassword = imza.getProperty("keyPassword") // anahtar şifresi
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true // R8 ile kod küçültme
            isShrinkResources = true // kullanılmayan kaynakları atma
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro") // kurallar
            if (imzaDosyasi.exists()) signingConfig = signingConfigs.getByName("release") // imzalama
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17 // Java 17
        targetCompatibility = JavaVersion.VERSION_17 // Java 17
    }

    buildFeatures { compose = true } // Compose açık

    packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } } // gereksiz lisans dosyaları
}

kotlin { compilerOptions { jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17) } } // Kotlin de Java 17 hedefler

dependencies {
    val bom = platform("androidx.compose:compose-bom:2025.06.01") // Compose sürümlerini tek yerden yönetir
    implementation(bom) // BOM
    implementation("androidx.activity:activity-compose:1.10.1") // Compose ile Activity
    implementation("androidx.compose.ui:ui") // temel arayüz
    implementation("androidx.compose.foundation:foundation") // pager, listeler, jestler
    implementation("androidx.compose.material3:material3") // Material 3 bileşenleri
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.1") // ViewModel
    implementation("androidx.core:core-splashscreen:1.0.1") // açılış ekranı
    testImplementation("junit:junit:4.13.2") // birim testleri
}
