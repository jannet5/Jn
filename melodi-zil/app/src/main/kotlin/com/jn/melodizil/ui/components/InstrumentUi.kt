package com.jn.melodizil.ui.components // Ortak bileşenler

import androidx.compose.material.icons.Icons // İkonlar
import androidx.compose.material.icons.rounded.Air // Flüt
import androidx.compose.material.icons.rounded.Audiotrack // Gitar
import androidx.compose.material.icons.rounded.GraphicEq // Marimba
import androidx.compose.material.icons.rounded.MusicNote // Müzik kutusu
import androidx.compose.material.icons.rounded.NotificationsActive // Çan
import androidx.compose.material.icons.rounded.Piano // Piyano
import androidx.compose.material.icons.rounded.SportsEsports // 8-bit
import androidx.compose.material.icons.rounded.Waves // Synth
import androidx.compose.ui.graphics.vector.ImageVector // İkon
import com.jn.melodizil.core.Instrument // Enstrüman

/** Enstrüman → ikon eşlemesi (tek ikon seti: Material Rounded). */
fun Instrument.icon(): ImageVector = when (this) {
    Instrument.PIYANO -> Icons.Rounded.Piano // Piyano
    Instrument.MUZIK_KUTUSU -> Icons.Rounded.MusicNote // Müzik kutusu
    Instrument.SEKIZ_BIT -> Icons.Rounded.SportsEsports // Oyun
    Instrument.MARIMBA -> Icons.Rounded.GraphicEq // Vurmalı
    Instrument.FLUT -> Icons.Rounded.Air // Nefesli
    Instrument.CAN -> Icons.Rounded.NotificationsActive // Çan
    Instrument.GITAR -> Icons.Rounded.Audiotrack // Telli
    Instrument.SYNTH -> Icons.Rounded.Waves // Pad
}

/** Enstrüman → kısa açıklama (chip altı). */
fun Instrument.description(): String = when (this) {
    Instrument.PIYANO -> "Sıcak ve dengeli"
    Instrument.MUZIK_KUTUSU -> "Tiz, çınlayan"
    Instrument.SEKIZ_BIT -> "Retro oyun sesi"
    Instrument.MARIMBA -> "Kısa, ahşap"
    Instrument.FLUT -> "Yumuşak, akıcı"
    Instrument.CAN -> "Uzun çınlama"
    Instrument.GITAR -> "Tele vurma"
    Instrument.SYNTH -> "Geniş, rüya gibi"
}
