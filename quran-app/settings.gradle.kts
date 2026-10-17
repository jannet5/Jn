// Eklenti ve bağımlılıkların indirileceği depolar
pluginManagement {
    repositories {
        google() // Android eklentileri
        mavenCentral() // Kotlin ve diğerleri
        gradlePluginPortal() // Gradle eklentileri
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // depolar sadece burada tanımlanır
    repositories {
        google() // AndroidX kütüphaneleri
        mavenCentral() // diğer kütüphaneler
    }
}

rootProject.name = "KuranOku" // proje adı
include(":app") // tek modül: uygulama
