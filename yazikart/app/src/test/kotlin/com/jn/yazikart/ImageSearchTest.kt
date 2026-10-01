package com.jn.yazikart // test paketi

import com.jn.yazikart.search.ImageResult // arama sonucu
import com.jn.yazikart.search.ImageSearch // arayıcı
import org.junit.Assert.assertEquals // eşitlik kontrolü
import org.junit.Assert.assertTrue // doğruluk kontrolü
import org.junit.Test // test işareti

// Arama cevaplarının doğru çözüldüğünü kontrol eden testler (gerçek API cevap biçimiyle)
class ImageSearchTest {

    @Test
    fun openverseCevabiCozuluyor() { // Openverse cevabı
        val json = """{"result_count":2,"results":[
            {"id":"a","title":"Lahmacun","url":"https://live.staticflickr.com/x_b.jpg","thumbnail":"https://api.openverse.org/v1/images/a/thumb/"},
            {"id":"b","title":"Boş","url":"","thumbnail":"t"}]}""" // biri geçerli, biri adressiz
        val r = ImageSearch.parseOpenverse(json) // çözülüyor
        assertEquals(1, r.size) // adressiz olan atlandı
        assertEquals("https://live.staticflickr.com/x_b.jpg", r[0].fullUrl) // büyük resim
        assertEquals("https://api.openverse.org/v1/images/a/thumb/", r[0].thumbUrl) // küçük resim
        assertEquals("Openverse", r[0].source) // kaynak
    }

    @Test
    fun wikimediaCevabiSiraliVeFiltreli() { // Wikimedia cevabı
        val json = """{"query":{"pages":[
            {"title":"File:Ikinci.png","index":2,"imageinfo":[{"mime":"image/png","url":"https://u/o2.png","thumburl":"https://u/thumb/330px-o2.png","width":800}]},
            {"title":"File:Birinci.jpg","index":1,"imageinfo":[{"mime":"image/jpeg","url":"https://u/o1.jpg","thumburl":"https://u/thumb/o1.jpg/330px-o1.jpg","width":4000}]},
            {"title":"File:Video.webm","index":3,"imageinfo":[{"mime":"video/webm","url":"https://u/v.webm","thumburl":"https://u/v.jpg","width":1920}]}
        ]}}""" // sırası karışık, biri video
        val r = ImageSearch.parseWikimedia(json) // çözülüyor
        assertEquals(listOf("Birinci", "Ikinci"), r.map { it.title }) // arama sırasına dizildi, video atıldı
        assertEquals("https://u/thumb/o1.jpg/1280px-o1.jpg", r[0].fullUrl) // çok büyük olan 1280 piksel indiriliyor
        assertEquals("https://u/o2.png", r[1].fullUrl) // küçük olan orijinal
    }

    @Test
    fun bosVeyaHataliCevapBosListe() { // beklenmeyen cevap
        assertTrue(ImageSearch.parseOpenverse("{}").isEmpty()) // sonuç yok
        assertTrue(ImageSearch.parseWikimedia("{\"batchcomplete\":true}").isEmpty()) // query yok
    }

    @Test
    fun ikiKaynakSirayla() { // karıştırma
        fun r(u: String) = ImageResult(u, u, u, "x") // kısa sonuç üretici
        val out = ImageSearch.interleave(listOf(r("a1"), r("a2"), r("a3")), listOf(r("b1"), r("a1"))) // a1 iki listede de var
        assertEquals(listOf("a1", "b1", "a2", "a3"), out.map { it.fullUrl }) // sırayla ve tekrarsız
    }
}
