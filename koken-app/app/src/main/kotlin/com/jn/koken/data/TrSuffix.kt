package com.jn.koken.data

import java.util.Locale

/**
 * Dil adlarına Türkçe hâl ekleri (ünlü uyumu + ünsüz benzeşmesi):
 * Arapça → Arapçadan / Arapçaya / Arapçada, Osmanlı Türkçesi → Osmanlı Türkçesinden / -ne / -nde.
 */
object TrSuffix {
    private const val FRONT = "eiöü"
    private const val BACK = "aıou"
    private const val VOWELS = FRONT + BACK
    private const val VOICELESS = "çfhkpsşt"
    private val trLocale = Locale("tr", "TR")

    // "Türkçesi", "Yunancası", "…dili" gibi iyelik ekli adlar: -nden, -ne, -nde
    private val possessive = Regex(".*(çesi|çası|cesi|cası|dili)$")
    // Türkçe dil adı gibi okunanlar kesmesiz (Arapçaya); yabancı özel adlar kesmeli (Anglo-Norman'a)
    private val plainName = Regex(".*(ça|çe|ca|ce|çesi|çası|cesi|cası|dili|dil)$")

    private fun front(low: String) = low.lastOrNull { it in VOWELS }?.let { it in FRONT } ?: true
    private fun endsWithVowel(low: String) = low.lastOrNull()?.let { it in VOWELS } ?: false
    private fun apostrophe(low: String) = if (plainName.matches(low)) "" else "'"

    fun dative(name: String): String {
        val low = name.lowercase(trLocale)
        val e = if (front(low)) "e" else "a"
        return when {
            possessive.matches(low) -> name + "n" + e
            endsWithVowel(low) -> name + apostrophe(low) + "y" + e
            else -> name + apostrophe(low) + e
        }
    }

    fun locative(name: String): String {
        val low = name.lowercase(trLocale)
        val e = if (front(low)) "e" else "a"
        return when {
            possessive.matches(low) -> name + "nd" + e
            endsWithVowel(low) -> name + apostrophe(low) + "d" + e
            else -> name + apostrophe(low) + (if (low.last() in VOICELESS) "t" else "d") + e
        }
    }

    fun ablative(name: String): String {
        val low = name.lowercase(trLocale)
        val e = if (front(low)) "e" else "a"
        return when {
            possessive.matches(low) -> name + "nd" + e + "n"
            endsWithVowel(low) -> name + apostrophe(low) + "d" + e + "n"
            else -> name + apostrophe(low) + (if (low.last() in VOICELESS) "t" else "d") + e + "n"
        }
    }
}
