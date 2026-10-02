package com.jn.oynatici.oynat

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.jn.oynatici.MainActivity
import java.io.File

// Çalma servisi: ekran kapalıyken de müzik çalar, bildirimde ve kilit ekranında kontroller çıkar
class CalmaServisi : MediaSessionService() {
    private var oturum: MediaSession? = null // medya oturumu

    override fun onCreate() {
        super.onCreate() // üst sınıfı çağır
        val oynatici = ExoPlayer.Builder(this)
            .setAudioAttributes( // ses odağını yönet (arama gelince dur)
                AudioAttributes.Builder().setUsage(C.USAGE_MEDIA).setContentType(C.AUDIO_CONTENT_TYPE_MUSIC).build(), true,
            )
            .setHandleAudioBecomingNoisy(true) // kulaklık çıkınca durdur
            .setWakeMode(C.WAKE_MODE_LOCAL) // ekran kapalıyken çalmaya devam
            .build() // oynatıcıyı oluştur
        oturum = MediaSession.Builder(this, oynatici)
            .setSessionActivity( // bildirime dokununca uygulamayı aç
                PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE),
            )
            .setCallback(object : MediaSession.Callback {
                // Ekrandan gelen şarkıları dosya yoluyla yeniden oluştur (dosya yolu = mediaId)
                override fun onAddMediaItems(
                    mediaSession: MediaSession,
                    controller: MediaSession.ControllerInfo,
                    mediaItems: MutableList<MediaItem>,
                ): ListenableFuture<MutableList<MediaItem>> = Futures.immediateFuture(
                    mediaItems.map { it.buildUpon().setUri(Uri.fromFile(File(it.mediaId))).build() }.toMutableList(), // her öğeye dosya adresini ekle
                )
            })
            .build() // oturumu oluştur
    }

    // Ekran bu oturuma bağlanır
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? = oturum

    // Uygulama son kullanılanlardan kaydırılınca: çalmıyorsa servisi kapat
    override fun onTaskRemoved(rootIntent: Intent?) {
        val oynatici = oturum?.player // oynatıcı
        if (oynatici == null || !oynatici.playWhenReady || oynatici.mediaItemCount == 0) stopSelf() // çalmıyorsa kapat
    }

    override fun onDestroy() {
        oturum?.run { player.release(); release() } // oynatıcıyı ve oturumu serbest bırak
        oturum = null // referansı temizle
        super.onDestroy() // üst sınıfı çağır
    }
}
