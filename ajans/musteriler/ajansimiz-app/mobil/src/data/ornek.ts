// Örnek içerik: açıkça temsili, yuvarlak fiyatlar. Gerçek işletme/iddia değildir.
export const KAFE = { ad: 'Örnek Kahve Evi', hedef: 10 };

export const BERBER = {
  ad: 'Örnek Berber Salonu',
  hizmetler: [
    { id: 'sac', ad: 'Saç kesimi', sure: 30, fiyat: 400 },
    { id: 'sakal', ad: 'Sakal', sure: 20, fiyat: 250 },
    { id: 'ikisi', ad: 'Saç + sakal', sure: 45, fiyat: 600 },
  ],
  saatler: ['10:00', '10:30', '11:00', '11:30', '13:00', '13:30', '14:00', '15:00', '16:00', '16:30', '17:00', '18:00'],
};

export const RESTORAN = {
  ad: 'Örnek Lokanta',
  kategoriler: [
    {
      id: 'ana',
      ad: 'Ana yemek',
      urunler: [
        { id: 'kofte', ad: 'Izgara köfte', aciklama: 'Pilav, közlenmiş biber', fiyat: 350 },
        { id: 'tavuk', ad: 'Tavuk şiş', aciklama: 'Bulgur pilavı, ezme', fiyat: 320 },
        { id: 'manti', ad: 'Ev mantısı', aciklama: 'Yoğurt, tereyağlı sos', fiyat: 280 },
      ],
    },
    {
      id: 'baslangic',
      ad: 'Başlangıç',
      urunler: [
        { id: 'mercimek', ad: 'Mercimek çorbası', aciklama: 'Limon, kıtır ekmek', fiyat: 120 },
        { id: 'humus', ad: 'Humus', aciklama: 'Tereyağlı pastırma', fiyat: 180 },
      ],
    },
    {
      id: 'tatli',
      ad: 'Tatlı',
      urunler: [
        { id: 'sutlac', ad: 'Fırın sütlaç', aciklama: 'Fındık', fiyat: 140 },
        { id: 'kunefe', ad: 'Künefe', aciklama: 'Antep fıstığı', fiyat: 220 },
      ],
    },
  ],
};

export const tl = (n: number) => `${n.toLocaleString('tr-TR')} ₺`;
