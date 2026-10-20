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
        File("build/story_dump.txt").appendText("\n######## $name\n[${s.badge}] ${s.word} (${s.language}; diller=${s.languages})\n" + s.paras.joinToString("") { "  ${it.label ?: "—"}: ${it.text.replace("\n", " | ")}\n" } +
            s.steps.joinToString("") { st -> "  ADIM ${st.label} [${st.language}] ${st.form} ${st.tiles} ${st.translit ?: ""} | anlam=${st.meaning} | nasıl=${st.how} | not=${st.note} | parça=${st.parts}\n" } +
            "  YAZI: ${s.narration}\n")
    }

    private fun verbOf(d: DeepInfo, page: String): DeepInfo {
        val base = d.chain.first()
        return d.copy(verb = com.jn.koken.data.ArabicVerbParser.parse(fx(page), base))
    }

    @Test
    fun mumtaz_harfler_kalip_fiil_ortac() {
        val r = LookupResult("mümtaz", wikt("tr_wikt_mumtaz.html", Source.WIKT_TR, "mümtaz"), wikt("en_mumtaz.html", Source.WIKT_EN, "mümtaz"), null)
        val deep = verbOf(deepOf(r, "en_ar_mumtaz.html", "ممتاز")!!, "en_ar_verb_imtaza.html")
        assertEquals("م ي ز", deep.root?.letters)
        assertEquals("VIII", deep.verb?.form)
        assertEquals("imtiyāz", deep.verb?.verbalNoun?.translit)
        assertEquals("مُمْتَاز", deep.verb?.activeParticiple?.text)
        val s = story(r, deep)
        record("mümtaz", s)
        assertEquals("Arapça kökenli", s.badge)
        val meaning = s.paras[0].text
        assertEquals("⟦mümtaz⟧, ⟦seçkin⟧ demek.", meaning)
        val t = s.narration!!
        assertTrue(t, t.startsWith("Arapçadan gelmiş. Arapçada sözcükler üç harfli köklerden türetilir; bu sözcüğün kök harfleri ⟦م + ي + ز⟧ (m + y + z)."))
        assertTrue(t, t.contains("Bu köke VIII. bâb (iftiʿâl) kalıbı uygulanınca ⟦اِمْتَازَ⟧ (imtāza) fiili oluşmuş: “TR[to be characterized or distinguished by]; TR[to surpass, to be superior]”."))
        assertTrue(t, t.contains("Bu fiilin etken ortacı (ism-i fâil) olan ⟦مُمْتَاز⟧ (mumtāz) ise “TR[excellent]; TR[distinguished, selected]; TR[privileged]” demek."))
        assertTrue(t, t.contains("Aynı fiilin mastarı ise ⟦اِمْتِيَاز⟧ (imtiyāz)."))
        assertTrue(t, t.contains("Sonra Osmanlı Türkçesine ⟦ممتاز⟧ olarak geçmiş."))
        assertTrue(t, t.endsWith("Türkçede ise ⟦mümtaz⟧ biçimini almış."))
        assertTrue(s.timeline.last().isRoot)
        assertEquals("VIII. bâb (iftiʿâl)", s.timeline.last().how)
    }

    @Test
    fun ahmet_isim_ustunluk_sifati() {
        val r = LookupResult(
            "Ahmet", wikt("tr_wikt_Ahmet.html", Source.WIKT_TR, "Ahmet"), wikt("en_Ahmet.html", Source.WIKT_EN, "Ahmet"),
            WikipediaParser.parse(fx("tr_wp_Ahmet.html"), "Ahmet", "https://x/Ahmet"),
        )
        val deep = deepOf(r, "en_ar_ahmad.html", "أحمد")!!
        val s = story(r, deep)
        record("Ahmet", s)
        assertTrue(s.paras[0].text, s.paras[0].text.startsWith("⟦Ahmet⟧, bir erkek ismi. ⟦TR["))
        val t = s.narration!!
        assertTrue(t, t.startsWith("Arapçadan gelmiş."))
        assertTrue(t, t.contains("kök harfleri ⟦ح + م + د⟧ (ḥ + m + d)."))
        assertTrue(t, t.contains("Bu kökten ⟦حَمِيد⟧ (ḥamīd, “TR[praiseworthy; benign]”) türemiş."))
        assertTrue(t, t.contains("onun üstünlük sıfatı (ism-i tafdîl)"))
        assertTrue(t, t.contains("Sonra Osmanlı Türkçesine ⟦احمد⟧ (ahmed) olarak geçmiş."))
        assertTrue(s.paras.any { it.label == "Vikipedi'ye göre" && it.text.contains("erkek çocuklara verilen") })
    }

    @Test
    fun kitap_kok_fiil_mastar() {
        val r = LookupResult("kitap", wikt("tr_wikt_kitap.html", Source.WIKT_TR, "kitap"), wikt("en_kitap.html", Source.WIKT_EN, "kitap"), null)
        val deep0 = deepOf(r, "en_ar_kitab.html", "كتاب")!!
        val deep = verbOf(deep0, "en_ar_verb_kataba.html")
        assertEquals("ك ت ب", deep.root?.letters)
        assertEquals("I", deep.verb?.form)
        val s = story(r, deep)
        record("kitap", s)
        assertEquals("Arapça kökenli", s.badge)
        assertTrue(s.paras[0].text, s.paras[0].text.startsWith("⟦kitap⟧ şu anlama geliyor: Ciltli veya ciltsiz"))
        val t = s.narration!!
        assertTrue(t, t.contains("kök harfleri ⟦ك + ت + ب⟧ (k + t + b)."))
        assertTrue(t, t.contains("Bu köke I. bâb (temel kalıp, faʿale) kalıbı uygulanınca ⟦كَتَبَ⟧ (kataba) fiili oluşmuş: “TR[to write, to inscribe]"))
        assertTrue(t, t.contains("Bu fiilin mastarı (fiil adı) olan ⟦كِتَاب⟧ (kitāb)"))
        assertTrue(t, t.contains("Sonra Osmanlı Türkçesine ⟦كتاب⟧ (kitab) olarak geçmiş."))
    }

    @Test
    fun salary_kok_yok_eskiden_yeniye() {
        val r = LookupResult("salary", null, wikt("en_salary.html", Source.WIKT_EN, "salary"), null)
        val deep = deepOf(r, "en_la_sal.html", "sal")
        val s = story(r, deep)
        record("salary", s)
        assertEquals("Latince kökenli", s.badge)
        val t = s.narration!!
        assertTrue(t, t.contains("Ardından Latinceye ⟦sal⟧ (“TR[salt]”) olarak geçmiş, ondan ⟦salārius⟧ (“TR[related to salt]”), ondan ⟦salārium⟧ (“TR[wages]”) olmuş."))
        assertTrue(t, t.contains("Daha sonra Eski Fransızcaya ⟦salaire⟧ olarak geçmiş."))
        assertTrue(t, t.contains("Derken Anglo-Norman'a ⟦salarie⟧ olarak geçmiş."))
        assertTrue(t, t.contains("Sonra Orta İngilizceye ⟦salarie⟧ olarak geçmiş."))
        assertTrue(t, t.endsWith("İngilizcede ise ⟦salary⟧ biçimini almış."))
        assertTrue(t, t.startsWith("Proto-Hint-Avrupa dilinde ⟦*sḗh₂l⟧ diye bir sözcük varmış."))
        assertTrue(t, t.contains("Sonra Proto-İtalik dilde ⟦*sāls⟧ biçimine dönüşmüş."))
    }

    @Test
    fun ceviri_tekrarlari_atilir() {
        val r = LookupResult("salary", null, wikt("en_salary.html", Source.WIKT_EN, "salary"), null)
        val en = Narrator.englishStrings(r, null).first()
        val s = Narrator.compose(r, null, mapOf(en to "maaş, maaş, aylık"), translating = false)
        assertTrue(s.paras[0].text, s.paras[0].text.contains("⟦maaş, aylık⟧ demek."))
    }

    @Test
    fun cevirisi_kelimenin_kendisi_olan_anlam_atilir() {
        // en.wiktionary Türkçe "kitap" için sadece "book" der: çevirisi "kitap" = kendisi, bilgi vermez
        val r = LookupResult("kitap", null, wikt("en_kitap.html", Source.WIKT_EN, "kitap"), null)
        val deep = verbOf(deepOf(r, "en_ar_kitab.html", "كتاب")!!, "en_ar_verb_kataba.html")
        val en = Narrator.englishStrings(r, deep)
        val tr = en.associateWith { if (it == "book") "kitap" else "TR[$it]" }
        val s = Narrator.compose(r, deep, tr, translating = false)
        assertTrue(s.paras[0].text, !s.paras[0].text.contains("⟦kitap⟧, ⟦kitap⟧ demek"))
        assertTrue(s.paras[0].text, s.paras[0].text.startsWith("⟦kitap⟧, ⟦TR[letter, note, paper, piece of writing, message]⟧ demek."))
    }

    @Test
    fun baska_dilden_ata_kokten_turemis_denmez() {
        val r = LookupResult("Elif", null, wikt("en_Elif.html", Source.WIKT_EN, "Elif"), null)
        val target = Narrator.chainOf(r).last { !it.langName.startsWith("Proto") }
        val base = com.jn.koken.data.Mention("phn", "Proto-Sinaitic", "𐩱𐩡𐩽", "ʾlp", null, null)
        val deep = DeepInfo(
            term = target, definitions = listOf("name of the first letter of the Arabic alphabet"), chain = listOf(base),
            root = com.jn.koken.data.Root("ء ل ف", "ʔ l f"),
        )
        val s = story(r, deep)
        val t = s.narration!!
        assertTrue(t, !t.contains("Bu kökten ⟦𐩱𐩡𐩽⟧"))
        assertTrue(t, t.contains("ise Proto-Sinaitic ⟦𐩱𐩡𐩽⟧ (ʾlp) sözcüğünden gelmiş: “TR[name of the first letter of the Arabic alphabet]”."))
    }

    @Test
    fun vikipedi_sadece_isimlerde_istenir() {
        val ahmet = wikt("tr_wikt_Ahmet.html", Source.WIKT_TR, "Ahmet")
        val kitap = wikt("tr_wikt_kitap.html", Source.WIKT_TR, "kitap")
        assertTrue(Narrator.wantsWikipedia(ahmet, null))
        assertTrue(!Narrator.wantsWikipedia(kitap, null))
        assertTrue(Narrator.wantsWikipedia(null, null))
    }

    @Test
    fun uzun_tanim_kisa_es_anlamlilara_iner() {
        assertEquals("to write, to inscribe", Narrator.shorten("to mark (symbols, shapes, graphs, glyphs, letters, and the like) on a surface, to write, to inscribe"))
        assertEquals("to surpass, to be superior", Narrator.shorten("to surpass, to be superior"))
        assertEquals("excellent", Narrator.shorten("excellent"))
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
        assertTrue("çeviri olmazsa İngilizce kalmalı", failed.paras[0].text.contains("A fixed amount of money"))
    }

    @Test
    fun olusum_adimlari_kokten_sozcuge_mumtaz() {
        val r = LookupResult("mümtaz", wikt("tr_wikt_mumtaz.html", Source.WIKT_TR, "mümtaz"), wikt("en_mumtaz.html", Source.WIKT_EN, "mümtaz"), null)
        val deep = verbOf(deepOf(r, "en_ar_mumtaz.html", "ممتاز")!!, "en_ar_verb_imtaza.html")
        val steps = story(r, deep).steps
        assertEquals(listOf("KÖK", "FİİL", "SÖZCÜK", "ATA", "BUGÜN"), steps.map { it.label })
        assertEquals(listOf("م", "ي", "ز"), steps[0].tiles)
        assertEquals("Köke VIII. bâb (iftiʿâl) kalıbı uygulanınca oluştu.", steps[1].how)
        assertEquals("Bu fiilin etken ortacı (ism-i fâil) olarak oluştu.", steps[2].how)
        assertEquals("Osmanlı Türkçesine geçti.", steps[3].how)
        assertEquals("Türkçede bugünkü biçimini aldı.", steps.last().how)
        assertTrue(steps.last().isWord)
    }

    @Test
    fun kader_ata_fiilin_anlami_dogru_ayrilir() {
        val r = LookupResult("kader", wikt("tr_wikt_kader.html", Source.WIKT_TR, "kader"), wikt("en_kader.html", Source.WIKT_EN, "kader"), null)
        val deep = deepOf(r, "en_ar_qadar.html", "قدر")!!
        val base = deep.chain.first()
        assertEquals("قَدِرَ", base.text)
        assertEquals("qadira", base.translit)
        assertEquals("to be able to", base.gloss)
        assertEquals("ق د ر", deep.root?.letters)
        assertEquals("mastarı (fiil adı)", base.how)
    }

    @Test
    fun ayni_yazilis_birden_cok_dilde_dil_secilir() {
        val r = LookupResult("kader", wikt("tr_wikt_kader.html", Source.WIKT_TR, "kader"), wikt("en_kader.html", Source.WIKT_EN, "kader"), null)
        val diller = Narrator.languagesOf(r)
        assertEquals("Türkçe", diller.first())
        assertTrue(diller.toString(), diller.size > 1)
        val other = diller.first { it != "Türkçe" }
        assertEquals(other, Narrator.compose(r, null, emptyMap(), translating = false, language = other).language)
        assertEquals("Türkçe", Narrator.compose(r, null, emptyMap(), translating = false).language)
    }

    @Test
    fun farsca_sozcukte_farsca_secilir_ve_basamaklar_dizilir() {
        val r = LookupResult("دوست", null, wikt("en_fa_dust.html", Source.WIKT_EN, "دوست"), null)
        val s = Narrator.compose(r, null, emptyMap(), translating = false)
        assertEquals("Farsça", s.language)
        assertEquals("Eski Farsça kökenli", s.badge)
        assertEquals("KÖK", s.steps.first().label)
        assertTrue(s.steps.last().isWord)
        assertEquals("Farsçada bugünkü biçimini aldı.", s.steps.last().how)
        assertTrue(s.steps.size >= 4)
    }

    @Test
    fun bilesenler_dogru_basamaga_baglanir() {
        val r = LookupResult("ordinateur", null, wikt("en_fr_ordinateur.html", Source.WIKT_EN, "ordinateur"), null)
        val s = Narrator.compose(r, null, emptyMap(), translating = false)
        val owner = s.steps.first { it.parts != null }
        assertEquals("Latince", owner.language)
        assertTrue(owner.parts!!, owner.parts!!.contains("ōrdināre") && owner.parts!!.contains("-tor"))
        assertTrue(s.narration!!, s.narration!!.contains("Latince ⟦ōrdinātōrem⟧ sözcüğü ise ⟦ōrdināre⟧ ile ⟦-tor⟧ sözcüklerinin birleşmesinden oluşmuş."))
    }
}
