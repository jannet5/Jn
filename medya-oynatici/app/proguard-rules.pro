# Güvenli küçültme: sadece Compose ikon kütüphanesindeki kullanılmayan ikonlar atılsın,
# geri kalan her sınıf olduğu gibi kalsın (yt-dlp, Jackson, Media3 yansıma kullanıyor).
-keep class !androidx.compose.material.icons.**, ** { *; }
# İsimleri karıştırma (hata ayıklaması kolay olsun)
-dontobfuscate
# Eksik isteğe bağlı sınıflar için uyarı verme
-dontwarn **
