plugins { id("com.android.application") }
android {
    namespace = "com.bocekringi.app"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.bocekringi.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes { release { isMinifyEnabled = false; signingConfig = signingConfigs.getByName("debug") } }
}
// Oyun dosyasını derlemeden önce assets içine kopyala
val copyGame by tasks.registering(Copy::class) {
    from("../../game/bocek-ringi.html") { rename { "index.html" } }
    into("src/main/assets")
}
tasks.named("preBuild") { dependsOn(copyGame) }
