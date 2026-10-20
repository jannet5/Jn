package com.jn.koken

import com.jn.koken.data.DeepBuilder
import com.jn.koken.data.LookupResult
import com.jn.koken.data.Narrator
import com.jn.koken.data.Source
import com.jn.koken.data.WiktionaryParser
import java.io.File
import org.junit.Test

/** Farklı dillerdeki gerçek sayfalarla anlatım dökümü (elle inceleme için: app/build/multilang_dump.txt) */
class MultiLangDumpTest {
    private fun fx(n: String) = javaClass.getResource("/fixtures/$n")!!.readText()
    private fun w(n: String?, s: Source, t: String) = n?.let { WiktionaryParser.parse(fx(it), s, t, "u") }

    @Test
    fun dump() {
        val sb = StringBuilder()
        fun run(title: String, tr: String?, en: String?, deepPage: String? = null) {
            val r = LookupResult(title, w(tr, Source.WIKT_TR, title), w(en, Source.WIKT_EN, title), null)
            sb.append("\n######## $title\n")
            val chain = Narrator.chainOf(r)
            sb.append("ZİNCİR: " + chain.joinToString(" <- ") { "${it.langName}:${it.text}(${it.translit ?: ""})${it.gloss?.let { g -> "'$g'" } ?: ""}" } + "\n")
            val deep = if (deepPage != null && chain.isNotEmpty()) {
                val target = chain.lastOrNull { !it.langName.startsWith("Proto") }
                target?.let { t -> w(deepPage, Source.WIKT_EN, DeepBuilder.title(t) ?: "x")?.let { p -> DeepBuilder.build(t, p) } }
            } else null
            sb.append("DEEP: ${deep?.let { "term=${it.term.text} defs=${it.definitions} chain=${it.chain.map { m -> m.text + "{" + m.translit + "|" + m.gloss + "}" }} root=${it.root}" }}\n")
            val tx = Narrator.englishStrings(r, deep).associateWith { "TR[$it]" }
            val s = Narrator.compose(r, deep, tx, false)
            sb.append("[${s.badge}] ${s.word}\n")
            sb.append("  DİLLER=${s.languages} anlatılan=${s.language}\n")
            s.paras.forEach { sb.append("  ${it.label ?: "—"}: ${it.text}\n") }
            s.steps.forEach { st -> sb.append("  ADIM ${st.label} [${st.language}] ${st.form} ${st.tiles} ${st.translit ?: ""} | anlam=${st.meaning} | nasıl=${st.how} | not=${st.note} | parça=${st.parts}\n") }
            sb.append("  YAZI: ${s.narration}\n")
        }
        run("kader", "tr_wikt_kader.html", "en_kader.html", "en_ar_qadar.html")
        run("دوست", null, "en_fa_dust.html")
        run("ordinateur", null, "en_fr_ordinateur.html")
        File("build/multilang_dump.txt").writeText(sb.toString())
    }
}
