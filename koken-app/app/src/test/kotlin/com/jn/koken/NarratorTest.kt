package com.jn.koken

import com.jn.koken.data.DeepBuilder
import com.jn.koken.data.DeepInfo
import com.jn.koken.data.LookupResult
import com.jn.koken.data.Narrator
import com.jn.koken.data.Source
import com.jn.koken.data.Story
import com.jn.koken.data.WikipediaParser
import com.jn.koken.data.WiktionaryParser
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** Gerçek sayfalardan düz Türkçe anlatım üretimi (çeviri sahte: "TR[...]") */
class NarratorTest {
    private fun fx(name: String): String = javaClass.getResource("/fixtures/$name")!!.readText()

    private fun wikt(name: String?, src: Source, title: String) =
        name?.let { WiktionaryParser.parse(fx(it), src, title, "https://x/$title") }

    private fun deepOf(r: LookupResult, page: String, title: String): DeepInfo? {
        val target = Narrator.chainOf(r).last { !it.langName.startsWith("Proto") }
        return DeepBuilder.build(target, WiktionaryParser.parse(fx(page), Source.WIKT_EN, title, "u")!!)
    }

    private fun story(r: LookupResult, deep: DeepInfo?): Story {
        val fake = Narrator.englishStrings(r, deep).associateWith { "TR[$it]" }
        return Narrator.compose(r, deep, fake, translating = false)
    }

    private fun Story.para(label: String?) = paras.firstOrNull { it.label == label }?.text

    private val dump = StringBuilder()
    private fun record(name: String, s: Story) {
        dump.append("\n######## $name\n[${s.badge}] ${s.word}\n")
        s.paras.forEach { dump.append("  ${it.label ?: "—"}: ${it.text.replace("\n", " | ")}\n") }
        dump.append("  YOLCULUK: " + s.timeline.joinToString(" <- ") { (if (it.isRoot) "KÖK " else "") + it.langName + ":" + it.text } + "\n")
        File("build/story_dump.txt").appendText("\n######## $name\n[${s.badge}] ${s.word}\n" + s.paras.joinToString("") { "  ${it.label ?: "—"}: ${it.text.replace("\n", " | ")}\n" } + "  YOLCULUK: " + s.timeline.joinToString(" <- ") { (if (it.isRoot) "KÖK " else "") + it.langName + ":" + it.text } + "\n")
    }

    @Test
    fun mumtaz_anlam_koken_ve_kok() {
        val r = LookupResult("mümtaz", wikt("tr_wikt_mumtaz.html", Source.WIKT_TR, "mümtaz"), wikt("en_mumtaz.html", Source.WIKT_EN, "mümtaz"), null)
        val deep = deepOf(r, "en_ar_mumtaz.html", "ممتاز")!!
        assertEquals("م ي ز", deep.root?.letters)
        assertEquals("m y z", deep.root?.translit)
        val s = story(r, deep)
        record("mümtaz", s)
        assertEquals("Arapça kökenli", s.badge)
        assertEquals("Arapça kökenli bir sıfattır.", s.paras.first().text)
        assertEquals("Seçkin", s.para("Anlamı"))
        assertEquals(
            "mümtaz, Osmanlı Türkçesi ممتاز sözcüğünden devralınmıştır, o da Arapça مُمْتَاز (mumtāz) sözcüğünden gelmektedir.",
            s.para("Nereden geldi?"),
        )
        assertTrue(s.para("Kaynak sözcüğün anlamı")!!.contains("TR[excellent]"))
        assertTrue(s.para("Kökü")!!.contains("Üç harfli Arapça kök: م ي ز (m y z)."))
        assertTrue(s.para("Kökü")!!.startsWith("Arapça مُمْتَاز (mumtāz), اِمْتَازَ (imtāza"))
        assertTrue(s.timeline.last().isRoot)
    }

    @Test
    fun ahmet_isim_adin_anlami_vikipedi() {
        val r = LookupResult(
            "Ahmet", wikt("tr_wikt_Ahmet.html", Source.WIKT_TR, "Ahmet"), wikt("en_Ahmet.html", Source.WIKT_EN, "Ahmet"),
            WikipediaParser.parse(fx("tr_wp_Ahmet.html"), "Ahmet", "https://x/Ahmet"),
        )
        val deep = deepOf(r, "en_ar_ahmad.html", "أحمد")!!
        val s = story(r, deep)
        record("Ahmet", s)
        assertEquals("Arapça kökenli bir erkek ismidir.", s.paras.first().text)
        assertNotNull("adın anlamı kaynak sözcükten gelmeli", s.para("Adın anlamı"))
        assertTrue(s.para("Adın anlamı")!!.startsWith("TR["))
        assertTrue(s.para("Vikipedi'ye göre")!!.contains("erkek çocuklara verilen"))
        assertTrue(s.para("Nereden geldi?")!!.startsWith("Ahmet, Osmanlı Türkçesi احمد (ahmed) sözcüğünden devralınmıştır"))
    }

    @Test
    fun kitap_cok_anlam_ve_kok() {
        val r = LookupResult("kitap", wikt("tr_wikt_kitap.html", Source.WIKT_TR, "kitap"), wikt("en_kitap.html", Source.WIKT_EN, "kitap"), null)
        val deep = deepOf(r, "en_ar_kitab.html", "كتاب")!!
        val s = story(r, deep)
        record("kitap", s)
        assertEquals("Arapça kökenli bir sözcüktür.", s.paras.first().text)
        assertTrue(s.para("Anlamı")!!.startsWith("1) "))
        assertTrue("kök: ${deep.root}", deep.root?.letters == "ك ت ب")
    }

    @Test
    fun salary_ingilizce_latince() {
        val r = LookupResult("salary", null, wikt("en_salary.html", Source.WIKT_EN, "salary"), null)
        val deep = deepOf(r, "en_la_sal.html", "sal")
        val s = story(r, deep)
        record("salary", s)
        assertEquals("İngilizce bir sözcüktür; Latince kökenlidir.", s.paras.first().text)
        assertTrue(s.para("Nereden geldi?")!!.startsWith("salary, Orta İngilizce salarie sözcüğünden"))
    }

    @Test
    fun arapca_ahmad_dokum() {
        val page = WiktionaryParser.parse(fx("en_ar_ahmad.html"), Source.WIKT_EN, "أحمد", "u")!!
        val sb = StringBuilder()
        for (l in page.languages.filter { it.language == "Arapça" }) {
            sb.append("ROOT=${l.root}\n")
            for (g in l.groups) {
                sb.append("[${g.heading}] ety=${g.etymology?.chain?.joinToString { it.text }}\n")
                for (e in g.entries) sb.append("   ${e.pos} (${e.headword}): ${e.definitions}\n")
            }
        }
        File("build/arabic_dump.txt").writeText(sb.toString())
    }

    @Test
    fun ceviri_bekleniyorsa_ingilizce_gosterilmez() {
        val r = LookupResult("salary", null, wikt("en_salary.html", Source.WIKT_EN, "salary"), null)
        val s = Narrator.compose(r, null, emptyMap(), translating = true)
        assertTrue(s.pending)
        assertTrue(s.paras.none { it.text.contains("A fixed amount of money") })
        val failed = Narrator.compose(r, null, emptyMap(), translating = false)
        assertTrue("çeviri olmazsa İngilizce kalmalı", failed.para("Anlamı")!!.contains("A fixed amount of money"))
    }
}
