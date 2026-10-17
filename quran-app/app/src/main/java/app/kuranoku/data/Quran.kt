package app.kuranoku.data // veri katmanı

import java.io.BufferedReader // satır satır okumak için
import java.io.InputStream // varlık dosyası akışı için

/** Tek bir ayet. Metin, Tanzil Uthmani metnidir; ilk ayetteki besmele ayrı başlık olarak çizildiği için çıkarılmıştır. */
data class Ayah(
    val sura: Int, // sure numarası (1-114)
    val number: Int, // sure içindeki ayet numarası
    val page: Int, // Medine mushafı sayfası (1-604)
    val juz: Int, // cüz (1-30)
    val hizbQuarter: Int, // hizb çeyreği (1-240)
    val sajda: Boolean, // secde ayeti mi
    val text: String, // Arapça metin
)

/** Sure bilgisi. */
data class Surah(
    val number: Int, // sure numarası
    val arabicName: String, // Arapça adı (ör. سُورَةُ المَائِدَةِ)
    val turkishName: String, // Diyanet yazımıyla Türkçe adı (ör. Mâide)
    val aliases: List<String>, // diğer adları (ör. Gâfir)
    val englishName: String, // Latin harfli uluslararası adı (ör. Al-Maaida)
    val ayahCount: Int, // ayet sayısı
    val meccan: Boolean, // Mekkî mi (değilse Medenî)
    val startPage: Int, // başladığı sayfa
)

/** Cüz bilgisi. */
data class Juz(
    val number: Int, // cüz numarası
    val startPage: Int, // başladığı sayfa
    val startSura: Int, // başladığı sure
    val startAyah: Int, // başladığı ayet
)

/** Sayfa üzerindeki görsel bloklar: sure başlığı, besmele ya da ayet paragrafı. */
sealed interface PageBlock {
    data class SurahHeader(val surah: Surah) : PageBlock // süslü sure başlığı
    data object Basmala : PageBlock // besmele satırı
    data class Paragraph(val ayahs: List<Ayah>) : PageBlock // aynı sureden art arda ayetler
}

/** Medine mushafındaki tek satır (KFGQPC baskısı, sayfa başına 15 satır). */
sealed interface MushafLine {
    val line: Int // satır numarası (1-15)
    data class Header(override val line: Int, val sura: Int) : MushafLine // sure başlığı
    data class Basmala(override val line: Int) : MushafLine // besmele
    /** Kelime satırı: ilk kelimenin suresi/ayeti ve kelimeler (ayet sonu işaretleri Arapça rakam olarak). */
    data class Words(override val line: Int, val sura: Int, val ayah: Int, val words: List<String>) : MushafLine
}

/** Kelime bir ayet sonu işareti mi (yalnızca Arapça rakamlardan oluşur; yazı tipi bunu ayet gülü olarak çizer). */
fun isAyahEnd(word: String): Boolean = word.isNotEmpty() && word.all { it in '٠'..'٩' }

/** Kur'an'ın tamamı ve hızlı erişim tabloları. */
class Quran(val surahs: List<Surah>, val ayahs: List<Ayah>, private val mushaf: Array<List<MushafLine>> = emptyArray()) {
    val pageCount: Int = ayahs.last().page // toplam sayfa (604)
    private val pageStart = IntArray(pageCount + 2) // her sayfanın ilk ayetinin dizideki yeri
    private val suraStart = IntArray(surahs.size + 2) // her surenin ilk ayetinin dizideki yeri
    val juzs: List<Juz> // 30 cüz

    init {
        var sonSayfa = 0 // en son görülen sayfa
        var sonSure = 0 // en son görülen sure
        ayahs.forEachIndexed { i, a -> // tüm ayetler dolaşılıyor
            while (sonSayfa < a.page) { sonSayfa++; pageStart[sonSayfa] = i } // yeni sayfa başlıyor
            if (a.sura != sonSure) { sonSure = a.sura; suraStart[sonSure] = i } // yeni sure başlıyor
        }
        pageStart[pageCount + 1] = ayahs.size // son sayfanın bitişi
        suraStart[surahs.size + 1] = ayahs.size // son surenin bitişi
        val liste = mutableListOf<Juz>() // cüz listesi
        ayahs.forEach { a -> if (a.juz > liste.size) liste += Juz(a.juz, a.page, a.sura, a.number) } // her cüzün ilk ayeti
        juzs = liste // atanıyor
    }

    /** Sayfadaki ayetler. */
    fun ayahsOnPage(page: Int): List<Ayah> = ayahs.subList(pageStart[page], pageStart[page + 1])

    /** Sayfanın suresi (başlıkta gösterilir): sayfada yeni bir sure başlıyorsa o, yoksa ilk ayetin suresi. */
    fun surahOfPage(page: Int): Surah {
        val baslayan = ayahsOnPage(page).firstOrNull { it.number == 1 } // sayfada başlayan ilk sure
        return surahs[(baslayan ?: ayahs[pageStart[page]]).sura - 1] // o sure ya da ilk ayetin suresi
    }

