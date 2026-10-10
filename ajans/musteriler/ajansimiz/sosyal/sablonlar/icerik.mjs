// Tüm görsellerin içeriği. Her öğe: { id, cikti (sosyal/ altına göre klasör), boyut, slaytlar: [html] }
import { T, I, mark, top, etiket, sayac, alt, eskiSite, yeniSite } from './stil.mjs';

const s = (oran, tema, ic) => `<div class="s ${oran} ${tema}">${ic}</div>`;
const p45 = (tema, ic) => s('r45', tema, ic);

// ---------- carousel yardımcıları ----------
const kapak = (n, eb, baslik, alt_, tema = '', sag) => p45(tema, `${top(tema, sag ?? sayac(1, n))}
  <div class="main"><div class="eb">${eb}</div><div class="d">${baslik}</div>${alt_ ? `<p class="lead mut mt16">${alt_}</p>` : ''}</div>
  <div class="alt"><span>Kaydırın</span><span>${n} slayt</span></div>`);
const madde = (i, n, no, baslik, metin, gorsel = '') => p45('', `${top('', sayac(i, n))}
  <div class="main"><div class="no">${no}</div><div class="h2 mt16">${baslik}</div><p class="body mut mt12">${metin}</p>${gorsel}</div>
  ${alt()}`);
const ozet = (i, n, baslik, maddeler) => p45('surface', `${top('surface', sayac(i, n))}
  <div class="main"><div class="eb">Kaydedin</div><div class="h2">${baslik}</div>
  <ul class="liste mt24">${maddeler.map(m => `<li>${I('check', 20)}<span>${m}</span></li>`).join('')}</ul></div>
  ${alt()}`);
const cta = (i, n, baslik, metin) => p45('ink', `${top('ink', sayac(i, n))}
  <div class="main"><div class="eb">Ücretsiz</div><div class="d">${baslik}</div><p class="lead mut mt16">${metin}</p>
  <span class="btn mt24">${I('msg', 18)}DM'den ÖRNEK yaz</span></div>
  <div class="alt"><span>Vitrin Atölyesi</span><span>Site · Sosyal medya · Reklam</span></div>`);

// mini paletler (DESIGN §8: konseptin kendi küçük paleti)
const P = {
  tesisat: { bg: '#F4F7FB', fg: '#0F2236', mut: '#4A5B6E', line: '#D5DEE8', acc: '#0F2236', accFg: '#FFFFFF' },
  kafe: { bg: '#F7F1EA', fg: '#2B1A10', mut: '#6B4F3C', line: '#E5D7C8', acc: '#5C3A24', accFg: '#FFFFFF' },
  restoran: { bg: '#FFFFFF', fg: '#1D1A16', mut: '#5E574E', line: '#E6E1DA', acc: '#1F7A4D', accFg: '#FFFFFF' },
};

const telEski = (baslik, renk) => `<div class="tel k"><div class="ek">${eskiSite(baslik, renk)}</div></div>`;
const telYeni = (o, k = true) => `<div class="tel${k ? ' k' : ''}"><div class="ek">${yeniSite(o)}</div></div>`;
const tesisatYeni = { ad: 'Sizin Tesisat', slogan: ['Su kaçağı · Tıkanıklık · Kombi', 'Aynı gün geliyoruz.'], satirlar: [['Su kaçağı', 'Fiyat al'], ['Tıkanıklık', 'Fiyat al'], ['Kombi bakımı', 'Fiyat al'], ['Hizmet bölgesi', '[ilçe]']], buton: 'Hemen ara', ikon: 'phone', p: P.tesisat };
const kafeYeni = { ad: 'Sizin Kafe', slogan: ['Menü · Saatler · Yol tarifi', 'Sabah kahvesi burada.'], satirlar: [['Filtre kahve', '[fiyat]'], ['Latte', '[fiyat]'], ['Günün keki', '[fiyat]'], ['Açık', '08.00-22.00']], buton: 'Yol tarifi', ikon: 'route', p: P.kafe };

