# Room: KSP uretilen *_Impl siniflari ve Entity/Dao yansima ile yuklenir; R8 AGP+Room tuketici kurallariyla da korur,
# burada acikca da korunur.
-keep class app.nokta.list.data.** { *; }
-keep class * extends androidx.room.RoomDatabase { *; }
-dontwarn androidx.room.paging.**
