# TekPanel release shrink rules.
-keep class com.tekpanel.app.data.local.** { *; }
-keepattributes *Annotation*
-keepclassmembers class * {
    @androidx.room.* <methods>;
}
