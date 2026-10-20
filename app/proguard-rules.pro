# Sende release shrink rules.
-keep class com.sende.app.data.local.** { *; }
-keepattributes *Annotation*
-keepclassmembers class * {
    @androidx.room.* <methods>;
}
