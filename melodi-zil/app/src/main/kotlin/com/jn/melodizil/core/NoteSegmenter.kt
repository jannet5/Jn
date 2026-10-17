package com.jn.melodizil.core // Çekirdek

import kotlin.math.abs // Mutlak değer
import kotlin.math.max // En büyük
import kotlin.math.roundToInt // Yuvarlama

/**
 * Kare başına perde (kesirli MIDI, 0 = melodi yok) → notalar.
 * Medyan filtre, histerezisli nota değişimi, kısa nota/kayma temizliği, kısa boşlukları doldurma (legato).
 * Melodia yolu ve RMVPE yolu aynı bölütleyiciyi kullanır; ayarlar araclar/degerlendirme ile ölçülerek seçildi.
 */
class NoteSegmenter(private val p: Params = Params()) {

    data class Params(
        val hysteresis: Float = 0.5f, // Nota değişimi eşiği (yarım ton)
        val minNoteSec: Float = 0.045f, // En kısa nota
        val legatoGapSec: Float = 0.12f, // Doldurulan boşluk
        val absorbSec: Float = 0.12f, // Kayma/vibrato parçası: bu süreden kısa ve komşusundan ±1 yarım ton farklı nota uzun komşuya katılır (0 = kapalı)
        val medianSec: Float = 0.058f, // Medyan filtre penceresi
        val mergeGapSec: Float = 0.06f, // Aynı perdede birleştirme boşluğu (ve kayma birleştirmede bitişiklik)
        val fillGapSec: Float = 0f, // Perde içindeki bu süreden kısa sessizlikler (sessiz harf, nefes) önceki/sonraki perdeyle doldurulur (0 = kapalı)
        val tune: Boolean = false, // Şarkının akort kaymasını (A=440'tan sapma) bulup yuvarlamayı ona göre yap
    )

    /** Son segment() çağrısında bulunan akort kayması (yarım ton, -0.5..0.5). Notalar bu kadar kaydırılmış ızgaraya yuvarlanır. */
    var lastTuning = 0f; private set

    /** Sesli karelerin kesirli kısmının dairesel ortalaması: şarkının A=440'a göre akort kayması (yarım ton). */
    fun tuning(midi: FloatArray): Float {
        var sx = 0.0; var sy = 0.0 // Birim çember toplamı
        for (m in midi) if (m > 0f) { val a = 2 * Math.PI * (m - Math.round(m)); sx += kotlin.math.cos(a); sy += kotlin.math.sin(a) } // Her kare
        return if (sx == 0.0 && sy == 0.0) 0f else (kotlin.math.atan2(sy, sx) / (2 * Math.PI)).toFloat() // Ortalama açı → yarım ton
    }

    /** midi: kare başına kesirli MIDI (0 = sessiz), frameSec: kare süresi, offsetSec: 0. karenin merkez zamanı. */
    fun segment(midiRaw: FloatArray, frameSec: Float, offsetSec: Float): List<Note> {
        lastTuning = if (p.tune) tuning(midiRaw) else 0f // Akort kayması
        val midiIn = if (lastTuning == 0f) midiRaw else FloatArray(midiRaw.size) { if (midiRaw[it] > 0f) midiRaw[it] - lastTuning else 0f } // Izgaraya hizala
        val midi = if (p.fillGapSec > 0f) fillGaps(midiIn, (p.fillGapSec / frameSec).roundToInt()) else midiIn // Kısa sessizlikleri doldur
        val win = max(1, (p.medianSec / frameSec).roundToInt()) or 1 // Tek sayılı medyan penceresi
        val sm = FloatArray(midi.size) // Medyan filtrelenmiş
        val half = win / 2; val buf = FloatArray(win) // Pencere
        for (t in midi.indices) { if (midi[t] == 0f) continue; var c = 0
            for (k in -half..half) { val i = t + k; if (i in midi.indices && midi[i] > 0f) buf[c++] = midi[i] } // Sesli komşular
            java.util.Arrays.sort(buf, 0, c); sm[t] = buf[c / 2] } // Medyan
        fun note(s: Int, e: Int, m: Int): Note { val st = max(0f, offsetSec + s * frameSec - frameSec / 2); return Note(m, st, offsetSec + e * frameSec - frameSec / 2 - st, 0.85f) } // Kare aralığı → nota (negatif başlangıç yok)
        val notes = ArrayList<Note>() // Notalar
        val minFrames = max(1, (p.minNoteSec / frameSec).roundToInt()) // En kısa nota (kare)
        var t = 0 // Kare
        while (t < sm.size) { // Sesli koşular
            if (sm[t] == 0f) { t++; continue } // Sessiz
            var start = t; var cur = sm[t].roundToInt(); var pend = -1; var pendStart = 0 // Mevcut nota ve bekleyen değişim
            while (t < sm.size && sm[t] > 0f) { // Koşu boyunca
                val r = sm[t].roundToInt() // En yakın yarım ton
                if (abs(sm[t] - cur) > p.hysteresis) { // Mevcut notadan belirgin uzak
                    if (pend != r) { pend = r; pendStart = t } // Yeni aday
                    if (t - pendStart + 1 >= minFrames) { // Aday yeterince sürdü: nota değiştir
                        if (pendStart > start) notes.add(note(start, pendStart, cur)) // Önceki nota
                        start = pendStart; cur = pend; pend = -1 // Yeni nota
                    }
                } else pend = -1 // Aday iptal
                t++ // Sonraki kare
            }
            notes.add(note(start, t, cur)) // Koşunun son notası
        }
        val merged = ArrayList<Note>() // Aynı perdede çok yakın notaları birleştir
        for (nt in notes) { val l = merged.lastOrNull()
            if (l != null && l.midi == nt.midi && nt.startSec - l.endSec <= p.mergeGapSec) merged[merged.size - 1] = l.copy(durationSec = nt.endSec - l.startSec)
            else merged.add(nt) }
        val clean0 = merged.filter { it.durationSec >= p.minNoteSec } // Çok kısaları at
        val clean = if (p.absorbSec > 0f) absorbGlides(clean0) else clean0 // Kayma/vibrato parçalarını birleştir
        val legato = ArrayList<Note>() // Kısa boşlukları doldur (zil sesinde kesik kesik duyulmasın)
        for ((i, nt) in clean.withIndex()) { val next = clean.getOrNull(i + 1)
            legato.add(if (next != null && next.startSec - nt.endSec in 0f..p.legatoGapSec) nt.copy(durationSec = next.startSec - nt.startSec) else nt) }
        return legato // Sonuç
    }

