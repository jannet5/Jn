// Uygulama modülünün eklentileri
plugins {
    id("com.android.application") // Android uygulaması
    id("org.jetbrains.kotlin.android") // Kotlin desteği
    id("org.jetbrains.kotlin.plugin.compose") // Jetpack Compose derleyicisi
}

android {
    namespace = "com.jn.oynatici" // kod paket adı
    compileSdk = 36 // derlenecek Android API seviyesi

    defaultConfig {
        applicationId = "com.jn.oynatici" // telefondaki uygulama kimliği
        minSdk = 26 // en az Android 8.0
        targetSdk = 35 // hedef Android 15
        versionCode = 1 // mağaza/sürüm numarası
        versionName = "1.0" // kullanıcıya görünen sürüm
    }

    // İmza: aynı anahtarla imzalanan güncellemeler eskisinin üstüne kurulabilir
    signingConfigs {
        create("release") {
            storeFile = file("../keystore/oynatici.jks") // anahtar deposu dosyası
            storePassword = "oynatici123" // depo şifresi
            keyAlias = "oynatici" // anahtar adı
            keyPassword = "oynatici123" // anahtar şifresi
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true // kullanılmayan kodu (özellikle ikonları) atar, APK küçülür
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro") // küçültme kuralları
            signingConfig = signingConfigs.getByName("release") // release imzası
        }
        debug {
            signingConfig = signingConfigs.getByName("release") // debug da aynı imzayla, üst üste kurulabilsin
        }
    }

    // Her işlemci türü için ayrı, küçük APK üret
    splits {
        abi {
            isEnable = true // ABI bölmeyi aç
            reset() // varsayılan listeyi temizle
            include("arm64-v8a", "armeabi-v7a", "x86_64") // modern telefon, eski telefon, emülatör
            isUniversalApk = false // hepsini içeren dev APK üretme
        }
    }

    // yt-dlp (python) ve ffmpeg dosyaları diske açılmak zorunda
    packaging {
        jniLibs { useLegacyPackaging = true } // native kütüphaneleri sıkıştırılmış paketle (extractNativeLibs)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17 // Java 17 dil özellikleri
        targetCompatibility = JavaVersion.VERSION_17 // Java 17 bytecode
    }
    kotlinOptions { jvmTarget = "17" } // Kotlin de Java 17 hedefler

    buildFeatures { compose = true } // Compose arayüzü açık

    lint {
        checkReleaseBuilds = false // Media3 "unstable api" uyarıları derlemeyi durdurmasın
        abortOnError = false // lint hatası derlemeyi durdurmasın
    }
}

dependencies {
    val media3 = "1.11.1" // Media3 sürümü
    val ytdl = "0.18.1" // youtubedl-android sürümü

    implementation(platform("androidx.compose:compose-bom:2026.06.01")) // Compose sürümlerini tek yerden yönet
    implementation("androidx.compose.ui:ui") // Compose temel arayüz
    implementation("androidx.compose.material3:material3") // Material 3 bileşenleri
    implementation("androidx.compose.material:material-icons-extended") // ikonlar
    implementation("androidx.activity:activity-compose:1.12.4") // Activity + Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0") // ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.10.0") // yaşam döngüsü yardımcıları
    implementation("androidx.documentfile:documentfile:1.1.0") // telefondaki klasörleri okumak için

    implementation("androidx.media3:media3-exoplayer:$media3") // ses/video oynatıcı
    implementation("androidx.media3:media3-session:$media3") // arka planda çalma + bildirim kontrolleri
    implementation("androidx.media3:media3-ui:$media3") // video ekranı (PlayerView)
    implementation("androidx.media3:media3-transformer:$media3") // video kırpma
    implementation("androidx.media3:media3-effect:$media3") // Transformer'ın ihtiyaç duyduğu efekt modülü
    implementation("androidx.media3:media3-common:$media3") // ortak Media3 sınıfları

    implementation("io.github.junkfood02.youtubedl-android:library:$ytdl") // telefonda çalışan yt-dlp
    implementation("io.github.junkfood02.youtubedl-android:ffmpeg:$ytdl") // telefonda çalışan ffmpeg (mp3 dönüştürme, kırpma)

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2") // arka plan işleri
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-guava:1.10.2") // Media3 Future -> coroutine
}
