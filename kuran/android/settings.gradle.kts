pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        // Bazı ağlarda mavenCentral() bazı dosyaları getiremiyor: Google'ın Maven Central aynası önce denenir
        maven { url = uri("https://maven-central.storage-download.googleapis.com/maven2/") }
        mavenCentral()
    }
}

rootProject.name = "KuranOkuyucu"
include(":app")
