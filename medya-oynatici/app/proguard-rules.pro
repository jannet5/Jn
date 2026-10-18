# Küçültme açık: kullanılmayan kod atılır, APK küçülür.
# yt-dlp/ffmpeg kütüphanesi ve JSON okuyucusu yansıma kullandığı için olduğu gibi kalsın.
-keep class com.yausername.** { *; }
-keep class com.fasterxml.jackson.** { *; }
-keep class org.apache.commons.** { *; }
# Kendi kodumuz olduğu gibi kalsın (servisler manifestte adla çağrılıyor)
-keep class com.jn.oynatici.** { *; }
# İsimleri karıştırma (hata ayıklaması kolay olsun)
-dontobfuscate
# Eksik isteğe bağlı sınıflar için uyarı verme
-dontwarn **
