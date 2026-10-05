package com.jn.melodizil.core // Çekirdek testleri

import com.jn.melodizil.data.youtube.ResolvedTrack // İz bilgisi
import com.jn.melodizil.data.youtube.YouTubeSource // İndirici
import org.junit.Assert.assertEquals // Eşitlik
import org.junit.Assert.assertTrue // Doğruluk
import org.junit.Test // JUnit
import java.io.File // Dosya
import java.net.ServerSocket // Yerel soket sunucusu (ağ gerekmez)
import java.util.concurrent.TimeUnit // Zaman
import java.util.concurrent.atomic.AtomicInteger // İstek sayacı
import kotlin.concurrent.thread // İş parçacığı

/** D-012: Bildirilen boyut gerçek dosyadan büyükken (416 yolu) indirme sonsuz döngüye girmemeli ve tam dosya gelmeli. */
class DownloadLoopTest {
    @Test(timeout = 30_000) // 30 sn içinde bitmezse takılma var demektir
    fun abartiliBoyutlaIndirmeBiter() {
        val real = ByteArray(1_500_000) { (it % 251).toByte() } // Gerçek içerik 1.5 MB
        val server = ServerSocket(0, 50, java.net.InetAddress.getByName("127.0.0.1")) // Rastgele port
        val requests = AtomicInteger() // İstek sayacı
        val t = thread(isDaemon = true) { // Basit Range destekli HTTP sunucusu
            while (!server.isClosed) { // Bağlantılar
                val sock = try { server.accept() } catch (e: Exception) { break } // Kabul
                sock.use { s -> // Soket
                    val reader = s.getInputStream().bufferedReader() // İstek okuyucu
                    var range = "bytes=0-" // Varsayılan aralık
                    while (true) { val line = reader.readLine() ?: break; if (line.isEmpty()) break; if (line.startsWith("Range:", true)) range = line.substringAfter(':').trim() } // Başlıklar
                    requests.incrementAndGet() // Say
                    val parts = range.removePrefix("bytes=").split("-") // Aralık parçaları
                    val start = parts[0].toLong(); val reqEnd = parts.getOrNull(1)?.toLongOrNull() ?: (real.size - 1L) // Başlangıç/bitiş
                    val out = s.getOutputStream() // Yanıt akışı
                    if (start >= real.size) { out.write("HTTP/1.1 416 Range Not Satisfiable\r\nContent-Length: 0\r\nConnection: close\r\n\r\n".toByteArray()); out.flush(); return@use } // Aralık dışı
                    val end = minOf(reqEnd, real.size - 1L) // Gerçek bitiş
                    val chunk = real.copyOfRange(start.toInt(), end.toInt() + 1) // Parça
                    out.write("HTTP/1.1 206 Partial Content\r\nContent-Type: audio/mp4\r\nContent-Range: bytes $start-$end/${real.size}\r\nContent-Length: ${chunk.size}\r\nConnection: close\r\n\r\n".toByteArray()) // Başlıklar (gerçek toplam)
                    out.write(chunk); out.flush() // Gövde
                }
            }
        }
        try {
            val url = "http://127.0.0.1:${server.localPort}/a" // Adres
            val track = ResolvedTrack("x", "t", "u", 100, null, url, "audio/mp4", 3_000_000L) // Boyut 2 kat abartılı
            val out = File.createTempFile("indir", ".bin") // Çıktı
            var last = -1f // Son ilerleme
            val client = okhttp3.OkHttpClient.Builder().proxy(java.net.Proxy.NO_PROXY).readTimeout(5, TimeUnit.SECONDS).build() // Yerel sunucu için proxy'siz istemci
            YouTubeSource(client).download(track, out) { last = it } // İndir
            assertEquals(real.size.toLong(), out.length()) // Tam boyut
            assertTrue(out.readBytes().contentEquals(real)) // Birebir aynı içerik
            assertEquals(1f, last, 1e-6f) // İlerleme %100'de bitti
            assertTrue("Çok fazla istek: ${requests.get()}", requests.get() <= 3) // 2 MB parça: 1 parça + en fazla 1 ek istek
            out.delete() // Temizlik
        } finally { server.close(); t.interrupt() } // Sunucuyu kapat
    }
}
