package com.jn.yazikart // test paketi

import com.jn.yazikart.search.VideoPick // eklenecek videoyu seçme
import com.jn.yazikart.search.VideoPick.Candidate // sayfadaki video
import com.jn.yazikart.search.VideoSite // video siteleri
import org.junit.Assert.assertEquals // eşitlik
import org.junit.Assert.assertFalse // yanlışlık
import org.junit.Assert.assertNull // boşluk
import org.junit.Assert.assertTrue // doğruluk
import org.junit.Test // test işareti

// Sitedeki oynayan videonun doğru seçildiğini kontrol eden testler
class VideoPickTest {

    @Test
    fun oynayanVideoOncelikli() { // küçük ama oynayan video, büyük duran videodan önce
        val list = listOf(Candidate("https://a.com/buyuk.mp4", false, 900f), Candidate("https://a.com/oynayan.mp4", true, 100f))
        assertEquals("https://a.com/oynayan.mp4", VideoPick.choose(list, null)) // oynayan seçildi
    }

    @Test
    fun oynayanYoksaEkrandakiEnBuyuk() { // hiçbiri oynamıyorsa ekranda en çok yer kaplayan
        val list = listOf(Candidate("https://a.com/kucuk.mp4", false, 50f), Candidate("https://a.com/buyuk.mp4", false, 500f), Candidate("https://a.com/gizli.mp4", false, 0f))
        assertEquals("https://a.com/buyuk.mp4", VideoPick.choose(list, null)) // en büyük görünen
    }

    @Test
    fun korumaliVideoAlinmaz() { // blob: (YouTube gibi) adresler seçilmez
        val list = listOf(Candidate("blob:https://youtube.com/123", true, 900f))
        assertNull(VideoPick.choose(list, null)) // seçilecek video yok
        assertTrue(VideoPick.onlyProtected(list)) // kullanıcıya "korumalı" denir
        assertEquals("https://cdn.site/x.mp4", VideoPick.choose(list, "https://cdn.site/x.mp4")) // sayfa ayrıca mp4 yüklediyse o alınır
    }

    @Test
    fun videoDosyasiTanima() { // arka planda yüklenen dosyaların video olup olmadığı
        assertTrue(VideoPick.looksLikeVideoFile("https://videos.pexels.com/video-files/1/elma_640.mp4?token=abc")) // parametreli mp4
        assertTrue(VideoPick.looksLikeVideoFile("https://cdn.coverr.co/videos/x/360p.MP4")) // büyük harf
        assertFalse(VideoPick.looksLikeVideoFile("https://images.pexels.com/photos/1/elma.jpeg")) // resim
        assertFalse(VideoPick.onlyProtected(emptyList())) // video yoksa "korumalı" denmez
    }

    @Test
    fun siteAramaAdresleri() { // her sitenin arama adresi
        assertEquals("https://www.pexels.com/tr-tr/arama/videos/k%C4%B1rm%C4%B1z%C4%B1%20elma/", VideoSite.PEXELS.searchUrl("kırmızı elma")) // Türkçe karakter + boşluk
        assertEquals("https://coverr.co/s?q=apple", VideoSite.COVERR.searchUrl(" apple ")) // boşluklar kırpılır
        assertEquals("https://mixkit.co/free-stock-video/red-apple/", VideoSite.MIXKIT.searchUrl("red apple")) // Mixkit konu sayfası tireli
    }
}
