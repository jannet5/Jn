// Gradle eklentilerinin indirileceği depolar
pluginManagement {
    repositories {
        google() // Google'ın Android deposu
        maven("https://maven-central.storage-download.googleapis.com/maven2/") // Maven Central'ın Google aynası (hızlı)
        mavenCentral() // Maven Central deposu
        gradlePluginPortal() // Gradle eklenti portalı
    }
}

// Kütüphanelerin indirileceği depolar
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // modüller kendi deposunu tanımlayamaz
    repositories {
        google() // AndroidX ve Media3 burada
        maven("https://maven-central.storage-download.googleapis.com/maven2/") // Maven Central'ın Google aynası
        mavenCentral() // youtubedl-android (yt-dlp) burada
    }
}

rootProject.name = "Oynatici" // proje adı
include(":app") // tek modül: uygulama
