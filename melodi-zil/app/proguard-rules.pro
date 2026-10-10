# NewPipeExtractor ve Rhino JS motoru yansıma kullanır; küçültmede korunur
-keep class org.schabi.newpipe.extractor.** { *; }
-keep class org.mozilla.javascript.** { *; }
-keep class org.mozilla.classfile.** { *; }
-dontwarn org.mozilla.javascript.**
-dontwarn org.schabi.newpipe.extractor.**
-dontwarn javax.script.**
-dontwarn java.beans.**
-dontwarn org.jsoup.**
# protobuf-javalite kendi mesaj sınıflarını yansımayla okur
-keep class com.google.protobuf.** { *; }
-dontwarn com.google.protobuf.**
# kotlinx.serialization için üretilen serializer'lar korunur
-keepattributes *Annotation*, InnerClasses
-keep,includedescriptorclasses class com.jn.melodizil.**$$serializer { *; }
-keepclassmembers class com.jn.melodizil.** { *** Companion; }
-keepclasseswithmembers class com.jn.melodizil.** { kotlinx.serialization.KSerializer serializer(...); }
# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# TensorFlow Lite (JNI ile çağrılan sınıflar korunur)
-keep class org.tensorflow.lite.** { *; }
-dontwarn org.tensorflow.lite.**
