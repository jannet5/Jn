import java.util.Properties // keystore.properties dosyasını okumak için

plugins {
    id("com.android.application") // Android uygulama modülü
    id("org.jetbrains.kotlin.android") // Kotlin desteği
    id("org.jetbrains.kotlin.plugin.compose") // Jetpack Compose derleyicisi
    id("org.jetbrains.kotlin.plugin.serialization") // JSON serileştirme
    id("com.github.triplet.play") // Google Play'e komutla yükleme (src/main/play/ altındaki metin ve görseller)
}

android {
    namespace = "com.jn.melodizil" // Kotlin paket kökü ve R sınıfı ad alanı
    compileSdk = 35 // Android 15 SDK ile derlenir

    defaultConfig {
        applicationId = "com.jn.melodizil" // Google Play'deki benzersiz uygulama kimliği
        minSdk = 26 // Android 8.0 ve üzeri (MediaCodec/MediaStore API'leri için yeterli)
        targetSdk = 35 // Play'in 2025 sonrası zorunlu hedef SDK'sı
        versionCode = 1 // Her mağaza yüklemesinde artırılır
        versionName = "1.0.0" // Kullanıcıya görünen sürüm
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // Cihaz testleri için koşucu
        vectorDrawables { useSupportLibrary = true } // Vektör ikonlar eski sürümlerde de çalışsın
    }

    signingConfigs {
        create("release") { // Yayın imzası keystore.properties dosyasından okunur
            val props = Properties() // Özellik okuyucu
            val f = rootProject.file("keystore.properties") // Kök klasördeki imza bilgileri
            if (f.exists()) { // Dosya varsa imza bilgileri yükleniyor
                props.load(f.inputStream()) // Dosya okunuyor
                storeFile = rootProject.file(props.getProperty("storeFile")) // Anahtar deposu dosyası
                storePassword = props.getProperty("storePassword") // Depo şifresi
                keyAlias = props.getProperty("keyAlias") // Anahtar adı
                keyPassword = props.getProperty("keyPassword") // Anahtar şifresi
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true // R8 ile kod küçültme (Play için daha küçük AAB)
            isShrinkResources = true // Kullanılmayan kaynaklar atılır
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro") // Küçültme kuralları
            if (rootProject.file("keystore.properties").exists()) signingConfig = signingConfigs.getByName("release") // İmza uygulanır
        }
        debug {
            isDebuggable = true // Hata ayıklama açık
            applicationIdSuffix = ".debug" // Debug ve release aynı cihazda yan yana kurulabilsin
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17 // Java 17 kaynak uyumluluğu
        targetCompatibility = JavaVersion.VERSION_17 // Java 17 hedef uyumluluğu
        isCoreLibraryDesugaringEnabled = true // java.time gibi API'ler minSdk 26 altında da çalışsın (ileride düşürülürse)
    }
    kotlinOptions { jvmTarget = "17" } // Kotlin JVM hedefi

    buildFeatures { compose = true } // Compose açık

    packaging {
        resources { excludes += setOf("/META-INF/{AL2.0,LGPL2.1}", "/META-INF/DEPENDENCIES") } // Çakışan lisans dosyaları atılır
    }

    sourceSets {
        getByName("main") { kotlin.srcDirs("src/main/kotlin") } // Ana kaynaklar
        getByName("test") { kotlin.srcDirs("src/test/kotlin") } // Birim testleri
    }

    testOptions { unitTests { isReturnDefaultValues = true } } // Android sınıfları JVM testinde varsayılan değer döndürür

    bundle { language { enableSplit = false } } // Tüm diller tek pakette (sadece Türkçe/İngilizce var)
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.12.01") // Compose sürümlerini hizalayan BOM
    implementation(composeBom) // BOM uygulanıyor
    androidTestImplementation(composeBom) // Cihaz testleri için de BOM

    implementation("androidx.core:core-ktx:1.15.0") // Kotlin uzantıları
    implementation("androidx.core:core-splashscreen:1.0.1") // Android 12+ uyumlu açılış ekranı
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7") // Yaşam döngüsü
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7") // Compose ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7") // collectAsStateWithLifecycle
    implementation("androidx.activity:activity-compose:1.9.3") // Compose Activity
    implementation("androidx.compose.ui:ui") // Compose çekirdek
    implementation("androidx.compose.ui:ui-graphics") // Çizim (dalga formu, piyano rulosu)
    implementation("androidx.compose.ui:ui-tooling-preview") // Önizleme
    implementation("androidx.compose.material3:material3") // Material 3 bileşenleri
    implementation("androidx.compose.material:material-icons-extended") // Tek ikon seti (Material Symbols)
    implementation("androidx.navigation:navigation-compose:2.8.5") // Ekranlar arası gezinme
    implementation("androidx.datastore:datastore-preferences:1.1.1") // Ayarlar (tema, geçmiş)
    implementation("io.coil-kt:coil-compose:2.7.0") // Kapak görseli yükleme

    implementation("com.squareup.okhttp3:okhttp:4.12.0") // HTTP istemcisi (indirme)
    implementation("com.github.TeamNewPipe:NewPipeExtractor:v0.26.5") // YouTube ses akışı çözücü (açık kaynak, NewPipe)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0") // Eşzamanlılık
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3") // Geçmiş kayıtları JSON
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs_nio:2.1.5") // Java API desugaring (NIO sürümü: NewPipeExtractor'ın URLDecoder.decode(String, Charset) gibi Java 10+ çağrıları için; NewPipe uygulamasıyla aynı)

    debugImplementation("androidx.compose.ui:ui-tooling") // Önizleme araçları
    testImplementation("junit:junit:4.13.2") // Birim test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0") // Coroutine testi
}

// Lint: adaptive ikon klasörü -v26 niteleyicisi AAPT için zorunlu; bağımlılık sürüm uyarıları yayın öncesi kasıtlı sabitlendi
android.lint { disable += setOf("ObsoleteSdkInt", "GradleDependency"); warningsAsErrors = false; abortOnError = true }

// Gradle Play Publisher: Play Console servis hesabı JSON'u varsa `./gradlew publishBundle` ile AAB + mağaza kaydı yüklenir.
// Anahtar dosyası depoya konmaz; PLAY_CREDENTIALS ortam değişkeni ya da kök klasörde play-credentials.json (gitignore'da) kullanılır.
play {
    val credPath = System.getenv("PLAY_CREDENTIALS") ?: rootProject.file("play-credentials.json").path // Anahtar yolu
    val credFile = file(credPath) // Anahtar dosyası
    enabled.set(credFile.exists()) // Anahtar yoksa yayın görevleri kapalı; normal derleme etkilenmez
    if (credFile.exists()) serviceAccountCredentials.set(credFile) // Anahtar varsa bağla
    track.set("internal") // İlk yükleme dahili test kanalına (güvenli)
    defaultToAppBundles.set(true) // APK değil AAB yüklenir
    releaseStatus.set(com.github.triplet.gradle.androidpublisher.ReleaseStatus.DRAFT) // Taslak: son onay Play Console'da kullanıcıda
}