const onceSonraKapak = (no, n, baslik, eskiBaslik, eskiRenk, yeni) => p45('ink', `${top('ink', etiket())}
  <div class="eb" style="margin-top:18px">Önce / Sonra #${no}</div><div class="h2">${baslik}</div>
  <div class="main kapak"><div class="ciftel">
    <div><div class="tl"><i style="background:#A3AFA9"></i>Önce</div>${telEski(eskiBaslik, eskiRenk)}</div>
    <div><div class="tl"><i style="background:${T.dPrimary}"></i>Sonra</div>${telYeni(yeni)}</div>
  </div></div>
  <div class="alt"><span>Kaydırın, farkı anlatalım</span>${sayac(1, n)}</div>`);
const onceDetay = (i, n, baslik, telHtml, sorunlar, not) => p45('', `${top('', etiket())}
  <div class="eb" style="margin-top:18px">Önce</div><div class="h3">${baslik}</div>
  <div class="main" style="flex-direction:row;align-items:center;gap:18px">
    ${telHtml}
    <ul class="liste" style="gap:12px;flex-shrink:1;min-width:0">${sorunlar.map(m => `<li style="font-size:15px">${I('x', 18)}<span>${m}</span></li>`).join('')}</ul>
  </div>
  <div class="alt"><span class="mut">${not}</span>${sayac(i, n)}</div>`);
const sonraDetay = (i, n, baslik, telHtml, artilar) => p45('surface', `${top('surface', etiket())}
  <div class="eb" style="margin-top:18px">Sonra</div><div class="h3">${baslik}</div>
  <div class="main" style="flex-direction:row;align-items:center;gap:18px">
    ${telHtml}
    <ul class="liste" style="gap:12px;flex-shrink:1;min-width:0">${artilar.map(m => `<li style="font-size:15px">${I('check', 18)}<span>${m}</span></li>`).join('')}</ul>
  </div>
  <div class="alt"><span class="mut">Sahte isim değil, yer tutucu: "Sizin ..."</span>${sayac(i, n)}</div>`);
const neDegisti = (i, n, maddeler) => p45('', `${top('', sayac(i, n))}
  <div class="main"><div class="eb">Ne değişti?</div><div class="h2">Üç küçük karar, <em>bir</em> büyük fark</div>
  <div class="mt24" style="display:flex;flex-direction:column;gap:10px">${maddeler.map(([a, b], k) => `<div class="kart" style="display:flex;gap:14px;align-items:flex-start"><span class="h3" style="color:${T.primary}">${k + 1}</span><span><b class="body" style="font-weight:800">${a}</b><br><span class="sm mut">${b}</span></span></div>`).join('')}</div></div>
  ${alt()}`);

// ---------- reel kapakları (9:16; yazı ortadaki 3:4 güvenli alanda) ----------
const reelKapak = (eb, baslik, alt_) => s('r916', 'ink', `${top('ink', `<span class="etiket">${I('play', 12)} Reel</span>`)}
  <div class="main"><div class="eb">${eb}</div><div class="d" style="font-size:42px">${baslik}</div><p class="lead mut mt16">${alt_}</p></div>
  <div class="alt"><span>Vitrin Atölyesi</span><span>DM'den <b>ÖRNEK</b> yaz</span></div>`);

// ---------- içerik listesi ----------
export const ICERIK = [];
const ekle = (id, klasor, boyut, slaytlar, tur) => ICERIK.push({ id, klasor, boyut, slaytlar, tur });

// PROFİL
ekle('profil-gorseli', 'profil', '11', [s('r11', '', `<div style="width:360px;height:360px;display:flex;align-items:center;justify-content:center;background:${T.fg}">
  <svg width="170" height="170" viewBox="0 0 32 32" aria-hidden="true"><path d="M8 9l8 15 8-15" stroke="${T.dPrimary}" stroke-width="3.6" fill="none" stroke-linecap="round" stroke-linejoin="round"/></svg></div>`)], 'profil');
const oneCikan = (ikon, ad) => s('r916', 'ink', `<div style="position:absolute;inset:0;display:flex;flex-direction:column;align-items:center;justify-content:center;gap:22px">
  <div style="width:200px;height:200px;border-radius:50%;background:${T.dSurface};border:2px solid ${T.dPrimary};display:flex;align-items:center;justify-content:center;color:${T.dFg}">${I(ikon, 84, 'style="stroke-width:1.6"')}</div>
  <div class="h3" style="color:${T.dFg}">${ad}</div></div>`);
