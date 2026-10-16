package com.jn.melodizil.service // Arka plan servisi

import android.app.Notification // Bildirim
import android.app.NotificationChannel // Bildirim kanalı
import android.app.NotificationManager // Bildirim yöneticisi
import android.app.PendingIntent // Bildirime basınca uygulamayı açmak için
import android.app.Service // Servis
import android.content.Context // Bağlam
import android.content.Intent // Intent
import android.content.pm.ServiceInfo // Ön plan servis türü
import android.os.Build // Sürüm kontrolü
import android.os.IBinder // Bağlama (kullanılmıyor)
import androidx.core.app.NotificationCompat // Uyumlu bildirim
import androidx.core.app.ServiceCompat // Uyumlu startForeground
import androidx.core.content.ContextCompat // Uyumlu startForegroundService
import com.jn.melodizil.MainActivity // Bildirime basınca açılacak ekran
import com.jn.melodizil.R // Kaynaklar

/**
 * Melodi çıkarılırken süreci ön planda tutan servis.
 * İş ViewModel'de yürür; bu servis yalnızca telefonun (özellikle MIUI/ColorOS gibi agresif pil yöneticilerinin)
 * uygulama arka plana alınınca süreci öldürmesini engeller ve ilerlemeyi bildirimde gösterir.
 */
class ProcessingService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null // Bağlanma yok

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) { stopSelfCompat(); return START_NOT_STICKY } // Durdurma isteği
        val text = intent?.getStringExtra(EXTRA_TEXT) ?: getString(R.string.process_title) // Bildirim metni
        val percent = intent?.getIntExtra(EXTRA_PERCENT, -1) ?: -1 // Yüzde (-1 = belirsiz)
        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC else 0 // Android 14+ tür ister
        ServiceCompat.startForeground(this, NOTIF_ID, build(this, text, percent), type) // Ön plana geç (her güncellemede aynı bildirim yenilenir)
        return START_NOT_STICKY // Süreç ölürse yeniden başlatma: iş zaten ViewModel'deydi
    }

    private fun stopSelfCompat() { ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE); stopSelf() } // Bildirimi kaldır ve dur

    companion object {
        private const val CHANNEL_ID = "isleme" // Kanal kimliği
        private const val NOTIF_ID = 42 // Bildirim kimliği
        private const val ACTION_STOP = "com.jn.melodizil.STOP" // Durdurma eylemi
        private const val EXTRA_TEXT = "text" // Metin anahtarı
        private const val EXTRA_PERCENT = "percent" // Yüzde anahtarı

        /** Servisi başlatır ya da bildirimi günceller. Başlatılamazsa (ör. arka plan kısıtı) sessizce geçer: iş yine sürer. */
        fun update(context: Context, text: String, percent: Int) {
            val i = Intent(context, ProcessingService::class.java).putExtra(EXTRA_TEXT, text).putExtra(EXTRA_PERCENT, percent) // İstek
            runCatching { ContextCompat.startForegroundService(context, i) } // Başlat/güncelle
        }

        /** İş bitince ya da iptal edilince servisi durdurur. */
        fun stop(context: Context) {
            runCatching { context.startService(Intent(context, ProcessingService::class.java).setAction(ACTION_STOP)) } // Durdurma isteği
                .onFailure { runCatching { context.stopService(Intent(context, ProcessingService::class.java)) } } // Olmazsa doğrudan durdur
        }

        private fun build(context: Context, text: String, percent: Int): Notification {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Android 8+: kanal gerekir
                val nm = context.getSystemService(NotificationManager::class.java) // Yönetici
                if (nm.getNotificationChannel(CHANNEL_ID) == null) // Kanal yoksa
                    nm.createNotificationChannel(NotificationChannel(CHANNEL_ID, context.getString(R.string.notif_channel), NotificationManager.IMPORTANCE_LOW).apply { setShowBadge(false) }) // Sessiz kanal
            }
            val open = PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT) // Uygulamaya dön
            return NotificationCompat.Builder(context, CHANNEL_ID) // Bildirim
                .setSmallIcon(R.drawable.ic_notification) // Küçük ikon
                .setContentTitle(context.getString(R.string.process_title)) // Başlık
                .setContentText(text) // Adım
                .setProgress(100, percent.coerceAtLeast(0), percent < 0) // İlerleme çubuğu
                .setOngoing(true).setOnlyAlertOnce(true).setSilent(true) // Kaydırılamaz, sessiz
                .setContentIntent(open) // Basınca aç
                .build() // Oluştur
        }
    }
}
