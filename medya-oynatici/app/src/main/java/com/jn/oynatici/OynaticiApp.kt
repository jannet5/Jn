package com.jn.oynatici

import android.app.Application
import com.jn.oynatici.indir.Motor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

// Uygulama açılınca çalışan sınıf
class OynaticiApp : Application() {
    override fun onCreate() {
        super.onCreate() // üst sınıfı çağır
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch { // arka planda
            runCatching { Motor.hazirla(this@OynaticiApp) } // yt-dlp ve ffmpeg'i önceden hazırla, ilk indirme beklemesin
        }
    }
}
