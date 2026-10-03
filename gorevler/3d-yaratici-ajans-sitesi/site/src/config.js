// Tek ayar noktası: spiral galerinin görünümü ve hareketi buradan yönetilir.
export const CONFIG = {
  // Galeri içeriği
  totalImages: 12,          // benzersiz görsel sayısı (prosedürel üretilir)
  tilesPerRevolution: 12,   // bir turdaki karo sayısı
  revolutions: 4,           // spiralin tur sayısı
  startRadius: 4.6,         // üst uç yarıçapı
  endRadius: 3.4,           // alt uç yarıçapı
  tileHeightRatio: 1.25,    // karo yüksekliği / kiriş uzunluğu
  tileSegments: 20,         // karo eğriliği için yatay bölüt
  spiralGap: 0.34,          // ardışık karolar arası dikey adım
  tileGap: 0.035,           // karolar arası yatay boşluk (radyan)

  // Kamera
  cameraZ: 11.5,
  cameraSmoothing: 0.08,
  cameraTravel: 0.55,       // sayfa ilerlemesi başına kameranın dikey yolu (spiral yüksekliğine oranla)

  // Hareket
  baseRotationSpeed: 0.0016,    // boştayken sürekli dönüş (radyan/kare)
  scrollSpin: 0.0042,           // kaydırma hızının dönüşe katkısı
  maxSpin: 0.22,                // tek karedeki en büyük dönüş
  spinDecay: 0.92,              // dönüş ivmesinin sönümü
  mouseRotate: 0.55,            // fare X ekseninin galeriyi döndürme miktarı (radyan)
  mouseTilt: 0.12,              // fare ile eğim (parallax)
  mouseSmoothing: 0.06,

  // Hover (karo geriye çekilir)
  hoverPush: 0.14,              // yarıçapın küçülme oranı
  hoverSmoothing: 0.1,

  maxPixelRatio: 1.75,
};