ekle('one-cikan-ornekler', 'profil', '916', [oneCikan('layout', 'Örnekler')], 'profil');
ekle('one-cikan-fiyat', 'profil', '916', [oneCikan('tag', 'Fiyat')], 'profil');
ekle('one-cikan-surec', 'profil', '916', [oneCikan('route', 'Süreç')], 'profil');
ekle('one-cikan-sss', 'profil', '916', [oneCikan('help', 'SSS')], 'profil');

// GÜN 1 tanıtım
ekle('01-tanisalim', 'gonderiler/01-tanisalim', '45', [p45('', `${top('', '<span class="sayac">Merhaba</span>')}
  <div class="main"><div class="eb">Biz Vitrin Atölyesi</div>
  <div class="d">Yerel işletmeye site, sosyal medya ve reklam. <em>Tek</em> yerden.</div>
  <p class="lead mut mt16">Restoran, kafe, berber, klinik, usta. Telefonda düzgün görünen bir vitrin, 7 günde yayında.</p>
  <div class="mt24" style="display:flex;flex-wrap:wrap;gap:8px">
    <span class="chip">${I('layout', 16)}&nbsp;Web sitesi</span><span class="chip">${I('insta', 16)}&nbsp;Sosyal medya</span>
    <span class="chip">${I('megaphone', 16)}&nbsp;Reklam</span><span class="chip">${I('smartphone', 16)}&nbsp;Uygulama</span></div></div>
  ${alt()}`)], 'tekil');

// GÜN 3 reel
ekle('03-reel-biz-kimiz', 'gonderiler/03-reel-biz-kimiz', '916', [reelKapak('30 saniyede', 'Biz kimiz, ne <em>yapıyoruz?</em>', 'Ekrandan izleyin: bir işletmenin örnek ana sayfası nasıl çıkıyor.')], 'reel');

// GÜN 4 carousel: 7 şey (10 slayt)
{
  const n = 10, m = [
    ['Tek dokunuşla arama ve WhatsApp', 'Numarayı kopyalatmayın. Ekranın altında sabit duran bir buton yeter.'],
    ['Açık adres ve yol tarifi', 'Yol tarifi düğmesi Google Haritalar\'ı doğrudan açsın.'],
    ['Güncel çalışma saatleri', 'Bayramda, tatilde saat değişiyorsa sitede de aynı gün değişsin.'],
    ['Fiyat ya da başlangıç fiyatı', 'Tam liste şart değil. "Şu fiyattan başlar" demek bile birçok soruyu baştan cevaplar.'],
    ['Gerçek fotoğraf', 'Stok fotoğraf değil. Sizin dükkânınız, ürününüz, ekibiniz.'],
    ['Telefonda hızlı açılış', 'Müşterinin çoğu telefondan bakıyor. Sayfa geç açılırsa beklemeden geri dönüyor.'],
    ['Google profilinizle aynı bilgi', 'Adres, saat ve telefon her yerde aynı olsun. Farklıysa müşteri hangisine güveneceğini bilemez.'],
  ];
  ekle('04-sitede-7-sey', 'gonderiler/04-sitede-7-sey', '45', [
    kapak(n, 'İşletme sahibine not', 'Müşteri sitenize girince bu <em>7 şeyi</em> arıyor', 'Kaydedin, kendi sitenize bakarken yanınızda dursun.'),
    ...m.map(([a, b], k) => madde(k + 2, n, `0${k + 1}`, a, b)),
    ozet(9, n, '7 maddelik kontrol listesi', ['Arama ve WhatsApp butonu', 'Adres ve yol tarifi', 'Güncel saatler', 'Başlangıç fiyatı', 'Gerçek fotoğraf', 'Telefonda hızlı açılış', 'Google profiliyle aynı bilgi']),
    cta(10, n, 'Sitenize birlikte bakalım', 'Kaç madde eksik, size yazalım. İsterseniz işletmeniz için örnek bir ana sayfa da hazırlarız.'),
  ], 'carousel');
}

