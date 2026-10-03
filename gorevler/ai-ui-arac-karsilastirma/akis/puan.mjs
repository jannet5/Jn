// 100 üzerinden şeffaf puanlama. Ağırlıklar README'de açıklanır; sezgiseldir, insan değerlendirmesinin yerini tutmaz.
export const AGIRLIK = { kapsam: 30, erisilebilirlik: 25, mobil: 15, klavye: 10, ozgunluk: 10, performans: 10 };

const ETKI_CEZA = { critical: 8, serious: 5, moderate: 2, minor: 1 };

export function puanla(s) {
  const m = s.gorunumler.masaustu, mo = s.gorunumler.mobil;
  const kalem = {};

  // Brif kapsamı: masaüstü ve mobilde karşılanan maddelerin ortalaması
  const oran = (m.kapsam.tamam / m.kapsam.toplam + mo.kapsam.tamam / mo.kapsam.toplam) / 2;
  kalem.kapsam = AGIRLIK.kapsam * oran;

  // Erişilebilirlik: iki görünümdeki farklı axe kurallarının etkisine göre ceza
  const kurallar = new Map();
  for (const g of [m, mo]) for (const v of g.axeListesi) kurallar.set(v.id, v.etki);
  let ceza = 0;
  for (const etki of kurallar.values()) ceza += ETKI_CEZA[etki] || 1;
  kalem.erisilebilirlik = Math.max(0, AGIRLIK.erisilebilirlik - ceza);

  // Mobil: 390 px'te yatay taşma yoksa tam puan
  kalem.mobil = mo.tasma.var ? AGIRLIK.mobil * 0.5 * (mo.tasma.clientWidth / mo.tasma.scrollWidth) : AGIRLIK.mobil;

  // Klavye: odaklanan öğelerde görünür odak oranı (en az 5 öğe beklenir)
  const k = m.odak;
  kalem.klavye = k.odaklanan === 0 ? 0 : AGIRLIK.klavye * (k.gorunurOdak / k.odaklanan) * Math.min(1, k.odaklanan / 5);

  // Özgünlük: bilinen "AI varsayılanı" işaretleri için ceza
  const v = m.varsayilan;
  let oz = AGIRLIK.ozgunluk;
  if (v.jenerikFont) oz -= 3;
  if (v.morGradyan > 0) oz -= 3;
  if (v.kartKiti >= 4) oz -= 2;
  if (v.buyukHarfEtiket >= 5) oz -= 1;
  if (v.ortaNokta >= 3) oz -= 1;
  if (v.kremTerakota) oz -= 2;
  kalem.ozgunluk = Math.max(0, oz);

  // Performans: aktarılan bayt + konsol hatası
  const kb = m.bayt / 1024;
  let p = kb <= 300 ? 10 : kb <= 1024 ? 6 : 3;
  if (m.konsolHatalari.length || mo.konsolHatalari.length) p -= 5;
  kalem.performans = Math.max(0, p);

  for (const a of Object.keys(kalem)) kalem[a] = Math.round(kalem[a] * 10) / 10;
  const toplam = Math.round(Object.values(kalem).reduce((a, b) => a + b, 0));
  return { toplam, kalem };
}
