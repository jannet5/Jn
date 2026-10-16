package com.jn.yazikart // test paketi

import com.jn.yazikart.data.Clips // video hesapları
import com.jn.yazikart.data.VideoClip // video parçası
import org.junit.Assert.assertEquals // eşitlik
import org.junit.Test // test işareti

// Video süre/aralık hesaplarının testleri
class ClipsTest {
    private val uri = "file:///test.mp4" // testte adres önemli değil
    private fun clip(d: Long, s: Long = 0, e: Long = d) = VideoClip(uri, d, s, e) // kısa üretici

    @Test
    fun toplamSureKirpilmisUzunluklardir() { // 2 video: 5 sn + 3 sn
        assertEquals(8000L, Clips.totalMs(listOf(clip(10_000, 2_000, 7_000), clip(3_000)))) // 5000 + 3000
    }

    @Test
    fun aralikSinirlariDuzeltilir() { // video dışına taşan aralık
        val c = Clips.clampRange(clip(4_000), -500, 9_000) // başlangıç negatif, bitiş uzun
        assertEquals(0L, c.startMs); assertEquals(4_000L, c.endMs) // videonun tamamı
    }

    @Test
    fun cokKisaAralikYarimSaniyeyeUzar() { // 100 ms'lik aralık
        val c = Clips.clampRange(clip(4_000), 1_000, 1_100) // çok kısa
        assertEquals(1_000L, c.startMs); assertEquals(1_500L, c.endMs) // yarım saniyeye uzadı
        val sonda = Clips.clampRange(clip(4_000), 3_900, 4_000) // sona dayanmış kısa aralık
        assertEquals(3_500L, sonda.startMs); assertEquals(4_000L, sonda.endMs) // başlangıç geri alındı
    }

    @Test
    fun yukariTasima() { // sıra değiştirme
        val a = clip(1_000); val b = clip(2_000); val c = clip(3_000) // üç video
        assertEquals(listOf(a, c, b), Clips.moveUp(listOf(a, b, c), 2)) // 3. video 2. sıraya
        assertEquals(listOf(a, b, c), Clips.moveUp(listOf(a, b, c), 0)) // en üstteki değişmez
    }

    @Test
    fun sureBicimi() { // dakika:saniye
        assertEquals("0:05", Clips.format(4_600)) // yuvarlanır
        assertEquals("1:23", Clips.format(83_000)) // 1 dk 23 sn
    }
}
