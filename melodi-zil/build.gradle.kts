// Kök derleme dosyası: eklenti sürümleri burada sabitlenir, modüllerde "apply false" ile uygulanır
plugins {
    id("com.android.application") version "8.7.3" apply false // Android uygulama eklentisi
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false // Kotlin Android eklentisi
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false // Compose derleyici eklentisi (Kotlin 2.x)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21" apply false // kotlinx.serialization eklentisi
    id("com.github.triplet.play") version "3.12.1" apply false // Gradle Play Publisher (Triple-T, 4.3k★, MIT): mağaza kaydı + AAB yükleme. 4.x Gradle 9.1 ister; Gradle 8 ile uyumlu son sürüm 3.12.1
}
