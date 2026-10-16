package com.jn.yazikart // test paketi

import com.jn.yazikart.search.GooglePick // en iyi görsel adresi seçimi
import org.junit.Assert.assertEquals // eşitlik
import org.junit.Assert.assertNull // boşluk
import org.junit.Test // test işareti

// Google Görseller'de basılı tutulan görselden doğru adresin seçildiğini kontrol eden testler
class GooglePickTest {

    @Test
    fun aramaAdresiTurkceKarakterleKodlanir() { // Türkçe kelime adrese doğru kodlanmalı
        assertEquals("https://www.google.com/search?tbm=isch&hl=tr&q=%C3%A7i%C4%9F+k%C3%B6fte", GooglePick.searchUrl(" çiğ köfte ")) // boşluk +, ç ğ ö kodlu
    }

    @Test
    fun baglantidakiOrijinalAdresOncelikli() { // imgres bağlantısı varsa orijinal görsel seçilir
        val href = "https://www.google.com/imgres?imgurl=https%3A%2F%2Fornek.com%2Flahmacun.jpg&imgrefurl=https%3A%2F%2Fornek.com"
        assertEquals("https://ornek.com/lahmacun.jpg", GooglePick.best(href, "https://encrypted-tbn0.gstatic.com/images?q=x", null)) // orijinal
    }

    @Test
    fun baglantiYoksaResimAdresi() { // bağlantı yoksa resmin kendi adresi
        assertEquals("https://site.com/b.png", GooglePick.best(null, null, "https://site.com/b.png")) // http adres
    }

    @Test
    fun httpVarsaGomuluResmeTercihEdilir() { // http adres data: önizlemeden iyidir
        assertEquals("https://site.com/c.jpg", GooglePick.best(null, "data:image/jpeg;base64,AAAA", "https://site.com/c.jpg")) // http seçildi
    }

    @Test
    fun sadeceGomuluResimVarsaO() { // tek seçenek gömülü küçük resimse
        assertEquals("data:image/jpeg;base64,AAAA", GooglePick.best(null, null, "data:image/jpeg;base64,AAAA")) // data: seçildi
    }

    @Test
    fun resimYoksaNull() { // görsel değilse
        assertNull(GooglePick.best("https://www.google.com/search?q=x", null, null)) // imgurl yok, resim yok
        assertNull(GooglePick.imgUrlParam("https://x.com/?imgurl=javascript%3Aalert(1)")) // internet adresi olmayan reddedilir
    }
}
