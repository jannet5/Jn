package app.kuranoku.data // veri katmanı

import java.util.Locale // Türkçe küçük harf dönüşümü için

/** Arama önerisi türleri. */
enum class SuggestionKind { SURAH, JUZ, PAGE, AYAH }

/** Arama kutusunun altında listelenen tek öneri. */
data class Suggestion(
    val kind: SuggestionKind, // türü
    val title: String, // ana başlık (ör. "Mâide")
    val subtitle: String, // açıklama (ör. "5. sure · 120 ayet · sayfa 106")
    val page: Int, // gidilecek sayfa
    val sura: Int = 0, // vurgulanacak sure (yoksa 0)
    val ayah: Int = 0, // vurgulanacak ayet (yoksa 0)
    val arabic: String = "", // sağda gösterilecek Arapça ad
    val badge: String = "", // soldaki rozet metni (sure no, cüz no…)
)

/** Sure adı, cüz, sayfa ve sure:ayet aramasını yapan, Android'den bağımsız (test edilebilir) arama motoru. */
class SearchEngine(private val quran: Quran) {

    private class Key(val surah: Surah, val keys: List<String>, val words: List<String>) // bir surenin arama anahtarları

    private val index: List<Key> = quran.surahs.map { s -> // her sure için anahtarlar hazırlanıyor
        val adlar = listOf(s.turkishName, s.englishName) + s.aliases // tüm adlar
        val anahtarlar = linkedSetOf<String>() // tekrar etmeyen anahtarlar
        val kelimeler = linkedSetOf<String>() // ad içindeki tek tek kelimeler
        for (ad in adlar) { // her ad
            anahtarlar += normalize(ad) // tam hali
            anahtarlar += normalize(stripArticle(ad)) // "el-/al-" eki atılmış hali
            ad.split(' ', '-').filter { it.length >= 2 }.forEach { kelimeler += normalize(it) } // kelimeleri
        }
        anahtarlar += normalizeArabic(s.arabicName).removePrefix("سوره") // Arapça adı ("sûre" kelimesi atılmış)
        Key(s, anahtarlar.filter { it.isNotEmpty() }, kelimeler.filter { it.length >= 2 }) // anahtar nesnesi
    }

    /** Sorguya göre en fazla [limit] öneri döndürür. */
    fun search(raw: String, limit: Int = 12): List<Suggestion> {
        val q = toLatinDigits(raw).trim().lowercase(Locale.forLanguageTag("tr")) // rakamlar Latin'e, harfler küçüğe
        if (q.isEmpty()) return emptyList() // boş sorgu
        val sonuc = mutableListOf<Suggestion>() // sonuçlar

        // 1) "2:255", "2/255", "2.255", "2 255" → sure ve ayet
        Regex("""^(\d{1,3})\s*[:/.,\s-]\s*(\d{1,3})$""").find(q)?.let { m ->
            val s = m.groupValues[1].toInt() // sure no
            val a = m.groupValues[2].toInt() // ayet no
            if (s in 1..114) { // geçerli sure
                val sure = quran.surahs[s - 1] // sure
                if (a in 1..sure.ayahCount) return listOf(ayahSuggestion(sure, a)) // geçerli ayet
            }
        }

        // 2) "cüz 20", "20. cüz", "juz 20", "c 20" → cüz
        Regex("""^(?:c[uü]z|juz|ciz|c)\s*\.?\s*(\d{1,2})$|^(\d{1,2})\s*\.?\s*(?:c[uü]z|juz|c)$""").find(q)?.let { m ->
            val n = (m.groupValues[1].ifEmpty { m.groupValues[2] }).toInt() // cüz no
            if (n in 1..30) return listOf(juzSuggestion(n)) // tek sonuç
        }

        // 3) "sayfa 302", "s 302", "sf302", "302. sayfa", "p 302" → sayfa
        Regex("""^(?:sayfa|sayf|say|sf|s|p|page)\s*\.?\s*(\d{1,3})$|^(\d{1,3})\s*\.?\s*(?:sayfa|sf|s)$""").find(q)?.let { m ->
            val n = (m.groupValues[1].ifEmpty { m.groupValues[2] }).toInt() // sayfa no
            if (n in 1..quran.pageCount) return listOf(pageSuggestion(n)) // tek sonuç
        }

        // 4) Sadece sayı: önce cüz, sonra sayfa, sonra sure numarası
        Regex("""^(\d{1,3})\.?$""").find(q)?.let { m ->
            val n = m.groupValues[1].toInt() // sayı
            if (n in 1..30) sonuc += juzSuggestion(n) // cüz
            if (n in 1..quran.pageCount) sonuc += pageSuggestion(n) // sayfa
            if (n in 1..114) sonuc += surahSuggestion(quran.surahs[n - 1]) // sure
            return sonuc // sonuçlar
        }

        // 5) "bakara 255" → sure adı + ayet
        val adAyet = Regex("""^(.*\D)\s*(\d{1,3})$""").find(q) // sonda sayı var mı
        val metin = adAyet?.groupValues?.get(1)?.trim() ?: q // ad kısmı
        val ayetNo = adAyet?.groupValues?.get(2)?.toIntOrNull() // ayet kısmı
        val sureler = matchSurahs(metin) // adı eşleşen sureler
        if (ayetNo != null) { // ayet de yazılmış
            sureler.take(3).forEach { s -> if (ayetNo in 1..s.ayahCount) sonuc += ayahSuggestion(s, ayetNo) } // ilk 3 sure için ayet önerisi
        }
        sureler.forEach { sonuc += surahSuggestion(it) } // sure önerileri
        return sonuc.distinctBy { Triple(it.kind, it.page, it.title) }.take(limit) // tekrarlar atılıp kırpılıyor
    }

