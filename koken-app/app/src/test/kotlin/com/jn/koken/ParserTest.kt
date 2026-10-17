package com.jn.koken

import com.jn.koken.data.Source
import com.jn.koken.data.WikipediaParser
import com.jn.koken.data.WiktionaryParser
import com.jn.koken.data.Relation
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** Gerçek Vikisözlük/Vikipedi sayfaları (test verisi olarak kaydedildi) üzerinde ayrıştırıcı testleri */
class ParserTest {
    private fun fx(name: String): String =
        javaClass.getResource("/fixtures/$name")!!.readText()

    private fun wikt(name: String, src: Source, title: String) =
        WiktionaryParser.parse(fx(name), src, title, "https://x/$title")

    @Test
    fun ahmet_tr_vikisozluk_zincir_arapca() {
        val r = wikt("tr_wikt_Ahmet.html", Source.WIKT_TR, "Ahmet")!!
        val tr = r.languages.first { it.language == "Türkçe" }
        val ety = tr.groups.first { it.etymology != null }.etymology!!
        assertEquals(listOf("Osmanlı Türkçesi", "Arapça"), ety.chain.map { it.langName })
        assertEquals("احمد", ety.chain[0].text)
        assertEquals("Arapça", ety.originLanguage)
        assertTrue(ety.text.contains("Arapça"))
        assertTrue(tr.groups.flatMap { it.entries }.any { it.pos == "Özel ad" && it.definitions.any { d -> d.contains("erkek adı") } })
    }

    @Test
    fun kitap_tr_vikisozluk_devralindi_ve_anlam() {
        val r = wikt("tr_wikt_kitap.html", Source.WIKT_TR, "kitap")!!
        val tr = r.languages.first { it.language == "Türkçe" }
        val ety = tr.groups.first().etymology!!
        assertEquals("Arapça", ety.originLanguage)
        assertEquals(Relation.INHERITED, ety.chain[0].relation)
        assertTrue(ety.chain[1].gloss!!.contains("kitap"))
        assertTrue(r.languages.size >= 3) // Türkçe, Kırım Tatarcası, Tatarca ...
    }

    @Test
    fun ahmet_en_wiktionary_iki_dil_ve_turkce_once() {
        val r = wikt("en_Ahmet.html", Source.WIKT_EN, "Ahmet")!!
        assertEquals(listOf("Sırp-Hırvatça", "Türkçe"), r.languages.map { it.language })
        val tr = r.languages.first { it.language == "Türkçe" }.groups.first().etymology!!
        assertEquals("Arapça", tr.originLanguage)
        assertTrue(tr.chain.map { it.langName }.containsAll(listOf("Osmanlı Türkçesi", "Arapça")))
        val sh = r.languages.first { it.language == "Sırp-Hırvatça" }.groups.first().etymology!!
        assertEquals(Relation.BORROWED, sh.chain[0].relation)
    }

    @Test
    fun salary_en_latince_koken() {
        val r = wikt("en_salary.html", Source.WIKT_EN, "salary")!!
        val en = r.languages.first { it.language == "İngilizce" }
        val ety = en.groups.first().etymology!!
        assertTrue("zincir: ${ety.chain}", ety.chain.any { it.langName == "Latince" })
        assertEquals("Latince", ety.originLanguage?.takeIf { it == "Latince" } ?: ety.chain.first { it.langName == "Latince" }.langName)
        assertTrue(en.groups.flatMap { it.entries }.isNotEmpty())
    }

    @Test
    fun kitap_en_wiktionary_turkce_arapca() {
        val r = wikt("en_kitap.html", Source.WIKT_EN, "kitap")!!
        val tr = r.languages.first { it.language == "Türkçe" }
        assertEquals("Arapça", tr.groups.first { it.etymology != null }.etymology!!.originLanguage)
    }

    @Test
    fun mehmet_ve_elif_en() {
        assertNotNull(wikt("en_Mehmet.html", Source.WIKT_EN, "Mehmet"))
        val elif = wikt("en_Elif.html", Source.WIKT_EN, "Elif")!!
        assertTrue(elif.languages.any { it.language == "Türkçe" })
    }

    @Test
    fun vikipedi_isim_bilgi_kutusu_ve_ozet() {
        val elif = WikipediaParser.parse(fx("tr_wp_Elif.html"), "Elif", "https://x/Elif")!!
        assertTrue(elif.paragraphs.any { it.contains("kadınlara verilen") })
        assertTrue("anlam bilgi kutusunda olmalı: ${elif.facts}", elif.facts.any { it.first == "Anlam" && it.second.contains("ince") })
        val ahmet = WikipediaParser.parse(fx("tr_wp_Ahmet.html"), "Ahmet", "https://x/Ahmet")!!
        assertTrue(ahmet.paragraphs.first().contains("Arapça"))
    }

    /** Elle incelemek için tüm sonuçları dosyaya döker (app/build/parser_dump.txt) */
    @Test
    fun dump() {
        val sb = StringBuilder()
        val cases = listOf(
            Triple("tr_wikt_Ahmet.html", Source.WIKT_TR, "Ahmet"), Triple("tr_wikt_kitap.html", Source.WIKT_TR, "kitap"),
            Triple("en_Ahmet.html", Source.WIKT_EN, "Ahmet"), Triple("en_salary.html", Source.WIKT_EN, "salary"),
            Triple("en_kitap.html", Source.WIKT_EN, "kitap"), Triple("en_Mehmet.html", Source.WIKT_EN, "Mehmet"),
            Triple("en_Elif.html", Source.WIKT_EN, "Elif"), Triple("en_telefon.html", Source.WIKT_EN, "telefon"),
        )
        for ((f, s, t) in cases) {
            sb.append("\n######## $f\n")
            val r = wikt(f, s, t)
            if (r == null) { sb.append("(boş)\n"); continue }
            for (l in r.languages.take(6)) {
                sb.append("== ${l.language}\n")
                for (g in l.groups) {
                    sb.append("  [${g.heading}]\n")
                    g.etymology?.let { e ->
                        sb.append("   ETY: ${e.text.take(220)}\n")
                        sb.append("   ZİNCİR: " + e.chain.joinToString(" <- ") { "${it.langName}:${it.text}(${it.translit ?: ""})${it.gloss?.let { g2 -> "'$g2'" } ?: ""}${it.relation?.let { r2 -> "{${r2.label}}" } ?: ""}" } + "\n")
                        if (e.parts.isNotEmpty()) sb.append("   PARÇA: " + e.parts.joinToString(" + ") { "${it.langName}:${it.text}" } + "\n")
                        if (e.related.isNotEmpty()) sb.append("   AKRABA: " + e.related.joinToString(", ") { "${it.langName}:${it.text}" } + "\n")
                        sb.append("   KÖKEN DİLİ: ${e.originLanguage}\n")
                    }
                    for (p in g.entries) sb.append("   ${p.pos} (${p.headword}): ${p.definitions.take(2)}\n")
                }
            }
        }
        for (f in listOf("tr_wp_Elif.html", "tr_wp_Ahmet.html")) {
            val w = WikipediaParser.parse(fx(f), f, "u")
            sb.append("\n######## $f\n FACTS=${w?.facts}\n P=${w?.paragraphs}\n")
        }
        File("build/parser_dump.txt").writeText(sb.toString())
    }
}
