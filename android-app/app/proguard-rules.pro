# Kotlinx serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.jn.winremote.**$$serializer { *; }
-keepclassmembers class com.jn.winremote.** {
    *** Companion;
}
-keepclasseswithmembers class com.jn.winremote.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# OkHttp / okio platform checks
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
