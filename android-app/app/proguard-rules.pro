# Keep kotlinx.serialization models (protocol DTOs) — needed since we serialize/deserialize
# by reflection-free codegen, but ProGuard can still strip "unused" fields without this.
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class com.cepgozcu.app.net.protocol.** {
    *** Companion;
}
-keepclasseswithmembers class com.cepgozcu.app.net.protocol.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.cepgozcu.app.net.protocol.**$$serializer { *; }
-keepclassmembers class com.cepgozcu.app.net.protocol.** {
    *** Companion;
}

# Room entities
-keep class com.cepgozcu.app.data.** { *; }