// GÜN 5 önce/sonra #1 tesisat (5 slayt)
ekle('05-once-sonra-tesisat', 'gonderiler/05-once-sonra-tesisat', '45', [
  onceSonraKapak(1, 5, 'Tesisatçı sitesi: telefonda <em>ara</em> butonu nerede?', 'USTA TESİSAT HİZMETLERİ', '#2B4C9B', tesisatYeni),
  onceDetay(2, 5, 'Masaüstü için yapılmış, telefonda küçülen sayfa', telEski('USTA TESİSAT HİZMETLERİ', '#2B4C9B'),
    ['Yazılar okunmuyor, yakınlaştırmak gerekiyor', 'Telefon numarası resmin içinde, dokununca aranmıyor', 'Hangi semtlere gelindiği yazmıyor'], 'Temsili eski site, gerçek bir işletme değil'),
  sonraDetay(3, 5, 'Telefon için baştan kurulan sayfa', telYeni(tesisatYeni), ['Ekranın altında sabit "Hemen ara"', 'Hizmetler tek tek, fiyat sorma kolay', 'Hizmet bölgesi en üstte']),
  neDegisti(4, 5, [['Önce telefon', 'Sayfa önce telefona göre çizildi, masaüstü sonra.'], ['Tek aksiyon', 'Her ekranda aynı buton: Hemen ara.'], ['Bölge açık', 'Müşteri "bize gelir mi" diye sormadan görüyor.']]),
  cta(5, 5, 'Sizin için de çizelim', 'İşletmenizin adını ve ne iş yaptığınızı yazın. Örnek ana sayfayı ücretsiz hazırlayalım.'),
], 'carousel');

// GÜN 6 SSS
ekle('06-sss-aylik-mi', 'gonderiler/06-sss-aylik-mi', '45', [p45('surface', `${top('surface', '<span class="sayac">SSS</span>')}
  <div class="main"><div class="eb">En çok sorulan</div><div class="d">Site aylık mı, tek <em>seferlik</em> mi?</div>
  <div class="mt24" style="display:flex;flex-direction:column;gap:10px">
    <div class="kart"><b class="body" style="font-weight:800">Aylık.</b> <span class="body mut">Kurulum bir kez, sonra aylık bakım: barındırma, güncelleme, küçük değişiklikler.</span></div>
    <div class="kart"><b class="body" style="font-weight:800">12 ay sonra site sizin.</b> <span class="body mut">İsterseniz başka yere taşırsınız.</span></div>
  </div></div>
  ${alt('Başka sorunuz mu var?', "DM'den yazın")}`)], 'tekil');

// GÜN 7 reel
ekle('07-reel-haritalar', 'gonderiler/07-reel-haritalar', '916', [reelKapak('Kulis', 'Sitesi olmayan işletmeyi <em>nasıl</em> buluyoruz?', 'Google Haritalar\'da 3 adım. Sizinkine de bakalım mı?')], 'reel');

// GÜN 8 carousel: WhatsApp sipariş (8 slayt)
{
  const n = 8;
  const balon = `<div class="mt16" style="align-self:flex-start;max-width:290px;background:#E7F6EC;border:1px solid #CDE9D6;border-radius:14px 14px 14px 4px;padding:10px 12px;font-size:15px;line-height:1.4;color:${T.fg}">Merhaba, 2 adet lahmacun ve 1 ayran istiyorum. Adres: [adres]</div>`;
  ekle('08-whatsapp-siparis', 'gonderiler/08-whatsapp-siparis', '45', [
    kapak(n, 'Restoranlar için', 'WhatsApp\'tan sipariş alan sayfa <em>nasıl</em> olur?', 'Uygulama kurmadan, komisyonsuz bir sipariş kanalı. 5 parça.'),
    madde(2, n, '01', 'Menü telefonda okunur', 'Kategoriler üstte sekme olarak, fiyat ürünün hemen yanında. PDF menü yok.'),
    madde(3, n, '02', 'Ürüne dokun, mesaj hazır', 'Müşteri seçer, WhatsApp hazır mesajla açılır. Yazmakla uğraşmaz.', balon),
    madde(4, n, '03', 'Teslimat bölgesi açık', 'Hangi mahallelere gittiğiniz ve minimum tutar menünün üstünde yazar.'),
    madde(5, n, '04', 'Saatler canlı', 'Mutfak kapandıysa buton "Yarın 11.00\'de açığız" der. Boşa mesaj gelmez.'),
    madde(6, n, '05', 'Kendi kanalınız', 'Paket platformları sipariş başına komisyon alır. Kendi sayfanızdan gelen siparişte bu kesinti yok.'),
    ozet(7, n, 'Kısa liste', ['Telefonda okunan menü', 'Hazır mesajla WhatsApp', 'Teslimat bölgesi ve minimum tutar', 'Canlı çalışma saatleri', 'Komisyonsuz kendi kanalınız']),
    cta(8, n, 'Menünüzü sayfaya çevirelim', 'Menünüzün fotoğrafını DM\'den atın. Örnek sipariş sayfasını ücretsiz hazırlayalım.'),
  ], 'carousel');
}

