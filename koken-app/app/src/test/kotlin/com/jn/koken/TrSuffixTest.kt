package com.jn.koken

import com.jn.koken.data.TrSuffix
import org.junit.Assert.assertEquals
import org.junit.Test

/** Dil adlarına Türkçe hâl ekleri: ünlü uyumu, ünsüz benzeşmesi, iyelik ekli adlar, kesme işareti */
class TrSuffixTest {
    @Test fun arapca() {
        assertEquals("Arapçadan", TrSuffix.ablative("Arapça"))
        assertEquals("Arapçaya", TrSuffix.dative("Arapça"))
        assertEquals("Arapçada", TrSuffix.locative("Arapça"))
    }
    @Test fun latince_on_unlu() {
        assertEquals("Latinceden", TrSuffix.ablative("Latince"))
        assertEquals("Latinceye", TrSuffix.dative("Latince"))
        assertEquals("Latincede", TrSuffix.locative("Latince"))
        assertEquals("İngilizceden", TrSuffix.ablative("İngilizce"))
        assertEquals("Türkçeye", TrSuffix.dative("Türkçe"))
    }
    @Test fun iki_kelimeli_adlar_son_unluye_gore() {
        assertEquals("Eski Fransızcadan", TrSuffix.ablative("Eski Fransızca"))
        assertEquals("Orta İngilizceye", TrSuffix.dative("Orta İngilizce"))
        assertEquals("Eski Yunancada", TrSuffix.locative("Eski Yunanca"))
    }
    @Test fun iyelik_ekli_adlar() {
        assertEquals("Osmanlı Türkçesinden", TrSuffix.ablative("Osmanlı Türkçesi"))
        assertEquals("Osmanlı Türkçesine", TrSuffix.dative("Osmanlı Türkçesi"))
        assertEquals("Osmanlı Türkçesinde", TrSuffix.locative("Osmanlı Türkçesi"))
        assertEquals("Bizans Yunancasına", TrSuffix.dative("Bizans Yunancası"))
        assertEquals("Proto-Hint-Avrupa dilinden", TrSuffix.ablative("Proto-Hint-Avrupa dili"))
        assertEquals("Proto-Hint-Avrupa dilinde", TrSuffix.locative("Proto-Hint-Avrupa dili"))
    }
    @Test fun yabanci_ozel_ad_kesmeli_ve_sert_unsuz() {
        assertEquals("Anglo-Norman'a", TrSuffix.dative("Anglo-Norman"))
        assertEquals("Anglo-Norman'da", TrSuffix.locative("Anglo-Norman"))
        assertEquals("Anglo-Norman'dan", TrSuffix.ablative("Anglo-Norman"))
        assertEquals("Proto-İtalik dile", TrSuffix.dative("Proto-İtalik dil"))
        assertEquals("Proto-İtalik dilde", TrSuffix.locative("Proto-İtalik dil"))
    }
}