    /** Ada göre sureleri puanlayıp sıralar. */
    fun matchSurahs(text: String): List<Surah> {
        val arapca = text.any { it in '؀'..'ۿ' } // Arapça harf var mı
        val q = if (arapca) normalizeArabic(text).removePrefix("سوره") else normalize(stripArticle(text)) // sorgu normalleştiriliyor
        if (q.isEmpty()) return emptyList() // boşsa sonuç yok
        return index.mapNotNull { k -> score(k, q)?.let { k.surah to it } } // her sure puanlanıyor
            .sortedWith(compareByDescending<Pair<Surah, Int>> { it.second }.thenBy { it.first.number }) // puana, sonra numaraya göre
            .map { it.first } // sadece sureler
    }

    private fun score(k: Key, q: String): Int? { // bir surenin sorguya uygunluk puanı (null = eşleşmedi)
        var en = -1 // en iyi puan
        for (key in k.keys) { // her anahtar
            val p = when {
                key == q -> 1000 // birebir aynı
                key.startsWith(q) -> 900 // başı tutuyor (eşitlikte sure sırası: Mâide, Mâûn'dan önce)
                else -> -1 // tutmuyor
            }
            if (p > en) en = p // en iyisi saklanıyor
        }
        if (en < 0) for (w in k.words) if (w.startsWith(q)) en = maxOf(en, 800) // içindeki bir kelimenin başı tutuyor (ör. "imran")
        if (en < 0 && q.length >= 3) for (key in k.keys) if (key.contains(q)) en = maxOf(en, 600 - key.indexOf(q)) // ortasında geçiyor
        if (en < 0 && q.length >= 3) { // yazım hatası toleransı
            val izin = if (q.length <= 5) 1 else 2 // kısa sorguda 1, uzunda 2 harf hata
            for (key in k.keys) { // her anahtar
                val tam = levenshtein(q, key) // tamamına uzaklık
                val bas = if (key.length > q.length) levenshtein(q, key.substring(0, q.length)) else tam // başına uzaklık
                val d = minOf(tam, bas) // en yakın
                if (d <= izin) en = maxOf(en, 400 - d * 50) // hata payı içinde
            }
        }
        return if (en >= 0) en else null // puan
    }

    private fun surahSuggestion(s: Surah) = Suggestion( // sure önerisi
        SuggestionKind.SURAH, s.turkishName, "${s.number}. sure · ${s.ayahCount} ayet · sayfa ${s.startPage}", s.startPage, s.number, 0, s.arabicName, s.number.toString(),
    )

    private fun ayahSuggestion(s: Surah, a: Int) = Suggestion( // ayet önerisi
        SuggestionKind.AYAH, "${s.turkishName} ${a}. ayet", "${s.number}:$a · sayfa ${quran.pageOf(s.number, a)}", quran.pageOf(s.number, a), s.number, a, s.arabicName, "${s.number}:$a",
    )

    private fun juzSuggestion(n: Int): Suggestion { // cüz önerisi
        val j = quran.juzs[n - 1] // cüz
        val s = quran.surahs[j.startSura - 1] // başladığı sure
        return Suggestion(SuggestionKind.JUZ, "$n. Cüz", "${s.turkishName} ${j.startAyah}. ayetten başlar · sayfa ${j.startPage}", j.startPage, j.startSura, j.startAyah, "", "C$n")
    }

