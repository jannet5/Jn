package app.nokta.a.model

/** Hesapsız yedek: düz metin. "[ ] süt" / "[x] ekmek". */
object Export {
    fun toText(state: ListState): String =
        state.items.joinToString("\n") { (if (it.done) "[x] " else "[ ] ") + it.text }

    /** toText çıktısını (veya düz satırları) geri okur. */
    fun fromText(text: String): List<Pair<String, Boolean>> =
        text.lines().mapNotNull { line ->
            val l = line.trim()
            when {
                l.startsWith("[x] ", true) -> ListState.normalize(l.substring(4)) to true
                l.startsWith("[ ] ") -> ListState.normalize(l.substring(4)) to false
                else -> ListState.normalize(l) to false
            }.takeIf { it.first.isNotEmpty() }
        }
}
