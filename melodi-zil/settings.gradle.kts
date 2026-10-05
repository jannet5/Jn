// Eklenti depoları: AGP için google, Kotlin için mavenCentral, diğerleri için plugin portal
pluginManagement {
    repositories {
        google() // Android Gradle Plugin buradan gelir
        mavenCentral() // Kotlin eklentileri buradan gelir
        gradlePluginPortal() // Diğer Gradle eklentileri
    }
}
// Bağımlılık depoları: proje bazlı depo tanımı yasak, hepsi burada
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // Modüller kendi deposunu ekleyemez
    repositories {
        google() // AndroidX ve Compose
        mavenCentral() // OkHttp, coroutines vb.
        maven("https://jitpack.io") // NewPipeExtractor (YouTube ses akışı çözücü)
    }
}
rootProject.name = "MelodiZil" // Proje adı
include(":app") // Tek modül: uygulama