// GÜN 9 önce/sonra #2 kafe (5 slayt)
{
  const igEski = `<div class="tel k"><div class="ek" data-dek style="background:#fff;padding:8px 6px;font-size:6px;color:#333">
    <div style="display:flex;gap:6px;align-items:center"><div style="width:26px;height:26px;border-radius:50%;background:#d9cfc4"></div><div style="flex:1">${'<div style="height:3px;background:#ccc;margin:3px 0"></div>'.repeat(3)}</div></div>
    <div style="display:flex;gap:4px;margin:7px 0">${'<div style="width:18px;height:18px;border-radius:50%;border:1px solid #bbb"></div>'.repeat(4)}</div>
    <div style="display:grid;grid-template-columns:repeat(3,1fr);gap:2px">${Array.from({ length: 12 }, (_, k) => `<div style="aspect-ratio:1;background:${['#d8cbbd', '#c9b8a6', '#e3d8cc', '#bfae9b'][k % 4]}"></div>`).join('')}</div>
    <div style="margin-top:6px;border:1px solid #ddd;border-radius:4px;padding:3px">"Kaça kadar açıksınız?"</div>
    <div style="margin-top:3px;border:1px solid #ddd;border-radius:4px;padding:3px">"Menü var mı?"</div></div></div>`;
  ekle('09-once-sonra-kafe', 'gonderiler/09-once-sonra-kafe', '45', [
    p45('ink', `${top('ink', etiket())}
      <div class="eb" style="margin-top:18px">Önce / Sonra #2</div><div class="h2">Kafe: menü hikâyede <em>kaybolunca</em></div>
      <div class="main kapak"><div class="ciftel"><div><div class="tl"><i style="background:#A3AFA9"></i>Önce</div>${igEski}</div>
      <div><div class="tl"><i style="background:${T.dPrimary}"></i>Sonra</div>${telYeni(kafeYeni)}</div></div></div>
      <div class="alt"><span>Kaydırın, farkı anlatalım</span>${sayac(1, 5)}</div>`),
    onceDetay(2, 5, 'Her şey Instagram\'da, ama aranınca bulunmuyor', igEski,
      ['Menü eski bir öne çıkan hikâyede', 'Saatler yorumlarda soruluyor', 'Google\'da arayan siteye ulaşamıyor'], 'Temsili profil, gerçek bir işletme değil'),
    sonraDetay(3, 5, 'Tek sayfa: menü, saat, yol tarifi', telYeni(kafeYeni), ['Menü ve fiyat ilk ekranda', 'Açık/kapalı bilgisi görünür', 'Yol tarifi tek dokunuş']),
    neDegisti(4, 5, [['Menü sabit adreste', 'Hikâye 24 saatte kaybolur, sayfa kalır.'], ['Soru azalır', 'Saat ve fiyat görünürse DM\'ler siparişe döner.'], ['Instagram kalır', 'Profil linki bu sayfaya gider, ikisi birlikte çalışır.']]),
    cta(5, 5, 'Kafeniz için de çizelim', 'Menünüzü ve adresinizi yazın. Örnek ana sayfayı ücretsiz hazırlayalım.'),
  ], 'carousel');
}

// GÜN 10 sosyal kanıt yerine dürüstlük
ekle('10-henuz-yorum-yok', 'gonderiler/10-henuz-yorum-yok', '45', [p45('ink', `${top('ink', '<span class="sayac">Açık konuşalım</span>')}
  <div class="main"><div class="d">Henüz müşteri yorumumuz <em>yok.</em></div>
  <p class="lead mut mt16">Yeni açıldık. Sahte yorum koymak yerine şunu yapıyoruz:</p>
  <ul class="liste mt24">
    <li>${I('check', 20)}<span>Örnek işleri açıkça "Örnek konsept" diye etiketliyoruz.</span></li>
    <li>${I('check', 20)}<span>Önce ücretsiz örnek ana sayfanızı görüyorsunuz, sonra karar veriyorsunuz.</span></li>
    <li>${I('check', 20)}<span>İlk 5 işletmeye kurulum 0 ₺. Karşılığında vaka çalışması izni.</span></li>
  </ul></div>
  <div class="alt"><span>İlk yorum sizinki olsun mu?</span><span>DM'den <b>ÖRNEK</b> yaz</span></div>`)], 'tekil');

