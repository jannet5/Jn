package com.jn.yazikart.ui // arayüz paketi

import android.graphics.Bitmap // küçük resim ve fotoğraf karesi
import androidx.media3.common.Player // önizleme oynatıcısı
import com.jn.yazikart.data.MusicTrack // müzik
import com.jn.yazikart.data.VideoClip // video / fotoğraf parçası

// Ekranların fotoğraf/video sırası ve müzikle ilgili ihtiyacı olan her şey tek pakette
class VideoUi(
    val clips: List<VideoClip>, // sıradaki parçalar
    val thumbs: Map<String, Bitmap>, // her parçanın küçük resmi (adres → resim)
    val music: MusicTrack?, // müzik (yoksa null)
    val videoSound: Boolean, // videoların kendi sesi açık mı
    val player: Player, // önizleme oynatıcısı
    val photo: Bitmap?, // önizlemede şu an gösterilen fotoğraf karesi (video oynuyorsa null)
    val onAddMedia: () -> Unit, // galeriden fotoğraf/video ekle
    val onSearchVideo: () -> Unit, // internetten video ara
    val onPickMusic: () -> Unit, // telefondan müzik seç
    val onRange: (Int, Long, Long) -> Unit, // bir parçanın aralığını değiştir (kaydırırken)
    val onRangeDone: (Int) -> Unit, // aralık ayarı bitti (önizleme o parçadan oynar)
    val onMove: (Int, Int) -> Unit, // parçayı başka bir sıraya taşı
    val onRemove: (Int) -> Unit, // parçayı kaldır
    val onMusicStart: (Long) -> Unit, // müziğin başlangıç saniyesi (kaydırırken)
    val onMusicStartDone: () -> Unit, // başlangıç ayarı bitti
    val onMusicVolume: (Float) -> Unit, // müzik sesi
    val onMusicRemove: () -> Unit, // müziği kaldır
    val onVideoSound: (Boolean) -> Unit, // videoların sesini aç/kapa
    val strip: suspend (VideoClip, Int) -> List<Bitmap?>, // kırpma şeridi için kareler
    val frameAt: suspend (VideoClip, Long) -> Bitmap?, // tek bir tam kare (kırparken önizleme)
) {
    val hasVideo: Boolean get() = clips.isNotEmpty() // sırada parça var mı
}
