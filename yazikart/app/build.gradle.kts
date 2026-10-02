import java.util.Properties // imza bilgilerini dosyadan okumak için

plugins {
    id("com.android.application") // bu modül bir Android uygulaması
    id("org.jetbrains.kotlin.android") // Kotlin ile yazılıyor
}

android {
    namespace = "com.jn.yazikart" // kod paketinin adı
    compileSdk = 34 // Android 14 SDK'sı ile derleniyor

    defaultConfig {
        applicationId = "com.jn.yazikart" // telefondaki uygulama kimliği
        minSdk = 26 // Android 8.0 ve üstü telefonlarda çalışır
        targetSdk = 34 // Android 14 hedefleniyor
        versionCode = 3 // mağaza sürüm numarası
        versionName = "1.0.2" // kullanıcıya görünen sürüm
    }

    signingConfigs {
        create("release") { // yayın (release) APK'sının imzası
            val props = Properties() // imza ayarları için boş liste
            val file = rootProject.file("keystore.properties") // imza ayar dosyası
            if (file.exists()) { // dosya varsa
                props.load(file.inputStream()) // ayarlar okunuyor
                storeFile = rootProject.file(props.getProperty("storeFile")) // anahtar deposu dosyası
                storePassword = props.getProperty("storePassword") // depo şifresi
                keyAlias = props.getProperty("keyAlias") // anahtar adı
                keyPassword = props.getProperty("keyPassword") // anahtar şifresi
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false // kod küçültme kapalı (basit uygulama)
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro") // varsayılan kurallar
            if (rootProject.file("keystore.properties").exists()) { // imza dosyası varsa
                signingConfig = signingConfigs.getByName("release") // release APK'sı imzalanıyor
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17 // Java 17 dil özellikleri
        targetCompatibility = JavaVersion.VERSION_17 // Java 17 bayt kodu
    }
    kotlinOptions {
        jvmTarget = "17" // Kotlin de Java 17'ye derleniyor
    }

    buildFeatures {
        compose = true // arayüz Jetpack Compose ile yazılıyor
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14" // Kotlin 1.9.24 ile uyumlu Compose derleyicisi
    }

    androidResources {
        noCompress += "ttf" // font dosyaları sıkıştırılmadan paketleniyor (hızlı açılış)
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}" // çakışan lisans dosyaları atılıyor
        }
    }

    sourceSets {
        getByName("main") { kotlin.srcDirs("src/main/kotlin") } // ana kod klasörü
        getByName("test") { kotlin.srcDirs("src/test/kotlin") } // test kod klasörü
    }

    testOptions {
        unitTests { isReturnDefaultValues = true } // testlerde Android sınıfları varsayılan değer döndürür
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.06.00") // Compose kütüphane sürümlerini tek yerden yöneten liste
    implementation(composeBom) // Compose sürüm listesi ekleniyor

    implementation("androidx.core:core-ktx:1.13.1") // Android çekirdek yardımcıları
    implementation("androidx.activity:activity-compose:1.9.1") // Activity + Compose + galeri seçici
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4") // ViewModel desteği
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.4") // akışları Compose'da dinlemek için
    implementation("androidx.compose.ui:ui") // Compose temel arayüz
    implementation("androidx.compose.ui:ui-graphics") // çizim araçları
    implementation("androidx.compose.material3:material3") // Material 3 bileşenleri
    implementation("androidx.compose.material:material-icons-extended") // ikonlar

    implementation("io.coil-kt:coil-compose:2.6.0") // internetten görsel yükleme
    implementation("com.squareup.okhttp3:okhttp:4.12.0") // internet istekleri
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1") // arka plan işleri

    testImplementation("junit:junit:4.13.2") // birim test kütüphanesi
    testImplementation("org.json:json:20240303") // testlerde gerçek JSON ayrıştırıcı
}
