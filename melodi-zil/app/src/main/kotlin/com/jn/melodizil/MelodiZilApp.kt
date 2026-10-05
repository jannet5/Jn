package com.jn.melodizil // Uygulama kökü

import android.app.Application // Uygulama sınıfı
import com.jn.melodizil.data.store.PrefsStore // Tercihler
import com.jn.melodizil.data.store.RingtoneStore // Zil deposu
import com.jn.melodizil.data.youtube.YouTubeSource // YouTube
import com.jn.melodizil.domain.MelodyPipeline // İşlem hattı

/** Süreç ömrü boyunca yaşayan bağımlılıklar burada tek kez oluşturulur (basit DI). */
class MelodiZilApp : Application() {
    val prefs: PrefsStore by lazy { PrefsStore(this) } // Tercihler
    val ringtones: RingtoneStore by lazy { RingtoneStore(this) } // Zil kaydı
    val youTube: YouTubeSource by lazy { YouTubeSource() } // YouTube çözücü (NewPipe init içinde)
    val pipeline: MelodyPipeline by lazy { MelodyPipeline(this, youTube) } // İşlem hattı
}
