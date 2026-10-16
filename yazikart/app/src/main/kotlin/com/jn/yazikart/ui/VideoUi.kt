package com.jn.yazikart.ui // arayüz paketi

import androidx.media3.common.Player // video oynatıcısı
import com.jn.yazikart.data.VideoClip // video parçası

// Ekranların video ile ilgili ihtiyaç duyduğu her şey tek pakette (parametre kalabalığı olmasın)
class VideoUi(
    val clips: List<VideoClip>, // arka arkaya videolar
    val player: Player, // önizleme oynatıcısı
    val onSearch: () -> Unit, // internetten video ara
    val onGallery: () -> Unit, // galeriden video seç
    val onRange: (Int, Long, Long) -> Unit, // bir videonun aralığını değiştir (kaydırırken)
    val onRangeDone: (Int) -> Unit, // aralık ayarı bitti
    val onMoveUp: (Int) -> Unit, // videoyu bir sıra yukarı al
    val onRemove: (Int) -> Unit, // videoyu kaldır
) {
    val hasVideo: Boolean get() = clips.isNotEmpty() // arka planda video var mı
}