// GÜN 11 teklif kartı
{
  const pk = (ad, fiyat, kur, icerik, one = false) => `<div class="kart" style="${one ? `border:2px solid ${T.primary};` : ''}padding:9px 14px">
    <div style="display:flex;justify-content:space-between;align-items:baseline;gap:8px"><span class="h3" style="font-size:19px">${ad}</span>${one ? `<span class="sm" style="color:${T.primary}">Önerilen</span>` : ''}</div>
    <div style="display:flex;align-items:baseline;gap:6px;margin-top:2px"><span style="font-weight:800;font-stretch:112%;font-size:24px;letter-spacing:-.02em">${fiyat} ₺</span><span class="sm mut">/ay · kurulum ${kur} ₺</span></div>
    <div class="sm mut" style="margin-top:4px">${icerik}</div></div>`;
  ekle('11-paketler', 'gonderiler/11-paketler', '45', [p45('', `${top('', '<span class="sayac">Fiyatlar açık</span>')}
    <div class="h2" style="font-size:30px;margin-top:16px">İlk 5 işletmeye kurulum <em>0 ₺</em></div>
    <div class="main" style="gap:7px;justify-content:flex-start;margin-top:12px;margin-bottom:12px">
      ${pk('Vitrin', '2.990', '4.990', 'Site, bakım, Google profili, WhatsApp butonu')}
      ${pk('Müşteri Makinesi', '5.990', '9.990', '+ otomatik takip, yorum isteme, ayda 12 gönderi', true)}
      ${pk('Büyüme', '11.990', '24.990', '+ randevu, size özel uygulama, reklam yönetimi')}
    </div>
    <div class="alt"><span class="mut">KDV hariç · reklam bütçesi hariç</span><span>DM'den <b>ÖRNEK</b> yaz</span></div>`)], 'tekil');
}

// GÜN 12 reel
ekle('12-reel-hizlandirilmis', 'gonderiler/12-reel-hizlandirilmis', '916', [reelKapak('Hızlandırılmış', 'Bir örnek ana sayfa, <em>baştan</em> sona', 'Boş sayfadan telefondaki son hâline. Ekran kaydı, kesintisiz.')], 'reel');

// GÜN 13 carousel: Google yorumları (7 slayt)
{
  const n = 7;
  const yanit = (k, t) => `<div class="kart mt16" style="background:${T.surface}"><div class="sm mut">${k}</div><div class="body" style="margin-top:4px">${t}</div></div>`;
  ekle('13-google-yorumlari', 'gonderiler/13-google-yorumlari', '45', [
    kapak(n, 'İşletme sahibine not', 'Google yorumlarına cevap <em>vermemek</em> neye mal oluyor?', 'Yorumu yazan bir kişi, cevabınızı okuyan çok kişi.'),
    madde(2, n, '01', 'Cevabı yeni müşteri okuyor', 'Sizi ilk kez arayan kişi yorumlarla birlikte cevaplarınıza da bakıyor. Sessizlik "ilgilenmiyorlar" diye okunuyor.'),
    madde(3, n, '02', 'Kötü yoruma sakin cevap', 'Tartışmayın, özür ve çözüm yazın. Okuyan kişi yorumu değil, tavrınızı hatırlar.',
      yanit('Örnek cevap', 'Yaşadığınız gecikme için özür dileriz. Bize [telefon] üzerinden ulaşırsanız hemen ilgilenelim.')),
    madde(4, n, '03', 'İyi yoruma kısa teşekkür', 'İsmiyle ve yorumdaki bir detayla. Her yoruma aynı kopyala-yapıştır cevap verilmiyor.'),
    madde(5, n, '04', 'Haftada bir, 10 dakika', 'Pazartesi sabahı yeni yorumlara bakın. Düzenli olmak, uzun cevaptan daha önemli.'),
    ozet(6, n, 'Kaydedin', ['Her yoruma cevap, iyi ya da kötü', 'Kötüye özür ve çözüm, tartışma yok', 'İyiye isimle kısa teşekkür', 'Haftada bir sabit gün']),
    cta(7, n, 'Profilinize birlikte bakalım', 'Google profilinizdeki eksikleri ve cevapsız yorumları ücretsiz çıkaralım.'),
  ], 'carousel');
}

