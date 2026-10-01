// Gradle eklentilerinin indirileceği depolar
pluginManagement {
    repositories {
        google() // Android eklentileri için Google deposu
        mavenCentral() // genel kütüphaneler için Maven Central
        gradlePluginPortal() // Gradle eklenti portalı
    }
}

// Uygulama kütüphanelerinin indirileceği depolar
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // depolar sadece burada tanımlanabilir
    repositories {
        google() // AndroidX kütüphaneleri
        mavenCentral() // Coil, OkHttp vb.
    }
}

rootProject.name = "YaziKart" // projenin adı
include(":app") // tek modül: uygulama
