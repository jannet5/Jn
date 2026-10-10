// Marka, iletişim ve FİYATLAR tek yerden değişir (kaynak: ajans/KARARLAR.md).
// TODO: müşteriye sor — gerçek numara, e-posta ve Instagram kullanıcı adı.
export const site = {
  brand: 'Vitrin Atölyesi',
  whatsapp: '905XXXXXXXXX',
  phone: '+905XXXXXXXXX',
  email: 'merhaba@vitrinatolyesi.com',
  instagram: 'vitrinatolyesi',
  whatsappMessage: 'Merhaba, işletmem için ücretsiz dijital check-up istiyorum.',
};

export const waLink = `https://wa.me/${site.whatsapp}?text=${encodeURIComponent(site.whatsappMessage)}`;
export const telLink = `tel:${site.phone}`;
export const mailLink = `mailto:${site.email}`;
export const igLink = `https://instagram.com/${site.instagram}`;

// ₺ biçimi: 2990 → "2.990 ₺"
export const tl = (n: number) => `${n.toLocaleString('tr-TR')} ₺`;

export const teklif = {
  teslimGun: 7,
  sahiplikAy: 12,
  lansmanAdet: 5,          // ilk N işletmeye kurulum 0 ₺ (vaka çalışması izni karşılığı)
  kdv: 'KDV hariç',
  // Karşılaştırma için piyasa bandı (arastirma/rakipler/OZET.md)
  ajansHafta: '4-16 hafta',
  ajansPesin: '25-75 bin ₺ peşin',
};

const ortakIstenen = ['Menü ya da hizmet listesi ve fiyatlar', 'Fotoğraflar (telefonla çekim rehberi veriyoruz)', 'Logo (yoksa birlikte netleştiriyoruz)'];

export const paketler = [
  {
    id: 'vitrin', ad: 'Vitrin', aylik: 2990, kurulum: 4990,
    kim: 'Sitesi olmayan ya da eskiyen işletme',
    dahil: ['Telefonda hızlı açılan site, Google\'da şehir ve hizmet adıyla bulunur', 'Barındırma, bakım ve küçük güncellemeler', 'Google İşletme Profili düzenlemesi', 'WhatsApp ve arama butonu', 'Aylık değer defteri'],
    haric: ['Sosyal medya paylaşımları', 'Reklam yönetimi', 'Mobil uygulama'],
    istenen: ortakIstenen,
  },
  {
    id: 'makine', ad: 'Müşteri Makinesi', aylik: 5990, kurulum: 9990, one: true,
    kim: 'Mesajı ve aramayı kaçırmak istemeyen işletme',
    dahil: ['Vitrin\'deki her şey', 'WhatsApp ve siteden gelen talepler tek yerde', 'Cevapsız aramaya otomatik mesaj', '3 adımlı otomatik takip mesajı', 'Google yorumu isteme ve yanıtlama', 'Ayda 12 paylaşım, sitenizle aynı görsel dil'],
    haric: ['Reklam yönetimi', 'Mobil uygulama', 'WhatsApp ve SMS gönderim ücretleri (kullandığınız kadar)'],
    istenen: [...ortakIstenen, 'Paylaşımlar için ayda bir kısa onay'],
  },
  {
    id: 'buyume', ad: 'Büyüme', aylik: 11990, kurulum: 24990,
    kim: 'Randevuyu ya da siparişi büyütmek isteyen işletme',
    dahil: ['Müşteri Makinesi\'ndeki her şey', 'Markanıza özel mobil uygulama (iOS ve Android)', 'Online randevu ya da sipariş', 'Instagram ve Google reklam yönetimi', 'Reklamın getirdiğini gösteren aylık rapor'],
    haric: ['Reklam bütçesi (Meta\'ya ve Google\'a siz ödersiniz)', 'App Store ve Google Play geliştirici hesabı ücreti', 'WhatsApp ve SMS gönderim ücretleri (kullandığınız kadar)'],
    istenen: [...ortakIstenen, 'Reklam bütçesi ve ödeme yöntemi'],
  },
];

export const enDusukAylik = Math.min(...paketler.map(p => p.aylik));