// ---------- REKLAM: 5 kreatif × 2 boyut ----------
const rk = (oran, tema, ic) => s(oran, tema, ic);
const reklamlar = [
  { id: 'r1-google-da-yoksaniz', tema: '', eb: 'Yerel işletme sahibine', baslik: 'Müşteri sizi arıyor. <em>Bulabiliyor</em> mu?', alt: 'Telefonda düzgün açılan bir site, Google profili ve WhatsApp butonu. 7 günde yayında.', buton: 'Örnek ana sayfanızı isteyin', ek: '' },
  { id: 'r2-once-sonra', tema: 'ink', eb: '', baslik: 'Aynı işletme, <em>iki</em> farklı ilk izlenim', alt: '', buton: 'Ücretsiz örnek isteyin', ek: 'oncesonra' },
  { id: 'r3-fiyat-acik', tema: 'surface', eb: 'Fiyatlar açık', baslik: 'Site + bakım aylık <em>2.990 ₺</em>', alt: 'Kurulum ayrı, KDV hariç. 12 ay sonra site sizin. Pazarlık yok, sürpriz yok.', buton: 'Paketleri görün', ek: '' },
  { id: 'r4-7-gunde', tema: '', eb: 'Klasik ajans haftalar sürer', baslik: 'Bir haftada <em>yayında.</em>', alt: '', buton: 'Ücretsiz örnek isteyin', ek: 'adimlar' },
  { id: 'r5-ilk-5', tema: 'ink', eb: 'Yeni açıldık', baslik: 'İlk 5 işletmeye kurulum <em>0 ₺</em>', alt: 'Karşılığında tek isteğimiz: işinizi vaka çalışması olarak paylaşma izni.', buton: 'Yerinizi sorun', ek: '' },
];
const adimlar = `<div class="mt24" style="display:flex;flex-direction:column;gap:8px">${[['1. gün', '10 dakikalık görüşme'], ['2-3. gün', 'Üç tasarım yönü'], ['4-6. gün', 'Site ve profiller'], ['7. gün', 'Yayında']].map(([a, b]) => `<div style="display:flex;gap:12px;align-items:center;border-top:1px solid ${T.border};padding-top:8px"><span class="sm" style="width:72px;color:${T.primary}">${a}</span><span class="body" style="font-weight:500">${b}</span></div>`).join('')}</div>`;
const reklamIc = (r, dikey) => {
  const g = r.ek === 'oncesonra' ? `<div class="ciftel mt16 kapak"><div><div class="tl"><i style="background:#A3AFA9"></i>Önce</div>${telEski('USTA TESİSAT HİZMETLERİ', '#2B4C9B')}</div><div><div class="tl"><i style="background:${T.dPrimary}"></i>Sonra</div>${telYeni(tesisatYeni)}</div></div>`
    : r.ek === 'adimlar' ? adimlar : '';
  return `${top(r.tema, r.ek === 'oncesonra' ? etiket() : '<span class="sayac">Sponsorlu</span>')}
  <div class="main"${dikey ? ' style="justify-content:center"' : ''}>${r.eb ? `<div class="eb">${r.eb}</div>` : ''}<div class="${r.ek === 'oncesonra' ? 'h2 mt12' : 'd'}"${dikey && r.ek !== 'oncesonra' ? ' style="font-size:46px"' : ''}>${r.baslik}</div>
  ${r.alt ? `<p class="lead mut mt16">${r.alt}</p>` : ''}${g}
  <span class="btn ${r.ek === 'oncesonra' ? 'mt16' : 'mt24'}">${I('msg', 18)}${r.buton}</span></div>
  <div class="alt"><span>Vitrin Atölyesi</span><span>Site · Sosyal · Reklam</span></div>`;
};
for (const r of reklamlar) {
  ekle(`${r.id}-feed`, 'reklam', '45', [rk('r45', r.tema, reklamIc(r, false))], 'reklam');
  // 9:16: üst ve alt ~%14 Meta arayüzü altında kalır → ek iç boşluk
  ekle(`${r.id}-story`, 'reklam', '916', [rk('r916', r.tema, `<div style="height:64px"></div>${reklamIc(r, true)}<div style="height:72px"></div>`)], 'reklam');
}
