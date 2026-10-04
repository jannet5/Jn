package app.kuranoku.data // veri testleri

import org.junit.Assert.assertEquals // eşitlik
import org.junit.Assert.assertTrue // doğruluk
import org.junit.BeforeClass // sınıf başında bir kez
import org.junit.Test // test
import java.io.File // dosya

/** Gerçek varlık dosyalarıyla Kur'an verisi ve arama testleri. */
class SearchTest {
    companion object {
        lateinit var q: Quran // Kur'an
        lateinit var e: SearchEngine // arama
        @JvmStatic @BeforeClass fun setup() { // dosyalar bir kez okunur
            val d = File("src/main/assets") // varlık klasörü
            q = File(d, "surahs.tsv").inputStream().use { s -> File(d, "quran.tsv").inputStream().use { k -> Quran.parse(s, k) } } // ayrıştırma
            e = SearchEngine(q) // motor
        }
    }

    @Test fun veriTamam() { // sayılar doğru mu
        assertEquals(604, q.pageCount) // 604 sayfa
        assertEquals(30, q.juzs.size) // 30 cüz
        assertEquals(106, q.surahs[4].startPage) // Mâide 106'da başlar
        assertEquals(582, q.juzs[29].startPage) // 30. cüz 582'de başlar
        assertEquals(42, q.pageOf(2, 255)) // Âyetü'l-Kürsî 42. sayfa
        assertEquals(1, q.pageOf(1)) // Fâtiha 1. sayfa
    }

    @Test fun besmeleBasliktaAyriliyor() { // besmele ayeti içinde tekrar etmesin
        val b = q.blocksOf(106) // Mâide'nin başladığı sayfa
        assertTrue(b.any { it is PageBlock.SurahHeader && it.surah.number == 5 }) // başlık var
        assertTrue(b.contains(PageBlock.Basmala)) // besmele var
        val ilk = q.ayahs.first { it.sura == 5 && it.number == 1 } // Mâide 1
        assertTrue(ilk.text.startsWith("ي")) // besmelesiz başlıyor ("yâ eyyühâ")
        assertTrue(q.blocksOf(187).none { it == PageBlock.Basmala }) // Tevbe'de besmele yok
    }

    @Test fun maYazincaMaide() { // kullanıcı örneği: "ma"
        val r = e.search("ma").map { it.title } // öneriler
        assertEquals("Mâide", r.first()) // ilk öneri Mâide
        assertTrue("Mâûn" in r) // Mâûn da çıkar
    }

    @Test fun maideYazimlari() { // farklı yazımlar
        for (s in listOf("Maide", "maide", "Mâide", "el maide", "al-maaida", "MAİDE", "maida", "maidr", "المائدة")) // yazımlar
            assertEquals(s, "Mâide", e.search(s).first().title) // hepsi Mâide
    }

    @Test fun cuzVeSayfa() { // sayı aramaları
        val r = e.search("20") // sadece 20
        assertEquals(SuggestionKind.JUZ, r[0].kind) // önce cüz
        assertEquals(q.juzs[19].startPage, r[0].page) // 20. cüzün sayfası
        assertEquals(SuggestionKind.PAGE, r[1].kind) // sonra sayfa
        assertEquals(SuggestionKind.SURAH, r[2].kind) // sonra sure (Tâhâ)
        assertEquals(q.juzs[19].startPage, e.search("20. cüz").single().page) // "20. cüz"
        assertEquals(q.juzs[19].startPage, e.search("cuz 20").single().page) // "cuz 20"
        assertEquals(302, e.search("s 302").single().page) // sayfa
        assertEquals(302, e.search("sayfa 302").single().page) // sayfa
        assertEquals(500, e.search("500").single().page) // 500 sadece sayfa olabilir
        assertEquals(20, e.search("٢٠").first().title.removeSuffix(". Cüz").toInt()) // Arapça rakam
    }

    @Test fun ayetArama() { // sure:ayet
        val r = e.search("2:255").single() // Âyetü'l-Kürsî
        assertEquals(42, r.page) // sayfa 42
        assertEquals(255, r.ayah) // ayet
        assertEquals(42, e.search("bakara 255").first().page) // adla
        assertTrue(e.search("2:999").isEmpty() || e.search("2:999").none { it.kind == SuggestionKind.AYAH }) // geçersiz ayet
    }

    @Test fun digerAdlar() { // takma adlar
        assertEquals("Yâsîn", e.search("yasin").first().title) // Yâsîn
        assertEquals("Mü'min", e.search("gafir").first().title) // Gâfir
        assertEquals("Âl-i İmrân", e.search("imran").first().title) // kelime başı
        assertEquals("İhlâs", e.search("ihlas").first().title) // İhlâs
        assertTrue(e.search("xqzw").isEmpty()) // anlamsız sorgu
    }
}
