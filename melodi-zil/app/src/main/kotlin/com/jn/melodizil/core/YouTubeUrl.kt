package com.jn.melodizil.core // Saf Kotlin çekirdek

/** YouTube bağlantılarından video kimliğini çıkaran yardımcı. Ağ erişimi yok, saf metin işleme. */
object YouTubeUrl {
    private val ID = Regex("^[A-Za-z0-9_-]{11}$") // Geçerli video kimliği: 11 karakter

    /** Metnin içindeki ilk YouTube video kimliğini döndürür; yoksa null. */
    fun extractVideoId(text: String): String? {
        val t = text.trim() // Boşluklar atılıyor
        if (ID.matches(t)) return t // Kullanıcı doğrudan kimlik yapıştırmış
        val patterns = listOf( // Desteklenen bağlantı biçimleri
            Regex("""(?:youtube\.com|youtube-nocookie\.com)/(?:watch\?(?:.*&)?v=|embed/|v/|shorts/|live/)([A-Za-z0-9_-]{11})"""), // youtube.com/watch?v=, /shorts/, /embed/
            Regex("""youtu\.be/([A-Za-z0-9_-]{11})"""), // Kısa bağlantı
            Regex("""music\.youtube\.com/watch\?(?:.*&)?v=([A-Za-z0-9_-]{11})"""), // YouTube Music
        )
        for (p in patterns) { // Her desen deneniyor
            val m = p.find(t) // Eşleşme aranıyor
            if (m != null) return m.groupValues[1] // İlk yakalama grubu kimliktir
        }
        return null // Hiçbiri uymadı
    }

    /** Kimlikten NewPipe'ın anlayacağı standart izleme bağlantısını üretir. */
    fun canonical(videoId: String): String = "https://www.youtube.com/watch?v=$videoId"

    /** Metin bir YouTube bağlantısı ya da kimliği mi. */
    fun isYouTube(text: String): Boolean = extractVideoId(text) != null
}