    private fun pageSuggestion(n: Int): Suggestion { // sayfa önerisi
        val s = quran.surahOfPage(n) // sayfanın suresi
        return Suggestion(SuggestionKind.PAGE, "Sayfa $n", "${s.turkishName} · ${quran.juzOfPage(n)}. cüz", n, 0, 0, "", "S$n")
    }

    companion object {
        private val ARTICLE = Regex("""^(?:a[ln]|e[ln]|[ae][sşztdrn]|ash|esh|ad|ed)[-\s'’]+""", RegexOption.IGNORE_CASE) // "el-", "al-", "en-", "es-"… ön ekleri

        /** "Al-Maaida" → "Maaida", "el Mâide" → "Mâide". */
        fun stripArticle(s: String): String = s.trim().replace(ARTICLE, "")

        /** Latin harfleri aramaya uygun hale getirir: aksan, Türkçe harf, boşluk ve kesme işaretleri atılır, çift ünlü teke iner. */
        fun normalize(s: String): String {
            val sb = StringBuilder(s.length) // sonuç
            for (c in s.lowercase(Locale.forLanguageTag("tr"))) { // Türkçe kurallarla küçük harf
                val m = when (c) {
                    'â', 'á', 'à', 'ä' -> 'a' // a
                    'î', 'í', 'ı', 'ï' -> 'i' // i
                    'û', 'ú', 'ü' -> 'u' // u
                    'ö', 'ô', 'ó' -> 'o' // o
                    'ş' -> 's' // s
                    'ç' -> 'c' // c
                    'ğ' -> 'g' // g
                    'é', 'ê' -> 'e' // e
                    else -> c // diğerleri aynen
                }
                if (m.isLetterOrDigit()) sb.append(m) // boşluk, tire, kesme işareti atılıyor
            }
            return sb.toString()
                .replace(Regex("([aeiou])\\1+"), "$1") // "maaida" → "maida"
                .replace("q", "k").replace("w", "v").replace("x", "ks") // İngilizce yazımlar Türkçeye
        }

        /** Arapça metinden harekeleri, tatvili ve harf varyantlarını atar. */
        fun normalizeArabic(s: String): String {
            val sb = StringBuilder(s.length) // sonuç
            for (c in s) { // her karakter
                when {
                    c in 'ً'..'ٟ' || c == 'ٰ' || c in 'ۖ'..'ۭ' || c == 'ـ' -> Unit // hareke ve işaretler atlanıyor
                    c == 'ٱ' || c == 'أ' || c == 'إ' || c == 'آ' -> sb.append('ا') // elif varyantları
                    c == 'ة' -> sb.append('ه') // te merbuta
                    c == 'ى' -> sb.append('ي') // elif maksura
                    c == ' ' -> Unit // boşluk atılıyor
                    else -> sb.append(c) // diğerleri aynen
                }
            }
            return sb.toString() // sonuç
        }

        /** Arapça-Hint ve Farsça rakamları Latin rakama çevirir. */
        fun toLatinDigits(s: String): String = buildString(s.length) {
            for (c in s) append(
                when (c) {
                    in '٠'..'٩' -> '0' + (c - '٠') // Arapça rakam
                    in '۰'..'۹' -> '0' + (c - '۰') // Farsça rakam
                    else -> c // diğerleri
                },
            )
        }

        /** İki metin arasındaki düzenleme uzaklığı (yazım hatası toleransı için). */
        fun levenshtein(a: String, b: String): Int {
            var onceki = IntArray(b.length + 1) { it } // önceki satır
            var simdiki = IntArray(b.length + 1) // şimdiki satır
            for (i in 1..a.length) { // a'nın her harfi
                simdiki[0] = i // ilk sütun
                for (j in 1..b.length) { // b'nin her harfi
                    val maliyet = if (a[i - 1] == b[j - 1]) 0 else 1 // aynıysa 0
                    simdiki[j] = minOf(simdiki[j - 1] + 1, onceki[j] + 1, onceki[j - 1] + maliyet) // ekleme/silme/değiştirme
                }
                val t = onceki; onceki = simdiki; simdiki = t // satırlar yer değiştiriyor
            }
            return onceki[b.length] // sonuç
        }
    }
}

/** Sayıyı Arapça-Hint rakamlarıyla yazar (ayet ve sayfa süsleri için). */
fun Int.toArabicDigits(): String = toString().map { '٠' + (it - '0') }.joinToString("")
