package com.jn.melodizil.core // Saf Kotlin çekirdek modelleri

import kotlin.math.ln // Doğal logaritma
import kotlin.math.pow // Üs alma

/** Tek bir melodi notası: MIDI numarası, başlangıç saniyesi, süre ve 0..1 arası şiddet. */
data class Note(
    val midi: Int, // MIDI nota numarası (60 = orta Do)
    val startSec: Float, // Başlangıç zamanı (saniye)
    val durationSec: Float, // Süre (saniye)
    val velocity: Float, // Şiddet 0..1 (salience'tan türetilir)
) {
    val endSec: Float get() = startSec + durationSec // Bitiş zamanı
    val frequencyHz: Float get() = midiToHz(midi) // Temel frekans
}

/** Çıkarılan melodinin tamamı: notalar ve kaynak parçanın süresi. */
data class Melody(
    val notes: List<Note>, // Zaman sırasına göre notalar
    val durationSec: Float, // Kaynak sesin toplam süresi
) {
    val isEmpty: Boolean get() = notes.isEmpty() // Hiç nota bulunamadı mı
}

/** MIDI numarasını Hz'e çevirir (A4 = 440 Hz). */
fun midiToHz(midi: Int): Float = (440.0 * 2.0.pow((midi - 69) / 12.0)).toFloat()

/** MIDI numarasını Hz'e çevirir (kesirli, cent hassasiyetinde). */
fun midiToHz(midi: Double): Double = 440.0 * 2.0.pow((midi - 69) / 12.0)

/** Hz'i kesirli MIDI numarasına çevirir. */
fun hzToMidi(hz: Double): Double = 69.0 + 12.0 * ln(hz / 440.0) / ln(2.0)

/** Nota adları (Türkçe solfej yerine uluslararası gösterim; kullanıcıya gösterimde kullanılır). */
private val NOTE_NAMES = arrayOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")

/** MIDI numarasını "C4" gibi ada çevirir. */
fun midiToName(midi: Int): String = NOTE_NAMES[((midi % 12) + 12) % 12] + (midi / 12 - 1)