    /** Sayfanın cüzü. */
    fun juzOfPage(page: Int): Int = ayahs[pageStart[page]].juz

    /** Sure ve ayetin bulunduğu sayfa; ayet numarası taşarsa surenin son ayeti alınır. */
    fun pageOf(sura: Int, ayah: Int = 1): Int {
        val s = sura.coerceIn(1, surahs.size) // sure sınırda tutuluyor
        val a = ayah.coerceIn(1, surahs[s - 1].ayahCount) // ayet sınırda tutuluyor
        return ayahs[suraStart[s] + a - 1].page // sayfa döndürülüyor
    }

    /** Mushaf sayfasının satırları (boşsa akan yazı kullanılır). */
    fun mushafLines(page: Int): List<MushafLine> = mushaf.getOrNull(page) ?: emptyList()

    /** Sayfayı görsel bloklara ayırır. */
    fun blocksOf(page: Int): List<PageBlock> {
        val bloklar = mutableListOf<PageBlock>() // sonuç
        var paragraf = mutableListOf<Ayah>() // birikmekte olan paragraf
        for (a in ayahsOnPage(page)) { // sayfadaki ayetler
            if (a.number == 1) { // yeni sure başlıyor
                if (paragraf.isNotEmpty()) { bloklar += PageBlock.Paragraph(paragraf); paragraf = mutableListOf() } // önceki paragraf kapanıyor
                bloklar += PageBlock.SurahHeader(surahs[a.sura - 1]) // sure başlığı
                if (a.sura != 1 && a.sura != 9) bloklar += PageBlock.Basmala // besmele (Fâtiha'da ayetin kendisi, Tevbe'de yok)
            }
            paragraf += a // ayet paragrafa ekleniyor
        }
        if (paragraf.isNotEmpty()) bloklar += PageBlock.Paragraph(paragraf) // son paragraf
        return bloklar // bloklar döndürülüyor
    }

    companion object {
        /** assets/quran.tsv, assets/surahs.tsv ve (varsa) assets/mushaf.tsv dosyalarından okur. */
        fun parse(surahsTsv: InputStream, quranTsv: InputStream, mushafTsv: InputStream? = null): Quran {
            val sureler = surahsTsv.bufferedReader(Charsets.UTF_8).useLines { satirlar -> // sure satırları
                satirlar.filter { it.isNotBlank() }.map { satir -> // boş satırlar atlanıyor
                    val p = satir.split('\t') // sütunlar
                    Surah(
                        number = p[0].toInt(), // numara
                        arabicName = p[1], // Arapça ad
                        turkishName = p[2], // Türkçe ad
                        aliases = p[3].split(',').map { it.trim() }.filter { it.isNotEmpty() }, // diğer adlar
                        englishName = p[4], // Latin ad
                        ayahCount = p[5].toInt(), // ayet sayısı
                        meccan = p[6] == "M", // Mekkî mi
                        startPage = p[7].toInt(), // ilk sayfa
                    )
                }.toList() // listeye çevriliyor
            }
            val ayetler = ArrayList<Ayah>(6236) // ayet listesi
            BufferedReader(quranTsv.reader(Charsets.UTF_8), 1 shl 16).useLines { satirlar -> // ayet satırları
                satirlar.forEach { satir -> // her satır
                    if (satir.isBlank()) return@forEach // boş satır atlanıyor
                    val p = satir.split('\t', limit = 7) // 7 sütun
                    ayetler += Ayah(p[0].toInt(), p[1].toInt(), p[2].toInt(), p[3].toInt(), p[4].toInt(), p[5] == "1", p[6]) // ayet ekleniyor
                }
            }
            require(sureler.size == 114 && ayetler.size == 6236) { "Kur'an verisi eksik" } // veri bütünlüğü kontrolü
            val sayfalar = Array<MutableList<MushafLine>>(605) { mutableListOf() } // 1-604 (0 boş)
            mushafTsv?.let { akis -> // mushaf düzeni
                BufferedReader(akis.reader(Charsets.UTF_8), 1 shl 16).useLines { satirlar ->
                    satirlar.forEach { satir -> // sayfa satır tür veri
                        if (satir.isBlank()) return@forEach // boş satır
                        val p = satir.split('\t') // sütunlar
                        val sayfa = p[0].toInt() // sayfa
                        val no = p[1].toInt() // satır
                        sayfalar[sayfa] += when (p[2]) {
                            "H" -> MushafLine.Header(no, p[3].toInt()) // sure başlığı
                            "B" -> MushafLine.Basmala(no) // besmele
                            else -> { val (s, a) = p[3].split(':').map { it.toInt() }; MushafLine.Words(no, s, a, p[4].split(' ')) } // kelimeler
                        }
                    }
                }
                require(sayfalar.drop(1).all { it.isNotEmpty() }) { "Mushaf düzeni eksik" } // her sayfa dolu olmalı
            }
            return Quran(sureler, ayetler, if (mushafTsv != null) sayfalar.map { it.toList() }.toTypedArray() else emptyArray()) // nesne döndürülüyor
        }
    }
}
