package com.jn.melodizil.core // Çekirdek testleri

import com.jn.melodizil.data.youtube.YouTubeSource // Kaynak
import org.junit.Assume // Atlama
import org.junit.Test // JUnit
import java.io.File // Dosya

/** Gerçek YouTube'a karşı çözümleme ve indirme denemesi. MELODI_LIVE=1 ile çalışır; CI'da atlanır. */
class YouTubeLiveTest {
    @Test
    fun gercekVideoyuCozVeIndir() {
        Assume.assumeTrue("MELODI_LIVE yok", System.getenv("MELODI_LIVE") == "1") // Atla
        val src = YouTubeSource() // Kaynak
        val t = src.resolve("https://youtu.be/dQw4w9WgXcQ?si=abc") // Çöz
        println("BAŞLIK: ${t.title} | ${t.uploader} | ${t.durationSec}s | ${t.mimeType} | bytes=${t.approxBytes}") // Yazdır
        println("KAPAK: ${t.thumbnailUrl}") // Kapak
        println("URL: ${t.audioUrl.take(120)}...") // Akış
        val out = File(System.getenv("MELODI_TEST_OUT") ?: "build", "indirilen.bin") // Hedef
        var last = 0f // İlerleme
        src.download(t, out) { if (it - last > 0.2f) { println("İLERLEME %.0f".format(it * 100)); last = it } } // İndir
        println("İNDİRİLEN BAYT: ${out.length()}") // Boyut
        assert(out.length() > 500_000) { "İndirme çok küçük" } // Makul boyut
    }
}
