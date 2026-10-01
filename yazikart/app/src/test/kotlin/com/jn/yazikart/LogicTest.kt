package com.jn.yazikart // test paketi

import com.jn.yazikart.data.ExportFormat // kayıt biçimi
import com.jn.yazikart.data.FontCatalog // yazı tipleri
import com.jn.yazikart.data.ImageExporter // dosya adı
import com.jn.yazikart.render.PostRenderer // kırpma hesabı
import com.jn.yazikart.ui.parseHex // HEX çözücü
import com.jn.yazikart.ui.toHex // HEX üretici
import org.junit.Assert.assertEquals // eşitlik
import org.junit.Assert.assertNull // boşluk
import org.junit.Assert.assertTrue // doğruluk
import org.junit.Test // test işareti
import java.io.File // dosya kontrolü
import java.util.Calendar // sabit tarih
import java.util.TimeZone // saat dilimi

// Hesap ve katalog testleri
class LogicTest {

    @Test
    fun elliYaziTipiVeDosyalariVar() { // 50 yazı tipi ve hepsinin dosyası
        assertEquals(50, FontCatalog.fonts.size) // tam 50 tane
        assertEquals(50, FontCatalog.fonts.map { it.name }.toSet().size) // adlar tekrarsız
        val assets = File("src/main/assets") // assets klasörü (test modül kökünden)
        FontCatalog.fonts.forEach { f -> // her yazı tipi
            assertTrue(f.regularPath, File(assets, f.regularPath).isFile) // normal dosya var
            f.boldPath?.let { assertTrue(it, File(assets, it).isFile) } // kalın dosya (varsa) var
        }
    }

    @Test
    fun kirpmaOrtadanYapiliyor() { // centerCrop hesabı
        val wide = PostRenderer.centerCropRect(2000, 1000, 1080, 1080) // geniş resim, kare hedef
        assertEquals(500, wide.left); assertEquals(1500, wide.right) // yanlardan 500'er kırpıldı
        assertEquals(0, wide.top); assertEquals(1000, wide.bottom) // yükseklik tam
        val tall = PostRenderer.centerCropRect(1000, 3000, 1080, 1350) // uzun resim, 4:5 hedef
        assertEquals(0, tall.left); assertEquals(1000, tall.right) // genişlik tam
        assertEquals(1250, tall.bottom - tall.top) // 1000 * 1350/1080 = 1250 yükseklik
        assertEquals(875, tall.top) // üstten ve alttan eşit
    }

    @Test
    fun hexCeviri() { // renk kodları
        assertEquals("#FF5A36", toHex(0xFFFF5A36.toInt())) // sayıdan koda
        assertEquals(0xFF000000.toInt(), parseHex("#000000")) // siyah
        assertEquals(0xFFFF5A36.toInt(), parseHex("ff5a36")) // # olmadan, küçük harf
        assertNull(parseHex("#12345")) // eksik hane
        assertNull(parseHex("#GGGGGG")) // geçersiz harf
    }

    @Test
    fun dosyaAdi() { // dosya adı biçimi
        val cal = Calendar.getInstance(TimeZone.getDefault()).apply { set(2026, 9, 1, 14, 30, 5) } // 1 Ekim 2026 14:30:05
        assertEquals("YaziKart_20261001_143005.jpg", ImageExporter.fileName(ExportFormat.JPEG, cal.time)) // JPEG adı
        assertEquals("YaziKart_20261001_143005.png", ImageExporter.fileName(ExportFormat.PNG, cal.time)) // PNG adı
    }
}