    /** İki sesli kare arasındaki en fazla maxFrames uzunluğundaki sessizliği, perdeler yakınsa (≤1 yarım ton) önceki perdeyle doldurur. */
    private fun fillGaps(m: FloatArray, maxFrames: Int): FloatArray {
        val out = m.copyOf(); var t = 0 // Kopya
        while (t < out.size) {
            if (out[t] != 0f || t == 0 || out[t - 1] == 0f) { t++; continue } // Boşluk başlangıcı değil
            var e = t; while (e < out.size && out[e] == 0f) e++ // Boşluğun sonu
            if (e < out.size && e - t <= maxFrames && abs(out[e] - out[t - 1]) <= 1f) for (k in t until e) out[k] = out[t - 1] // Kısa ve aynı perde: doldur
            t = e // Devam
        }
        return out // Sonuç
    }

    /** Kısa (< absorbSec) ve bitişik komşusundan ±1 yarım ton farklı notayı, daha uzun bitişik komşuya katar; aynı perdeli komşular birleşir. */
    private fun absorbGlides(input: List<Note>): List<Note> {
        val ns = input.toMutableList(); var changed = true // Çalışma listesi
        while (changed) { changed = false
            for (i in ns.indices) { val n = ns[i]; if (n.durationSec >= p.absorbSec) continue // Uzun nota: dokunma
                val prev = ns.getOrNull(i - 1)?.takeIf { n.startSec - it.endSec <= p.mergeGapSec && abs(it.midi - n.midi) == 1 } // Bitişik önceki
                val next = ns.getOrNull(i + 1)?.takeIf { it.startSec - n.endSec <= p.mergeGapSec && abs(it.midi - n.midi) == 1 } // Bitişik sonraki
                val host = listOfNotNull(prev, next).maxByOrNull { it.durationSec } ?: continue // Uzun olan komşu
                if (host.durationSec <= n.durationSec) continue // Komşu daha kısa
                val hi = ns.indexOf(host); val s0 = minOf(host.startSec, n.startSec); val e0 = maxOf(host.endSec, n.endSec) // Birleşik aralık
                ns[hi] = host.copy(startSec = s0, durationSec = e0 - s0); ns.removeAt(i); changed = true; break } // Kat ve baştan tara
            if (!changed) { for (i in ns.size - 1 downTo 1) { val a = ns[i - 1]; val b = ns[i]
                if (a.midi == b.midi && b.startSec - a.endSec <= p.mergeGapSec) { ns[i - 1] = a.copy(durationSec = b.endSec - a.startSec); ns.removeAt(i); changed = true } } } } // Aynı perdeleri birleştir
        return ns // Sonuç
    }

    companion object {
        /** "anahtar=değer,..." metninden Params (değerlendirme aracı için). */
        fun parse(text: String?, base: Params = Params()): Params { var q = base; if (text.isNullOrBlank()) return q
            for (kv in text.split(',')) { val parts = kv.split('='); if (parts.size != 2) continue; val k = parts[0].trim(); val v = parts[1].trim()
                q = when (k) { "hyst" -> q.copy(hysteresis = v.toFloat()); "minnote" -> q.copy(minNoteSec = v.toFloat()); "legato" -> q.copy(legatoGapSec = v.toFloat())
                    "absorb" -> q.copy(absorbSec = v.toFloat()); "median" -> q.copy(medianSec = v.toFloat()); "merge" -> q.copy(mergeGapSec = v.toFloat()); "fill" -> q.copy(fillGapSec = v.toFloat()); "tune" -> q.copy(tune = v == "1"); else -> q } }
            return q }
    }
}
